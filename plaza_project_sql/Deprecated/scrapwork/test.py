#!/usr/bin/env python3

#Alejandro Bustelo
#04/17/2017
#run with py -3 -m csv_to_sql.py while in the same directory as the CSV files to convert

import csv
import re
import os
import sys
import datetime

def main():
	#print(sys.version_info) #debugging
	print("You will have to delete the top line if the CSV file has a header line.")
	print("Enter name of table VALUEs will be INSERTed INTO")
	destTable = input()
	print("Converting .csv to .sql")
	sqlFile = []
	for filename in os.listdir("./"):
		if filename.endswith(".csv"):
			destFilename = filename[:-4] + ".sql"
			with open(str(filename), newline='\n') as csvreadfile: #read each CSV individually
				csvreader = csv.reader(csvreadfile)
				for row in csvreader:
					sqlRow = "INSERT INTO " + destTable + " VALUES (\"Null\", "
					for c in range(0,5): #number of columns
						#print(row[c]) #debugging
						if not row[c]:
							sqlRow = sqlRow + "\"Null\", "
						else:
							sqlRow = sqlRow + "\"" + row[c] + "\", "
					sqlRow = sqlRow[:-3] + "\");"
					sqlFile.append(sqlRow)
	with open(destFilename, 'w', newline='\n') as sqlWriteFile: #write to destination file
		sqlWriteFile.write('\n'.join(sqlFile))
	print("Finished")

main() #entry point
