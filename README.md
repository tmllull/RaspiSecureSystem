# RaspiSecureSystem
Aquest és un projecte per a l'especialitat d'IT de la carrera de _Grau en Enginyeria Informàtica_ de la FIB, que consisteix en crear un petit sistema de seguretat. Els materials que s'han fet servir han estat:
- Raspberry Pi (model 1 B)
- Dongle WiFi
- Càmera IP (Dlink DCS-932L)
- Arduino (UNO)
- Targeta de memòria SD de 32GB classe 10
- Sensor de temperatura
- Sensor de moviment

A continuació es detalles els passos que s'han seguit per tal de poder congifurar-ho tot, tant per la part de la Raspberry Pi com de la resta de components.

##Raspberry Pi
Pel que fa referència a la Raspi, s'han seguit aquests pasos i en l'ordre descrit:

1. Instal·lar el SO
Per instal·lar el SO ens hem servit de l'eina que ens proporciona la comunitat de Raspberry, anomenada NOOBS, el que ens permet instal·lar el sistema de forma guiada i d'una manera senzilla.

El primer que hem de fer és baixar NOOBS de la pàgina de Raspberry i descomprimir-lo. A continuació hem de donar format a la targeta; per això podem fer servir l'eina SDFormatter, oficial de la comunitat SD, que ens permet fer-ho també d'una forma molt senzilla.

Una vegada tenim la targeta formatejada i NOOBS descomprimit, copiem tot el contingut de la carpeta dins de la targeta. 
***COMPTE:*** NO copiar la carpeta com a únic arxiu, sino tots els arxius dins de la mateixa. 

Per fer la instal·lació ens ajudarem d'una pantalla HDMI, d'un teclat i un ratolí. Connectem tots els perifèrics a la Raspi i la connectem a la corrent. Després d'uns moments ens apareixerà una pantalla con ens demana 

2. Assignar IP estàtica

3. Canviar el port SSH (opcional)

4. Instal·lar i configurar no-ip

5. Instal·lar un servidor ftp

6. Instal·lar un servidor WEB

7. Configurar notificacions amb l'API de Pushover

##
