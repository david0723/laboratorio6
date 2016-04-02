import cv2
import socket
from PIL import Image
import os
import time
import struct


class streamer:
	sock = None
	def __init__(self):
		sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

	def send(self, ip, port, video):
		cap = cv2.VideoCapture(video)
		count = 0
		flag, frame = cap.read();


		while flag:
		    print "reading frame %d" %count
		    ret, png = cv2.imencode('.jpeg', frame, [cv2.IMWRITE_JPEG_QUALITY, 10])
		    b = png.tobytes()
		    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

		    ttl = struct.pack('b', 32)
		    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, ttl)

		    sock.sendto(b, (ip, port))
		    time.sleep(0.03)
		    count +=1;
		    flag, frame = cap.read();
