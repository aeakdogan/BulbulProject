###################
#   Picks the recenttrack files from user information folder
#   Important: You should check the output file paths and update them
###################


import os
from shutil import copyfile


input_file = 'friends'
files = os.listdir(input_file)
files = [x for x in files if 'recent_tracks' in x]

for file in files:
    src = 'friends/{}'.format(file)
    dst = 'recenttracks/{}'.format(file)
    copyfile(src, dst)
