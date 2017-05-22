###################
#   Fetches lastm track information using lastfm track mbid:
#   You should provide a file with lastfm track mbids
#   Important: You should check the output file paths and update them
###################


import requests
import time
import json
import atexit

api_key = '385e930ba5105ac30ad2371a8fe1112f'
slack_webhook_url = 'https://hooks.slack.com/services/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'

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

notify_slack('Lastfm Track fetcher started(3)')
atexit.register(notify_slack, message='Lastfm Track finished(3)')

with open( "../outputs/unique_tracks.txt", 'r') as file:
    for i,line in enumerate(file):
        if len(line) > 5:
            track_url = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key={}&mbid={}&format=json".format(api_key, line[:-1])
            track_info = get_checked(track_url)
            time.sleep(1.7)
            # save the top tracks of user
            with open("track_info/" + line[:-1], 'w+') as resp_file:
                resp_file.write(track_info.text)

        if i % 200 == 0:
            notify_slack('{} - Lastfm track info has been downloaded!'.format(i))
