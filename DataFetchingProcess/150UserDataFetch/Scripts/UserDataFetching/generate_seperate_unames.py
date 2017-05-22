###########
#   Mesut Gurlek
#   Generate user name files
###########


num_of_machine = 3
username_per_file = 5000

with open('all_known_user_names', 'r') as f:
    all_usernames = f.read().split()

with open('selected_unames', 'r') as f:
    fetched_usernames = f.read().split()


for var in fetched_usernames:
    all_usernames.remove(var)

counter = 1
for num in range(num_of_machine):
    filename = 'unames_{}'.format(num)

    with open(filename, 'w') as f:
        while counter % username_per_file != 0:
            f.write('{}\n'.format(all_usernames[counter]))
            counter += 1
    counter += 1
    print('{} is generated'.format(filename))


