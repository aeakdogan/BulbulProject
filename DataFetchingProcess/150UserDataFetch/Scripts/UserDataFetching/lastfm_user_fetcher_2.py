###################
#   Fetches lastm user information:
#   For more information please ask @omerakgul
#   Important: You should check the output file paths and update them
###################

import urllib.request
import json, requests
import mmap
from queue import *
import os
import atexit
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
    api_key = '7d67bf49e1c3743c1e2ce02eefd530ee'

    friends_url = "http://ws.audioscrobbler.com/2.0?method=user.getfriends&user={}&api_key={}&format=json".format(username, api_key)
    recent_tracks_url = "http://ws.audioscrobbler.com/2.0?method=user.getrecenttracks&user={}&api_key={}&format=json&limit=200".format(username, api_key)
    top_artists_url = "http://ws.audioscrobbler.com/2.0?method=user.gettopartists&user={}&api_key={}&format=json".format(username, api_key)
    top_tracks_url = "http://ws.audioscrobbler.com/2.0?method=user.gettoptracks&user={}&api_key={}&format=json&period=overall".format(username, api_key)
    top_albums_url = "http://ws.audioscrobbler.com/2.0?method=user.gettopalbums&user={}&api_key={}&format=json".format(username,api_key)

    resp = get_checked(url=friends_url)
    data = json.loads(resp.text)
    time.sleep(1)
    # save the response.
    with open('friends/' + username + '_friends_resp', 'w+') as resp_file:
        resp_file.write(resp.text)

    recent_tracks = get_checked(url=recent_tracks_url)
    time.sleep(1)
    # save the recent tracks of user
    with open('friends/' + username + '_recent_tracks', 'w+') as resp_file:
        resp_file.write(recent_tracks.text)

    top_artists = get_checked(url=top_artists_url)
    time.sleep(1)
    # save the top artists of user
    with open('friends/' + username + '_top_artists', 'w+') as resp_file:
        resp_file.write(top_artists.text)

    top_tracks = get_checked(url=top_tracks_url)
    time.sleep(1)
    # save the top tracks of user
    with open('friends/' + username + '_top_tracks', 'w+') as resp_file:
        resp_file.write(top_tracks.text)

    top_albums = get_checked(url=top_albums_url)
    time.sleep(2)
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
def collect_data(username, file):

    unames = Queue(maxsize=20000000)
    unames.put(username)

    while not unames.empty():

        username = unames.get()
        friends_url = "http://ws.audioscrobbler.com/2.0?method=user.getfriends&user={}&api_key=385e930ba5105ac30ad2371a8fe1112f&format=json".format(username)

        resp = get_checked(url=friends_url)
        data = json.loads(resp.text)
        time.sleep(1)
        # save the response.
        with open('friends/' + username + '_friends_resp', 'w+') as resp_file:
            resp_file.write(resp.text)

        increase_by_un()

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

                unames.put(new_guy)
                write_uname(new_guy, file)


    else:
        print("No friends, I am lonely, call me...")


def collect_data_uname_known(username):

    dir_name = 'user_detail/'
    api_key = '4f8e6432dbbb90f1c0a75cac9448902d'

    friends_url = "http://ws.audioscrobbler.com/2.0?method=user.getfriends&user={}&api_key={}&format=json".format(username, api_key)
    recent_tracks_url = "http://ws.audioscrobbler.com/2.0?method=user.getrecenttracks&user={}&api_key={}&format=json&limit=200".format(username, api_key)
    top_artists_url = "http://ws.audioscrobbler.com/2.0?method=user.gettopartists&user={}&api_key={}&format=json".format(username, api_key)
    top_tracks_url = "http://ws.audioscrobbler.com/2.0?method=user.gettoptracks&user={}&api_key={}&format=json&period=overall".format(username, api_key)
    top_albums_url = "http://ws.audioscrobbler.com/2.0?method=user.gettopalbums&user={}&api_key={}&format=json".format(username, api_key)
    info_url = "http://ws.audioscrobbler.com/2.0?method=user.getinfo&user={}&api_key={}&format=json".format(username, api_key)
    loved_tracks_url = "http://ws.audioscrobbler.com/2.0?method=user.getlovedtracks&user={}&api_key={}&format=json".format(username, api_key)

    resp = get_checked(url=friends_url)
    time.sleep(1.7)
    # save the response.
    with open(dir_name + username + '_friends_resp', 'w+') as resp_file:
        resp_file.write(resp.text)

    recent_tracks = get_checked(url=recent_tracks_url)
    time.sleep(1.7)
    # save the recent tracks of user
    with open(dir_name + username + '_recent_tracks', 'w+') as resp_file:
        resp_file.write(recent_tracks.text)

    top_artists = get_checked(url=top_artists_url)
    time.sleep(1.7)
    # save the top artists of user
    with open(dir_name + username + '_top_artists', 'w+') as resp_file:
        resp_file.write(top_artists.text)

    top_tracks = get_checked(url=top_tracks_url)
    time.sleep(1.7)
    # save the top tracks of user
    with open(dir_name + username + '_top_tracks', 'w+') as resp_file:
        resp_file.write(top_tracks.text)

    top_albums = get_checked(url=top_albums_url)
    time.sleep(2)
    # save the top tracks of user
    with open(dir_name + username + '_top_albums', 'w+') as resp_file:
        resp_file.write(top_albums.text)

    info = get_checked(url=info_url)
    time.sleep(2)
    # save info of user
    with open(dir_name + username + '_info', 'w+') as resp_file:
        resp_file.write(info.text)

    loved_tracks = get_checked(url=loved_tracks_url)
    time.sleep(2)
    # save the top tracks of user
    with open(dir_name + username + '_loved_tracks', 'w+') as resp_file:
        resp_file.write(loved_tracks.text)


def notify_slack(message):
    payload = {
        'payload': json.dumps({'text': message})
    }
    print(message)
    resp = requests.post(slack_webhook_url, data=payload)


def get_checked(url):
    response = requests.get(url=url)
    if response.status_code != 200:
        notify_slack(response.json())
        exit(0)
    else:
        return response

uname_file = 'all_known_user_names'
selected_unames = 'unames_1'
seed = 'a'
slack_webhook_url = 'https://hooks.slack.com/services/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'

notify_slack('User fetcher started')
atexit.register(notify_slack, message='User fetcher finished')

increase_by_un(init=True, known_file=uname_file)
#collect_data(seed, uname_file)

with open(selected_unames, 'r') as all_names:
    for i, line in enumerate(all_names):
        collect_data_uname_known(line[:-1])
        print(i)







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
