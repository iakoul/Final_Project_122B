import random


f = open('workfile', 'w')

for _ in range(400):
    f.write(str(random.randint(2,4))+'\n')
