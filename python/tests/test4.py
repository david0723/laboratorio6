import cv2
import socket
from PIL import Image
import os
from mysocket import my_socket
import time



cap = cv2.VideoCapture("./lowq.mp4")
# while not cap.isOpened():
#     cap = cv2.VideoCapture("./lowq.mp4")
#     cv2.waitKey(1000)
#     print "Wait for the header"

count = 0
ip = "192.168.2.106"
port = 12345


#Cap lee el frame actual
#flag es true si cap pudo leer el frame
#frame tiene el frame, si cap no pudo leerlo frame = null
flag, frame = cap.read();
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)


#mientras que se puedan seguir leyendo frames
while flag:
    
    print "reading frame %d" %count
    #guarda el frame como una imagen PNG
    # cv2.imwrite("frame%d.jpeg" % count, frame, [cv2.IMWRITE_JPEG_QUALITY, 40])
    #carga la imagen en la variable img

    ret, png = cv2.imencode('.jpeg', frame, [cv2.IMWRITE_JPEG_QUALITY, 40])
    b = png.tobytes()

    # chunks = zip(*[iter(b)]*6)


    sock.sendto(b, (ip, port))
    time.sleep(0.03)

    #  for chunk in chunks:
        
    #     sock.sendto(chunk, (ip, port))


    # with open("frame%d.png" % count, "rb") as imageFile:
    #     f = imageFile.read()
    #     b = bytearray(f)

    

    #img = open("frame%d.png" % count,"r")

    #img = Image.open("frame%d.png" % count)

    #imgString = img.read(1024)

    #imgString = img.toString()

    #imgBytes = img.getBytes()

    

    count +=1;

    flag, frame = cap.read();


    if cv2.waitKey(10) == 27:
        print 'cv2.wairKey(10)==27'
        break



