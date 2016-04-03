import glob


path = "./users/user/*.mp4"

videos = glob.glob(path)

playlist=""

for vid in videos:
	playlist = playlist + vid + ":"

print playlist
