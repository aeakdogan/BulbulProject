from os import listdir
import json

path = '/root/Bulbul/BulbulData/user_detail/'

usernames = []
for item in listdir(path):
    if '_loved_tracks' in item:
        user = item.split('_loved_tracks')[0]
        usernames.append(user)

i = 0
average_intersect = 0
for user in usernames:
    top = '{}_top_tracks'.format(user)
    loved = '{}_loved_tracks'.format(user)
    print('Top: ', top, ' Loved: ', loved)

    f = open(path + top)
    data_top = json.loads(f.read())
    f.close()
    f = open(path + loved)
    data_loved = json.loads(f.read())
    f.close()

    top_track_ids = []
    loved_track_ids = []
    top_playcounts = {}
    for count in range(len(data_top['toptracks']['track'])):
        if data_top['toptracks']['track'][count]['mbid']:
            top_track_ids.append(data_top['toptracks']['track'][count]['mbid'])
            top_playcounts[data_top['toptracks']['track'][count]['mbid']] = data_top['toptracks']['track'][count]['playcount']
    for count in range(len(data_loved['lovedtracks']['track'])):
        if data_loved['lovedtracks']['track'][count]['mbid']:
            loved_track_ids.append(data_loved['lovedtracks']['track'][count]['mbid'])

    intersection = sorted(list(set(top_track_ids).intersection(set(loved_track_ids))), reverse=True)

    # for it in intersection:
    #     print('Count: {}'.format(top_playcounts[it]))

    # print('Play Counts: ', sorted(top_playcounts.values(), reverse=True))

    # print('Loved Tracks: ', loved_track_ids)
    # print('Top tracks: ', top_track_ids)

    average_intersect += len(intersection)
    print('-----------------------------------')

    # if i == 1000:
    #     break
    # i += 1
print('Average intersection elements: ', average_intersect / len(usernames), average_intersect, len(usernames))
