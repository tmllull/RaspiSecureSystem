# RaspiSecureSystem
Aquest és un projecte per a l'especialitat d'IT de la carrera de _Grau en Enginyeria Informàtica_ de la FIB, que consisteix en crear un petit sistema de seguretat utilitzant una Raspberry Pi (a partir d'ara, Raspi), una càmera IP, un Arduino i varis sensors.

Aquest sistema ens permetrà veure què passa a la nostra casa o habitació des d'una pàgina web, i gràcies a un sensor de movient podrem fer que la càmera faci una foto i ens l'envii per correu o ens avisi amb una notificació al mòbil.

De la mateixa manera farem servir un sensor de temperatura que ens podrà servir com a "detector d'incendis", i podrem fer també que si passa d'una certa temperatura ens avisi d'alguna manera. Afegirem també l'opció de poder encendre o apagar alguns llums remotament (via web), per si arribem tard a casa poder "fer veure" que hi ha algú.

Tant l'accés a la càmera com controlar els llums també es podrà fer des d'una aplicació mòbil.

Els materials que s'han fet servir han estat:

- Raspberry Pi (model 3 B)
- Càmera IP (Dlink DCS-932L)
- Arduino (UNO)
- Targeta de memòria SD de 32GB classe 10
- Sensor de temperatura
- Sensor de moviment

A continuació es detalles els passos que s'han seguit per tal de poder congifurar-ho tot.

## Raspberry Pi
Pel que fa referència a la Raspi, s'han seguit aquests pasos:

1. Instal·lar i preparar el SO
2. Assignar una IP estàtica
3. Canviar el port SSH (Opcional)
4. Instal·lar i configurar no-ip
5. -
6. -
7. -
8. -

### 1. Instal·lar i preparar el SO

El SO escollit per aquest projecte ha estat ***Raspbian*** versió *"Pixel"*, una distribució Linux basada en Debian-Jessie i adaptada pel chip ARM de la Raspi. Per instal·lar el SO ens hem servit de l'eina que ens proporciona la comunitat de Raspberry, anomenada *NOOBS*, el que ens permet instal·lar el sistema d'una forma senzilla.


El primer que hem de fer és baixar [NOOBS](https://downloads.raspberrypi.org/NOOBS_latest) de la pàgina de Raspberry Pi i descomprimir-lo. A continuació hem de donar format a la targeta; per això podem fer servir l'eina [SDFormatter](https://www.sdcard.org/downloads/formatter_4/), oficial de SD Association.

Una vegada tenim la targeta formatejada i NOOBS descomprimit, copiem tot el contingut de la carpeta NOOBS dins de la targeta.

***COMPTE:*** NO copiar la carpeta com a únic arxiu, sino tots els arxius que trobem dins de la mateixa. 

Per fer la instal·lació ens ajudarem d'una pantalla amb entrada HDMI, un teclat i un ratolí. Connectem tots els perifèrics a la Raspi i la connectem a la corrent. Després d'uns moments ens apareixerà una pantalla on ens demana quin sistema volem instal·lar. Seleccionem *Raspbian* i cliquem "Install". Aquest procés pot tardar entre 20 i 30 minuts.

Quan la instal·lació hagi acabat, reiniciem el sistema i ja podrem arrencar Raspbian normalment. De moment encara mantenim la pantalla, teclat i ratolí, ja que ens queda fer algunes configuracions.

Des del menú d'aplicacions anirem a Preferences --> Mouse and Keyboard Settings, i posarem el teclat en Espanyol (Català).

A continuació anirem a Preferences --> Raspberry Pi Configuration, modificarem el **password**, i marcarem l'opció de **Boot** que diu *To CLI*. Això farà que quan arrenqui la Raspi no carregui l'entorn gràfic, ja que normalment no el farem servir i així tenim més recursos disponibles.

En cas que volguem accedir a l'entorn gràfic (si tenim la Raspi conectada a una tele o pantalla, per exemple), ho podrem fer amb la comanda *startx*

	startx

###2. Assignar IP estàtica

Una part important i que dóna sentit a una Raspi és el fet de poder accedir a ella des de qualsevol lloc a través d'un terminal. Això ho podem fer sempre i quan sapiguem la seva adreça IP però, com sabem, aquesta pot canviar si el router es reinicia o si reiniciem la Raspi. Per evitar-ho li assignarem una IP estàtica i així ens asegurarem que sempre té la mateixa.

Per fer això tenim varies opcions:

####Opció 1:
Abans de res ens connectem a la nostra WiFi, d'aquesta manera ja tindrem regisrat el SSID i Password, i a continuació obrim un terminal amb la combinació de tecles **Ctrl+Alt+t**. Podem fer-ho també des del menú d'aplicacions.

A continuació hem de modificar l'arxiu *dhcpcd.conf*
	
	sudo nano /etc/dhcpcd.conf

Afegint al final el següent codi si volem fer la connexió per cable:

	interface eth0

	static ip_address=192.168.1.XX/24
	static routers=192.168.1.1
	static domain_name_servers=192.168.1.1
	
O aquest si la volem fer per WiFi:

	interface wlan0

	static ip_address=192.168.1.XX/24
	static routers=192.168.1.1
	static domain_name_servers=192.168.1.1

Sortim amb *Ctrl+x*, acceptem els canvis amb *y*, i premem *enter*.

**NOTA:** Substituim el valor XX per l'adreça que vulguem, tenint en compte de posar un valor que estigui fora del rang DHCP. Normalment es comencen a donar adreces a partir del número 33 (tot i que pot variar), però dificilment comença per adreces baixes. Això ens permet assignar sense cap problema adreces a partir de la 2. Igualment, hem de posar l'adreça del router i DNS que correspongui amb el nostre router. Normalment sol ser 192.168.1.1 en els routers domèstics, però assegureu-vos abans per si de cas.

####Opció 2:

L'altre opció és modificar directament l'arxiu interfaces

	sudo nano /etc/network/interfaces

I modifiquem les dades de wlan0 per les següents si farem servir el WiFi:

	auto wlan0
    allow-hotplug wlan0
    iface wlan0 inet static
    address 192.168.1.XX
    netmask 255.255.255.0
    gateway 192.168.1.1
    wpa-passphrase wifi-password
    wpa-ssid my-ssid

Canviant *wifi-password* pel password de la nostra xarxa, *my-ssid* pel nom de la nostra xarxa, i modificant el valor XX per l'adreça que volem.

O les dades de eth0 si ens connectarem per cable:

	auto eth0
	iface eth0 inet static
    address 192.168.1.XX
    netmask 255.255.255.0
    gateway 192.168.1.1

Sortim amb *Ctrl+x*, acceptem els canvis amb *y*, i premem *enter*.

####Opció 3:

Ens queda encara una altra opció, que és fer servir l'arxiu *wpa_supplicant.conf*

	sudo nano /etc/wpa_supplicant/wpa_supplicant.conf

Aquest arxiu és l'encarregat de guardar els noms i contrassenyes de les xarxes WiFi, i per tant haurem d'afegir al final la nostra xarxa si no ens hem connectat amb anterioritat a la xarxa WiFi. Si ja tenim una xarxa amb el nom i password correctes, no cal fer res:

	network={
    ssid="WiFi_name"
    psk="Wifi_password"
	}

Sortim amb *Ctrl+x*, acceptem els canvis amb *y*, i premem *enter*.

Ara anem a modificar l'arxiu interfaces

	sudo nano /etc/network/interfaces

I canviem la part de wlan0 pel següent si farem servir la connexió WiFi:

	allow-hotplug wlan0
	iface wlan0 inet static
	address 192.168.1.XX
	netmask 255.255.255.0
	gateway 192.168.1.1
	wpa-conf /etc/wpa_supplicant/wpa_supplicant.conf

O la part de eth0 si ens connectarem per cable:

	auto eth0
	iface eth0 inet static
    address 192.168.1.XX
    netmask 255.255.255.0
    gateway 192.168.1.1

Com veiem, els canvis d'aquest arxiu son molt semblants a l'opció 2, però aquí no posem explícitament el nom i password de la nostra xarxa, sino que ens servim de l'arxiu wpa_supplicant.conf. Pensar a canviar el valor XX de l'adreça IP.

Després reiniciarem la Raspi:

	sudo reboot now

I una vegada hagi tornat a arrencar, comprovarem que ens ha assignat l'adreça que li hem dit amb ifconfig:

	ifcongif

En cas que no ho hagi fet, revisem els arxius que hem modificat per si ens hem errat en alguna cosa.

En aquest moment ja ens podem desfer de la pantalla, teclat i ratolí, ja que totes les comunicacions les farem per ssh. La manera d'accedir a la Raspi és, des d'un terminal de Linux/Mac teclejar el següent:

	ssh pi@192.168.1.XX

Ens demanarà la contrassenya que hem modificat al punt 1 (*Mentre s'escriu la contrassenya no es veurà res per pantalla.*) i ja estarem connectats a la Raspi.

**Nota:** Si fem servir Windows, hi podrem accedir mitjançant l'aplicació PuTTY.

###3. Canviar el port SSH (Opcional)

Addicionalment podriem voler emprar un port SSH diferent de l'standart (per defecte és el 22). Això ens pot ser útil si volem accedir a la Raspi des de fora de casa i el port 22 ja està utilitzat, o per intentar tenir una mica més de seguretat evitant valors per defecte.

Per fer-ho modifiquem l'arxiu *sshd_config*

	sudo nano /etc/ssh/sshd_config

Busquem la linia on indica el port i el canviem pel que ens interessi, ja sigui modificant la linia en qüestió o comentant aquesta i afegint una nova

	#port 22
	port 2234

Per comoditat, lo més pràctic és afegir 2 valors més al 22 inicial, així ens serà més fàcil d'associar el port a SSH, per exemple 2234, però podem posar el que vulguem (sempre i quan no estigui ja utilitzat)

Finalment reiniciem el servidor ssh

	sudo service ssh restart

Una vegada fet el canvi, la manera d'accedir a la Raspi per terminal serà

	ssh -p 2234 pi@192.168.1.XX

Sent XX l'adreça que li haurem donat a la Raspi en el punt 2.


###4. Instal·lar i configurar no-ip

De la mateixa manera que les direccions IP locals poden canviar, també ho fan les direccions IP públiques, pel que potser que ara en tiguem una i demà una altra. Això és un problems si intentem accedir a la Raspi des de fora de la xarxa local, ja que no sabrem quina direcció tenim assignada. Per evitar aquest problema ens ajudarem del servei no-ip.

No-ip és un servidor DNS que el que fa és traduir una direcció web (http://www.google.com) a la seva IP pública (http://74.125.224.72, per exemple). Així, el que farem ara serà accedir a noip.com, crear un compte (gratuit) i registrar un domini, per exemple *elmeudomini.ddns.net*.

Quan ja tenim el nostre domini registrat, toca instal·lar el client a la Raspi. 

Hi accedim per ssh amb:

	ssh pi@192.168.1.XX

Abans ens assegurarem de tenir el sistema actualitzat, pel que farem un update i un upgrade. Això pot tardar fins a 20 o 30 minuts, depenent del que s'hagi d'actualitzar.

	sudo apt-get update && sudo apt-get upgrade

És recomanable fer aquest procés cada cert temps, ja que hi pot haver actualitzacions de seguretat del sistema o d'alguna altra aplicació.

A continuació descarreguem el client de no-ip

	wget http://www.no-ip.com/client/linux/noip-duc-linux.tar.gz

El descomprimim

	tar -zxvf noip-duc-linux.tar.gz

Accedim a la carpeta que s'ha creat

	cd noip-2.1.9-1

I l'instal·lem

	sudo make
	sudo make install

En aquest punt ens demanarà el nom d'usuari i la contrassenya del nostre compte de no-ip, i degut a que només tindrem un domini registrat, agafarà aquest per defecte. El temps de refresc el podem deixar per defecte, i a la següent pregunta, respondrem que NO (n).

Ara creem un nou fitxer que li direm noip2

	sudo nano /etc/init.d/noip2

I afegim la següent comanda

	sudo /usr/local/bin/noip2

Guardem el fitxer amb Ctrl+x --> y --> enter, i li donem permissos d'execució

	sudo chmod +x /etc/init.d/noip2

Actualitzem el fitxer d'inici perquè arrenqui cada vegada que engeguem la Raspi

	sudo update-rc.d noip2 defaults

I posem el servei en marxa

	sudo /usr/local/bin/noip2

Ens falta un darrer punt molt important, i que justifica la importància del punt 2. Per poder accedir des de fora de la xarxa, necessitem saber l'adreça pública (problema que hem solucionat amb no-ip), però també necessitem saber a quina adreça privada volem anar. Això se soluciona fent "port-forwarding" al router i indicant que tot el que vingui des de fora que vulgui anar al port 22 (o el que haguem configurat si hem fet el punt 2), vagi a la nostra Raspi. Aquí veiem la importància de tenir una IP estàtica.

Per aconseguir això necessitem accedir al router teclejant la seva adreça a qualsevol navegador, que normalment sol ser 192.168.1.1, i posem el nom d'usuari i contrassenya. Els nous routers de fibra òptica de Movistar només demanen una contrassenya que es troba abaix del router, i els més antics normalment tenen "admin" com a usuari, i "admin" o "1234" com a contrassenya (sense cometes). En cas que no vagi bé, s'haurien de cercar les credencials d'accés a Internet.

Una vegada dins cerquem alguna opció que es digui "Ports" (depèn molt de cada fabricant), i creem una nova regla que indiqui el següent:

	Port: 22 (o el que correspongui)
	Tipus de protocol: TCP/UDP
	Adreça destinació: 192.168.1.XX

Aquesta és la informació rellevant, sent XX l'adreça que hem assignat en el punt 2. Potser ens demana també un nom, que li podem posar SSH, per exemple.

Amb això ja tenim configurat no-ip i el port 22 del router obert, pel que hauriem de poder accedir a la nostra raspi des de qualsevol lloc fora de la nostra xarxa local. No ho podrem provar sempre que estiguiem connectats a la pròpia xarxa, però ho podem provar des del mòbil (amb una aplicació com JuiceSSH per Android) o demanant a algú que estigui en una altra xarxa que ens ho miri.

Des del terminal (o PuTTY si estem utilitzant Windows) teclegem

	ssh pi@elmeudomini.ddns.net
O si hem canviat el port ssh

	ssh -p 2234 pi@elmeudomini.ddns.net

Si ens demana contrassenya, vol dir que tot ha sortit bé, i ja podrem accedir a la nostra Raspi des de qualsevol banda (inclús des del mòbil).

###5. Obrir els ports del router

En el punt anterior hem vist com obrir un port del nostre router, i aquí aprofitarem per obrir tots els que ens faci falta pels propers serveis. Així, una vegada dins del router, i com que ja sabem quins tipus de serveis farem servir, aprofitarem per obrir-los tots de cop i ens estalviem haver de fer-ho després un per un.

En el nostre cas, a banda del port 22, obrirem els següents apuntant també a l'adreça de la nostra Raspi:

	Ports: 80 (servidor web per defecte), 21 (servidor FTP per defecte)
	Protocols: TCP/UDP
	IP destí: 192.168.1.XX

A més a més, ja que també farem servir una càmera IP, podem obrir el port que li correspondrà:

	Port: 8081
	Protocols: TCP/UDP
	IP destí: 192.168.1.XY

Canviarem XY per l'adreça que posteriorment li donarem a la nostra càmera, i així ja no haurem de tornar a configurar el router una vegada instal·lada.

###6. Instal·lar un servidor ftp

###7. Instal·lar un servidor WEB

###8. Configurar notificacions amb l'API de Pushover
