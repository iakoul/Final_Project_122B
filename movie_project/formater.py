# Not really production value, but just a quick test on how to format some lines given from the data


with open("movie_data.sql") as f:
    content = f.readlines()
# you may also want to remove whitespace characters like `\n` at the end of each line

buff = []

for i in content:
    e = i.strip().strip("['INSERT INTO movies VALUES(")
    e = e.strip(");")

    e.split(',')
    buff.append(e)

for i in range(len(buff)):
    buff[i] = buff[i].split(',')

    for j in range(len(buff[i])):
        buff[i][j] = buff[i][j].strip().strip("'")
    
mine = buff[:10]

for i in mine:
    print("{:10}{:30}{:10}{:40}{:100}{:150}".format(i[0],i[1],i[2],i[3],i[4],i[5]))





