# path = '../BulbulData/user_detail/'

from os import listdir
import json

# Count the track number in toptracks
for f in listdir(path):
    if '_top_tracks'in f:
        with open('{}{}'.format(path, f), 'r') as data_file:
            data = json.load(data_file)

            try:
                print('Count: ', len(data['toptracks']['track']), f)
            except Exception as e:
                print('got exception ', str(e))

# # Delete tracks from
# for f in listdir(path):
#     if '_top_tracks'in f:
#         with open('{}{}'.format(path, f), 'r') as data_file:
#             data = json.load(data_file)
#
#         try:
#             if len(data['toptracks']['track']) > 500:
#                 del data['toptracks']['track'][500:]
#         except Exception as e:
#             print('got exception', str(e))
#
#         with open('{}{}'.format(path, f), 'w') as data_file:
#             data = json.dump(data, data_file)
