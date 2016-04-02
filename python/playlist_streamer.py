from video_streamer import streamer
import glob

class playlist:
	path = ""

	def __init__(self, path_name, xip, xport):
		self.path = path_name
		self.ip = xip
		self.port = xport

	def stream(self, xpath, xport, xip):
		videos = glob.glob(xpath+"*.mp4")

		srt = streamer()

		for vid in videos:
			print vid
			srt.send(xip, xport, vid)


