import socket
import sys
import select
from utils import *

args = sys.argv

class Client(object):
	def __init__(self, name, address, port):
		self.name = name
		self.address = address
		self.port = int(port)
		self.socket = socket.socket()

	def start(self):
		try:	
			self.socket.connect((self.address, self.port))
			#send the name
		except:
			sys.stdout.write(CLIENT_CANNOT_CONNECT.format(self.address, self.port) + '\n')
			sys.exit()

		self.socket.send(self.name.ljust(200,' '))
		sys.stdout.write(CLIENT_MESSAGE_PREFIX); sys.stdout.flush()
		socket_list = [sys.stdin, self.socket]
		#first me I send
		while 1:
			RtoR, RtoW, RtoE = select.select(socket_list, [], [])

			for sock in RtoR:

				if sock == self.socket:
					#not 200 recieve again
					data = (sock.recv(MESSAGE_LENGTH))
					while len(data) < MESSAGE_LENGTH:
						data += sock.recv(MESSAGE_LENGTH - len(data))
					if not data:
						sys.stdout.write(CLIENT_WIPE_ME + '\r')
						sys.stdout.write(CLIENT_SERVER_DISCONNECTED.format(self.address, self.port) + '\n')
						sys.exit()

					else:
						data = data.rstrip()

						#to me
						sys.stdout.write(CLIENT_WIPE_ME + '\r')
						sys.stdout.write(data.rstrip() + '\n')
					
						sys.stdout.write(CLIENT_MESSAGE_PREFIX); sys.stdout.flush()
				else :
					#to server

					msg = sys.stdin.readline()
					msg = msg.strip('\n')
					self.socket.send(msg.ljust(200,' '))
					sys.stdout.write(CLIENT_MESSAGE_PREFIX); sys.stdout.flush()


client = Client(args[1], args[2], args[3])
client.start()


