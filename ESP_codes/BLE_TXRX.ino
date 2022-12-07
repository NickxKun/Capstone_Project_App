#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>
#include <BLE2902.h>
// See the following for generating UUIDs:
// https://www.uuidgenerator.net/

BLECharacteristic *pCharacteristic;
bool deviceConnected = false;
float val;
uint8_t* received_data;
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
      received_data = pCharacteristic->getData();
    
      if (RXvalue.length() > 0) {
        Serial.println("*********");
        Serial.print("Recieved value: ");
        Serial.println(*received_data);
        //for (int i = 0; i < RXvalue.length(); i++)
          //Serial.print(RXvalue[i]);
      }
      Serial.println();
      Serial.println("*********");                    
    }
}; 

void setup() 
{
  Serial.begin(9600);
     
  BLEDevice::init("MyESP32New");
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
}

void loop() {
  if(deviceConnected){
    val = random(-10,20);
    char valStr[8];
    dtostrf(val, 1, 2, valStr);
    pCharacteristic->setValue(valStr); 
    pCharacteristic->notify();
    Serial.print("Sent Value: ");
    Serial.println(valStr);    
    delay(500);
  }  
  
}
