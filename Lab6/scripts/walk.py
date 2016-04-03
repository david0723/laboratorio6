# Import the os module, for the os.walk function
import os
import thread
from playlist_streamer import playlist
import time
import threading


# def launch(path, channel, group):
# 	print "---------------------------------"
# 	p = playlist()
# 	p.stream(path,channel,group)
 
class myThread (threading.Thread):
	def __init__(self, dir, chan, group):
		threading.Thread.__init__(self)
		self.dir = dir
		self.chan = chan
		self.group = group
	def run(self):
		p = playlist()
		p.stream(self.dir,self.chan,self.group)

x = 1
channel = 12345
print "walking"
try:
	rootDir = './users'

	for dirName, subdirList, fileList in os.walk(rootDir):
		print "x: " , x
		time.sleep(1)
		if x != 1:
			print('Found directory: %s' % dirName)
			dir = dirName + "/"

			print "channel: ", channel
			print "group:   ", x
			t = myThread(dir,channel,str(x))
			t.start()
			# thread.start_new_thread( launch, (dirName+"/",channel,str(x)) )

		x+=1
		channel+=1
except:
   print "Error: unable to start thread"
