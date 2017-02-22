import urllib.request
import json, requests
import mmap
from queue import *
import os
from pprint import pprint
import time

'''
Application name        Bulbul
API key 7d67bf49e1c3743c1e2ce02eefd530ee
Shared secret   8e5c796de2ffd2b343e86aa492284a79
Registered to   mesutgurlek
'''
cnt = 0

# DEPRICATED
def collectDataRecursively(username, file):

    friends_url = "http://ws.audioscrobbler.com/2.0?method=user.getfriends&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json".format(username)
    recent_tracks_url = "http://ws.audioscrobbler.com/2.0?method=user.getrecenttracks&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json&limit=200".format(username)
    top_artists_url = "http://ws.audioscrobbler.com/2.0?method=user.gettopartists&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json".format(username)
    top_tracks_url = "http://ws.audioscrobbler.com/2.0?method=user.gettoptracks&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json&period=overall".format(username)
    top_albums_url = "http://ws.audioscrobbler.com/2.0?method=user.gettopalbums&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json".format(username)

    resp = requests.get(url=friends_url)
    data = json.loads(resp.text)
    time.sleep(1)
    # save the response.
    with open('friends/' + username + '_friends_resp', 'w+') as resp_file:
        resp_file.write(resp.text)

    recent_tracks = requests.get(url=recent_tracks_url)
    time.sleep(1)
    # save the recent tracks of user
    with open('friends/' + username + '_recent_tracks', 'w+') as resp_file:
        resp_file.write(recent_tracks.text)

    top_artists = requests.get(url=top_artists_url)
    time.sleep(1)
    # save the top artists of user
    with open('friends/' + username + '_top_artists', 'w+') as resp_file:
        resp_file.write(top_artists.text)

    top_tracks = requests.get(url=top_tracks_url)
    time.sleep(1)
    # save the top tracks of user
    with open('friends/' + username + '_top_tracks', 'w+') as resp_file:
        resp_file.write(top_tracks.text)

    top_albums = requests.get(url=top_albums_url)
    time.sleep(1)
    # save the top tracks of user
    with open('friends/' + username + '_top_albums', 'w+') as resp_file:
        resp_file.write(top_albums.text)

    # update the user base
    new_unames = []
    if 'friends' in data and 'user' in data['friends']:
        for user in data['friends']['user']:
            if 'name' in user:
                user['name']
                if write_uname(user['name'], file):
                    new_unames.append(user['name'])

        # recursive call for each new user
        for new_guy in new_unames:
            global cnt
            cnt += 1
            with open('counter.txt', 'w+') as counter_file:
                counter_file.write(str(cnt))

            print(cnt)
            collectDataRecursively(new_guy, file)
    else:
        print("No friends, I am Lonely, call me...")


# returns if the name was actually written or not.
def write_uname(uname, uname_file):
    write = True
    with open(uname_file, 'r') as s:
        if uname in s.read():
            write = False

    if write:
        with open(uname_file, 'a') as unames:
            unames.write(uname)
            unames.write('\n')

    return write

def increase_by_un(init=False, known_file=''):
    global cnt

    if init:
        prev_cnt=0
        with open(known_file, 'r') as knowns:
            for line in knowns:
                prev_cnt += 1

        cnt = prev_cnt

    else:
        cnt += 1
        with open('counter.txt', 'w+') as counter_file:
            counter_file.write(str(cnt))

        print(cnt)

# a recursive implementation is not logical therefore we choose an iterative implementation.
def collectData(username, file, done_file):

    unames = Queue(maxsize=20000000)
    unames.put(username)

    while not unames.empty():

        username = unames.get()

        if write_uname(username, done_file):
            friends_url = "http://ws.audioscrobbler.com/2.0?method=user.getfriends&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json".format(username)
            recent_tracks_url = "http://ws.audioscrobbler.com/2.0?method=user.getrecenttracks&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json&limit=200".format(username)
            top_artists_url = "http://ws.audioscrobbler.com/2.0?method=user.gettopartists&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json".format(username)
            top_tracks_url = "http://ws.audioscrobbler.com/2.0?method=user.gettoptracks&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json&period=overall".format(username)
            top_albums_url = "http://ws.audioscrobbler.com/2.0?method=user.gettopalbums&user={}&api_key=7d67bf49e1c3743c1e2ce02eefd530ee&format=json".format(username)

            resp = requests.get(url=friends_url)
            data = json.loads(resp.text)
            time.sleep(1)
            # save the response.
            with open('friends/' + username + '_friends_resp', 'w+') as resp_file:
                resp_file.write(resp.text)

            recent_tracks = requests.get(url=recent_tracks_url)
            time.sleep(1)
            # save the recent tracks of user
            with open('friends/' + username + '_recent_tracks', 'w+') as resp_file:
                resp_file.write(recent_tracks.text)

            top_artists = requests.get(url=top_artists_url)
            time.sleep(1)
            # save the top artists of user
            with open('friends/' + username + '_top_artists', 'w+') as resp_file:
                resp_file.write(top_artists.text)

            top_tracks = requests.get(url=top_tracks_url)
            time.sleep(1)
            # save the top tracks of user
            with open('friends/' + username + '_top_tracks', 'w+') as resp_file:
                resp_file.write(top_tracks.text)

            top_albums = requests.get(url=top_albums_url)
            time.sleep(1)
            # save the top tracks of user
            with open('friends/' + username + '_top_albums', 'w+') as resp_file:
                resp_file.write(top_albums.text)

            # now that the user information is taken record that username and increase our counter.
            write_uname(username, done_file)
            increase_by_un()

        #data exists just take it.
        else:
            # get the response.
            with open('friends/' + username + '_friends_resp', 'r') as resp_file:
                data = json.loads(resp_file.read())

        # update the user base
        new_unames = []
        if 'friends' in data and 'user' in data['friends']:
            for user in data['friends']['user']:
                if 'name' in user:
                    new_unames.append(user['name'])

            # do the previous steps for each new user
            for new_guy in new_unames:
                unames.put(new_guy)
        else:
            print("No friends, I am lonely, call me...")




uname_file = 'all_known_user_names'
done_uname_file = 'all_info_received_unames'
seed = 'brandnewfan123'

increase_by_un(init=True, known_file=done_uname_file)
collectData(seed, uname_file, done_uname_file)








'''
print(data['friends']['user'])

for user in data['friends']['user']:
    if 'name' in user and 'realname' in user:
        print('name: ', user['name'], ' realname: ', user['realname'])
    elif 'name' in user:
        print('name: ', user['name'],)
    elif 'realname' in user:
        print('realname: ', user['realname'])
    else:
        print('Haydaaaaaaaa')

'''
