import socket 
import sys
import select
from utils import *

args = sys.argv

Channels_to_client = {}
Client_to_channel = {}
Buffer = {}
Socket_List = []
Has_Name = {}
Name_Recorder = {}

class BasicServer(object):


	def __init__(self, port):
		self.socket = socket.socket()
		self.socket.bind(("",int(port)))
		self.socket.listen(5) 
		Socket_List.append(self.socket)
	# up to 5 

	def start(self):
		while 1:
			RtoR, RtoW, RtoE = select.select(Socket_List, [], [])
			sys.stdout.flush()
			for socket in RtoR:
				if socket == self.socket:
					(new_socket, address) = self.socket.accept()
					Socket_List.append(new_socket)
					Has_Name[new_socket] = 0
				else:
					try: 

						len_data = 0
						if socket in Buffer:
							for key in Buffer.keys():
								if socket == key:
									len_data = len(Buffer.get(socket))
									data = socket.recv(MESSAGE_LENGTH - len_data)
									if data == '':
										if in_channel(socket):
											Channels_to_client[find_channel(socket)].remove(socket)
											Socket_List.remove(socket)
											calling_broadcast_all(socket, find_channel(socket), SERVER_CLIENT_LEFT_CHANNEL.format(find_name(socket)).ljust(200, ' '))
											del Client_to_channel[socket]
											continue
									data = (Buffer.get(socket) + data)
						else:

							data = socket.recv(MESSAGE_LENGTH)
							if data =='':
								if in_channel(socket):
									Channels_to_client[find_channel(socket)].remove(socket)
									Socket_List.remove(socket)
									calling_broadcast_all(socket, find_channel(socket), SERVER_CLIENT_LEFT_CHANNEL.format(find_name(socket)).ljust(200, ' '))
									del Client_to_channel[socket]
									continue

						if (len(data) < MESSAGE_LENGTH):
							Buffer[socket] = data
							
						else:
							if socket in Buffer:
								del Buffer[socket]
							if data != '':
								data = data.rstrip()
								if Has_Name[socket] == 1:
									if data.startswith('/'):
										handle_messages(socket, data)
									else: 
										if in_channel(socket):
											broadcast_all(socket, find_channel(socket), data)
										else:
											socket.send(SERVER_CLIENT_NOT_IN_CHANNEL.ljust(200, ' '))
								else:
									Has_Name[socket] +=1
									Name_Recorder[socket] = data
							else:
								if in_channel(socket):
									Channels_to_client[find_channel(socket)].remove(socket)
									Socket_List.remove(socket)
									calling_broadcast_all(socket, find_channel(socket), SERVER_CLIENT_LEFT_CHANNEL.format(find_name(socket)).ljust(200, ' '))
									del Client_to_channel[socket]
					except:
						if in_channel(socket):
							Channels_to_client[find_channel(socket)].remove(socket)
							Socket_List.remove(socket)
							calling_broadcast_all(socket, find_channel(socket), SERVER_CLIENT_LEFT_CHANNEL.format(find_name(socket)).ljust(200, ' '))
							del Client_to_channel[socket]
						raise

def handle_messages(socket, data):
	string = data.split(' ')
	if string[0] == '/join':
		if len(string) > 1: 
			if string[1] in Channels_to_client.keys():
					in_other_channel(socket, string[1])
					Client_to_channel[socket] = string[1]
					Channels_to_client[string[1]].append(socket)
					calling_broadcast_all(socket, string[1], SERVER_CLIENT_JOINED_CHANNEL.format(find_name(socket))) 
			else:
				socket.send(SERVER_NO_CHANNEL_EXISTS.format(string[1].rstrip()).ljust(200, ' '))
		else:
			socket.send(SERVER_JOIN_REQUIRES_ARGUMENT.ljust(200, ' '))
	elif (string[0] == '/create'):
		if len(string) > 1:
			if string[1] in Channels_to_client.keys():
				socket.send(SERVER_CHANNEL_EXISTS.format(string[1].rstrip()).ljust(200, ' '))
			else:
				in_other_channel(socket, string[1])
				Channels_to_client[string[1]] = [socket]
				Client_to_channel[socket] = string[1]
		else:
			socket.send(SERVER_CREATE_REQUIRES_ARGUMENT.ljust(200, ' '))
	elif (string[0] == '/list'):
		if len(string) == 1:
			for key in Channels_to_client.keys():
				socket.send(key +"\n")
	else:
		socket.send(SERVER_INVALID_CONTROL_MESSAGE.format(data).ljust(200, ' '))

def in_other_channel(sock, channel):
	#is it in another channel
	for socket, channels in Client_to_channel.iteritems():
		if socket == sock:
			if channels != channel:
				del Client_to_channel[socket]
				Channels_to_client[channel].remove(socket)
				broadcast_all(socket, channels, (SERVER_CLIENT_LEFT_CHANNEL.format(socket)).ljust(200, ' '))

def find_name(socket):
	for sock in Name_Recorder.keys():
		if sock == socket:
			return Name_Recorder.get(sock)

def calling_broadcast_all(sock, channel, message):
	for socket in Channels_to_client[channel]:
		if socket != server.socket and socket != sock:
			try:
				socket.send(message.ljust(200, ' '))
					
			except:
					# broken socket connection
				socket.close()
					# broken socket, remove it
				if socket in Socket_List:
					Socket_List.remove(socket)


def broadcast_all(sock, channel, message):
	for socket in Channels_to_client[channel]:
		if socket != server.socket and socket != sock:
			try:
				socket.send(('[' + find_name(sock) + ']' + ' ' + message).ljust(200, ' '))
			except:

				socket.close()
				if socket in Channels_to_client[channel]:
					Channels_to_client[channel].remove(socket)
				if socket in Socket_List:
					Socket_List.remove(socket)

def in_channel(socket):
	if socket in Client_to_channel:
		return True
	return False

def find_channel(socket):
	for socket_pass, channel in Client_to_channel.iteritems():
		if socket_pass == socket:
			return channel

								

server = BasicServer(args[1])
server.start()