import serial
import os
import time

arduino=serial.Serial('/dev/ttyACM0',9600)
#arduino.open()

while True:
	time.sleep(0.5)
	x = arduino.read().rstrip()
	if x == ("d"):
		print "Movement detected"
#		os.system("/home/pi/scripts/pushNotification.sh > /dev/null") 
	arduino.flushInput()
arduino.close()
