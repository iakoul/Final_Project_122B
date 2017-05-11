#Alejandro Bustelo
#05/10/2017
#Python 3
#Turn an HTML document into a out.println() statement that
#can be used in a Java servlet to generate a webpage

def main():

	print('Enter the filename of the HTML file to process:')
	fileName = input()
	outputFile = open(fileName+'_java.txt', 'w')
	tempLine = None

	with open(fileName) as f:
		lines = f.readlines()
		first = lines[0].rstrip().replace('"', r'\"')
		last = lines[-1].rstrip().replace('"', r'\"')
		
		#open("
		tempLine = 'out.println("' + first + r'\n"' + '\n'
		outputFile.write(tempLine)
		
		#HTML content
		for line in lines[1:-1]:
			tempLine = line.rstrip().replace('"', r'\"')
			tempLine = '\t+ "' + tempLine + r'\n"' + '\n'
			outputFile.write(tempLine)
		
		#");
		tempLine = '\t+ "' + last + r'");'
		
		outputFile.write(tempLine)
		
main()