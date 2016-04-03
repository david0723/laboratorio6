from video_streamer import streamer
import glob

class playlist:
	path = ""

	def stream(self, xpath, xport, xip):
		while(True):
			# print "Streaming: " , xpath , 
			videos = glob.glob(xpath+"*.mp4")
			group = "224.0.0."+xip

			# print "ON group: " , group
			# print "ON channel: " , xport

			
			# print "new streamer"

			for vid in videos:
				srt = streamer()
				print vid
				srt.send(group, xport, vid)


