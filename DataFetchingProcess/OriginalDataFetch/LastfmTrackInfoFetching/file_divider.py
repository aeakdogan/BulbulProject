import pickle

with open('toBeDownloaded', 'rb') as f:
    mbid_list = pickle.load(f)

# content = open('outputs/unique_tracks.txt').read()
# content_list = content.split('\r\n')
content_list = mbid_list

part_size = int(len(content_list) / 6)
split_no = 6

for i in range(split_no):
    with open('outputs/extra/unique_tracks_{}.txt'.format(str(i + 1)), 'w') as f:
        for j in range(i * part_size, (i+1) * part_size):
            f.write('{}\r\n'.format(content_list[j]))





