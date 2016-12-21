import serial

arduino = serial.Serial('/dev/ttyACM0',9600)
arduino.flushInput()
arduino.write('0')
arduino.close()
