import serial
import os
import time

arduino=serial.Serial('/dev/ttyACM0',9600)
arduino.flushInupt()
arduino.flushOutput()

while True:
	x = arduino.read(2).rstrip('\n')
	arduino.flushInupt()
	arduino.flushOutput()
	if x == ("d"):
		os.system("/home/pi/scripts/pushNotification.sh > /dev/null")
	elif x.isdigit():
		file = open("home/pi/temp.txt","a+")
		file.write(x + '\n')
		file.close()
	arduino.flushInput()
	arduino.flushOutput()
arduino.close()
