#include "DHT.h"
int pin=2;
float temp;
long dist;
long time;
DHT dht(pin,DHT11);
int cont=1000;
boolean checked=false;

// Setup de l'Arduino
void setup(){
  Serial.begin(9600);
  pinMode(9, OUTPUT); /*activación del pin 9 como salida: para el pulso ultrasónico*/
  pinMode(8, INPUT); /*activación del pin 8 como entrada: tiempo del rebote del ultrasonido*/
  pinMode(13,OUTPUT);
  dht.begin();
}

// Llums
void light(){
  char option;
  if(Serial.available() >0){
    option = Serial.read();
    if(option== 'h'){
      digitalWrite(13,HIGH);
      //Serial.println("ON");
    }
    else if(option=='l'){ 
      digitalWrite(13,LOW);
      //Serial.println("OFF");
    }
  }
}

// Moviment
void movement() {
  digitalWrite(9,LOW); /* Por cuestión de estabilización del sensor*/
  delayMicroseconds(10);
  digitalWrite(9, HIGH); 
  delayMicroseconds(10);
  time=pulseIn(8, HIGH); 
  dist= int(0.017*time); 
  if (dist < 50 and cont == 1000 and dist !=0){
    Serial.println("d");
    checked=true;
  }
}

// Temperatura
void temperature() {
  if((temp -4.0) >= 30.0 and cont == 1000){
    Serial.println("t");
    checked=true;
  }
}

// Loop principal (main)
void loop() {
  light();
  movement();
  temperature();
  if(checked) --cont;
  if(cont==0){
    cont=1000;
    checked=false;
  }
  delay(10);
}
