import serial

arduino = serial.Serial('/dev/ttyACM0',9600)
arduino.flushInput()
arduino.write('1')
arduino.close()
