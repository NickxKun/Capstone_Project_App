#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>
#include <BLE2902.h>
#include "HX711.h"
#include "analogWrite.h"
#define calibration_factor 211.85 //This value is obtained using the SparkFun_HX711_Calibration sketch

#define DOUT 16
#define CLK 4

// init Class:
HX711 scale;

#define PIN_RED    27 
#define PIN_GREEN  26 
#define PIN_BLUE   25 

char str[8] = "";
char valStr[8];
BLECharacteristic *pCharacteristic;
bool deviceConnected = false;
int vals = 0;
int stat = 0;
bool flag = 0;
BLEServer *pServer;

#define SERVICE_UUID           "6E400001-B5A3-F393-E0A9-E50E24DCCA9E"
#define CHARACTERISTIC_UUID_RX "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
#define CHARACTERISTIC_UUID_TX "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"

class MyServerCallbacks: public BLEServerCallbacks {
    void onConnect(BLEServer* pServer) {
      deviceConnected = true;
    };

    void onDisconnect(BLEServer* pServer) {
      deviceConnected = false;
      pServer->getAdvertising()->start();      
    }
};

class MyCallbacks: public BLECharacteristicCallbacks 
{
  void onWrite(BLECharacteristic *pCharacteristic) 
  {
    std::string RXvalue = pCharacteristic->getValue().c_str(); 
    uint8_t* rx_data = pCharacteristic->getData();
    vals = (int)(*rx_data);
    if (RXvalue.length() > 0) {
      Serial.println("*********");
      Serial.print("Recieved value: ");
      Serial.println(vals);
    }
    Serial.println("*********");                
  }
};  
 

void setup() 
{
  Serial.begin(9600);
     
  BLEDevice::init("ESP32_2");
  pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());
  
  BLEService *pService = pServer->createService(SERVICE_UUID);

  pCharacteristic = pService->createCharacteristic(
                                         CHARACTERISTIC_UUID_TX,
                                         BLECharacteristic::PROPERTY_READ
                                        );

  pCharacteristic->addDescriptor(new BLE2902());

  BLECharacteristic *pCharacteristic2 = pService->createCharacteristic(
                                         CHARACTERISTIC_UUID_RX,
                                         BLECharacteristic::PROPERTY_WRITE
                                       );
  pCharacteristic2->setCallbacks(new MyCallbacks());                                                                      
  
  pService->start();
  BLEAdvertising *pAdvertising = pServer->getAdvertising();
  pAdvertising->start();

  scale.begin(DOUT, CLK);
  scale.set_scale(calibration_factor);
  scale.tare(); 
  pinMode(PIN_RED,   OUTPUT);
  pinMode(PIN_GREEN, OUTPUT);
  pinMode(PIN_BLUE,  OUTPUT); 
  randomSeed(analogRead(0));  
  setColor(255,255,255);
}

void loop() {
  if(deviceConnected){       
    Serial.printf("Vals = %d\n", vals);     
    stat = getReflex();
    getStrength();
    if(stat == 1){
      Serial.printf("Current status : %d\n",stat);
      flag = 1;
      pCharacteristic->setValue("2"); 
      pCharacteristic->notify();
      vals = 0;
    }
    if(stat == 0 && flag == 1){
        Serial.println("Clear state");        
        pCharacteristic->setValue(str); 
        pCharacteristic->notify();
        flag = 0;                        
    }        
  }  
}

void setColor(int R, int G, int B) {
  analogWrite(PIN_RED,   R);
  analogWrite(PIN_GREEN, G);
  analogWrite(PIN_BLUE,  B);
}

int getReflex(){ 
  unsigned long prevTime = 0;
  unsigned long currTime = 0;
  int reading_init;
  int reading_final; 
  currTime = millis();
  prevTime = millis();
  reading_init = 0;
  reading_final = 0;
  if(vals == 18 || vals == 19){
    return 0;
  }
  Serial.printf("In getReflex val = %d\n", vals);
  int valsPrev =vals;
  while(currTime-prevTime <= 2000)
  {
    currTime = millis();
    if(scale.is_ready()) {
      reading_init = scale.get_units(1);
    }
    if(valsPrev != vals){
      prevTime = currTime;
      valsPrev = vals;
    }    
    if(vals==20 && reading_init < 15)
      setColor(0,0,0);
    else if(vals == 0 && reading_init < 15)
      setColor(255,255,255);

    if( reading_init > 15 && vals ==20 ){
      setColor(255,0,255);              
      if(reading_init > reading_final )
        reading_final = reading_init;
      else if( reading_final > reading_init ){
        setColor(255,0,255);       
        return 1; 
      }
    }
    else if(reading_init > 15 && vals == 0){
      setColor(0,255,255);
      if(reading_init > reading_final )
        reading_final = reading_init;
      else if( reading_final > reading_init ){
        setColor(0,255,255);
        return 1; 
      }
    }
  }
  return 0;  
}

void getStrength(){
  float reading;
  char valStr[8];
  Serial.printf("In getStrength val = %d\n", vals);
  if(vals == 19){
    pCharacteristic->setValue(str); 
    pCharacteristic->notify();
    setColor(255,255,255);
    vals = 0;
    return;    
  }
  if(vals != 18){
    return;
  }  
  
  while(vals == 18){
    if(scale.is_ready()) {
      reading = scale.get_units(1);
      Serial.println(reading);
    }
    if(reading<0){
      reading = 0;
    }
    setColor(0,0,0);
    dtostrf(reading, 5, 1, valStr);
    Serial.print(valStr);
    Serial.println();
    pCharacteristic->setValue(valStr); 
    pCharacteristic->notify();    
  }  
  setColor(255,255,255);
}
