
content = open('outputs/unique_albums.txt').read()
content_list = content.split('\n')

part_size = int(len(content_list) / 3)

with open('outputs/unique_albums_1.txt', 'w') as f:
    for i in range(0, part_size):
        f.write('{}\r\n'.format(content_list[i]))

with open('outputs/unique_albums_2.txt', 'w') as f:
    for i in range(part_size, 2*part_size):
        f.write('{}\r\n'.format(content_list[i]))

with open('outputs/unique_albums_3.txt', 'w') as f:
    for i in range(2*part_size, len(content_list)):
        f.write('{}\r\n'.format(content_list[i]))
