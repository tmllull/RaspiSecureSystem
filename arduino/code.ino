#include "DHT.h"
int temp; //temperatura
long distancia; //distancia del objeto al sensor
long tiempo; // tiempo de ultrasonidos
int pin=2; //pin de temperatura
DHT dht(pin,DHT11); //sensor tipo DHT11
int cont=28; //cada 10 segundos aprox cambia
int cont_temp = 122; // contador de temperatura. cada 1 min cambia 
boolean trobat=false; //cada 10 segundos cambia movement sensor
boolean trobat_temp = false; // cada 1 min cambia temperature sensor
int relay = 12; //pin del relé
char val = '0'; // 1=encender luz, 0= apagar luz
int led = 5; //pin del led
void encender_luz(){
  if(Serial.available()>0){
    val = Serial.read();
    if(val == '1'){
      digitalWrite(relay,LOW); //ENCENDER
    }
    else{
      digitalWrite(relay,HIGH); //APAGAR
    }
  }
}

void movement(){
  digitalWrite(9,LOW); /* Por cuestión de estabilización del sensor*/
  delayMicroseconds(10);
  digitalWrite(9, HIGH);
  delayMicroseconds(10);
  tiempo=pulseIn(8, HIGH);
  distancia= int(0.017*tiempo);
  if (distancia < 20 and cont == 28 and distancia != 0){
   digitalWrite(led,HIGH);
   Serial.println("dd"); //enviamos una d por el puerto serie
   Serial.flush();
    trobat=true;
  }
  if(cont == 14) digitalWrite(led,LOW);
  if(trobat) --cont;
  if(cont==0) { 
    cont=28; 
    trobat=false;
    
    }
}

void temperature(){
  temp = dht.readTemperature();
  if(!isnan(temp)){
    if (cont_temp == 122){
    Serial.println(temp - 3); // enviamos la temperatura por el puerto serie
    Serial.flush();
    trobat_temp = true;
    }
  }
  if(trobat_temp) --cont_temp;
  if(cont_temp == 0) { 
    cont_temp = 122; 
    trobat_temp = false;
  }
}

void setup(){
  Serial.begin(9600);
  pinMode(9, OUTPUT); /*activación del pin 9 como salida: para el pulso ultrasónico*/
  pinMode(8, INPUT); /*activación del pin 8 como entrada: tiempo del rebote del ultrasonido*/
  pinMode(relay,OUTPUT); // pin 12 de salida para la luz
  digitalWrite(relay,HIGH); //DEFAULT APAGADO
  dht.begin(); //init del sensor de temperatura
}


void loop() {
  encender_luz();
  movement();
  temperature();
  delay(100); 
}
