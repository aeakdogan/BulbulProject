###################
#   Fetches lastm track information using lastfm track mbid:
#   You should provide a file with lastfm track mbids
#   Important: You should check the output file paths and update them
###################


import requests
import time
import json
import atexit
from os import listdir

# Last FM API key ekle (NEW***)
api_key = '4f8e6432dbbb90f1c0a75cac9448902d'
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

dirs = listdir('album_info')
downloaded_files = []
for filename in dirs:
    downloaded_files.append(filename)

notify_slack('Lastfm Album fetcher started(2)')
atexit.register(notify_slack, message='Lastfm Album finished(2)')

skipped_file_count = 0
with open( "outputs/unique_2.txt", 'r') as file:
    for i,line in enumerate(file):
        if  line[:-1] in downloaded_files:
            skipped_file_count += 1
            continue
        elif len(line) > 5:
            album_url = "http://ws.audioscrobbler.com/2.0/?method=album.getInfo&api_key={}&mbid={}&format=json".format(api_key, line[:-1])
            album_info = get_checked(album_url)
            time.sleep(0.2)

            with open("album_info/" + line[:-1], 'w+') as resp_file:
                resp_file.write(album_info.text)

        if i % 1000 == 0:
            notify_slack('{} # of files are skipped (2)'.format(skipped_file_count))
            notify_slack('{} - (2) Lastfm Album info has been downloaded!'.format(i))
