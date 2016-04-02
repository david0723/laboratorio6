from video_streamer import streamer
from playlist_streamer import playlist

x = streamer()

p = playlist("./user/",12345,"224.0.0.23")

while(True):
	p.stream("./user/",12345,"224.0.0.23")

# while(True):
# 	x.send("157.253.124.117",12345,"./user/tame.mp4")