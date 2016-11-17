import serial

arduino = serial.Serial('/dev/ttyACM0',9600)

while True:
	command = raw_input('Introduce un comando: ')
	arduino.write(command)
	if command == 'h':
		print('LED ON')
	elif command == 'l':
		print('LED OFF')
arduino.close()
