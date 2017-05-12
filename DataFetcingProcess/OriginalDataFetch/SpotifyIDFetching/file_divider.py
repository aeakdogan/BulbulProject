import sys
import os

file_loc = sys.argv[1]
content = open(file_loc).read()
content_list = content.split('\n')

split_no = int(sys.argv[2])
part_size = int(len(content_list) / split_no)
print(part_size)
print(len(content_list))
print(split_no)

if 'outputs' not in os.listdir('.'):
    os.mkdir('outputs')
for i in range(split_no):
    with open('outputs/unique_{}.txt'.format(str(i + 1)), 'w') as f:
        for j in range(i * part_size, (i+1) * part_size):
            f.write('{}\r\n'.format(content_list[j]))





