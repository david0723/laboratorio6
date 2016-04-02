import cv2
import socket
from PIL import Image
import os
from mysocket import my_socket 
import time



cap = cv2.VideoCapture("./vid.mp4")
while not cap.isOpened():
    cap = cv2.VideoCapture("./vid.mp4")
    cv2.waitKey(1000)
    print "Wait for the header"

count = 0
ip = "192.168.2.106"
port = 6789


#Cap lee el frame actual
#flag es true si cap pudo leer el frame
#frame tiene el frame, si cap no pudo leerlo frame = null
flag, frame = cap.read();


#mientras que se puedan seguir leyendo frames
while flag:
    
    print "reading frame %d" %count
    #guarda el frame como una imagen PNG
    cv2.imwrite("frame%d.jpeg" % count, frame, [cv2.IMWRITE_JPEG_QUALITY, 40])
    #carga la imagen en la variable img

    buf = 9000

    ret, png = cv2.imencode('.jpeg', frame)
    b = png.tobytes()

    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    f = open ("frame%d.jpeg" % count, "rb") 
    data = f.read(buf)
    i=0
    while (data):
        if(sock.sendto(data, (ip, port))):
            print "sending ... %d" % i
            data = f.read(buf)
            time.sleep(0.1)
            i+=1

    sock.sendto("fin", (ip, port))


    count +=1;

    # flag, frame = cap.read();
    flag = False


    if cv2.waitKey(10) == 27:
        print 'cv2.wairKey(10)==27'
        break


