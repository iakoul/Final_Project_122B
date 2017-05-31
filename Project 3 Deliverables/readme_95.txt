XML Parsing Optimizations


Optimization 1 - The parser streams the XML document and generates a SQL file. This is as opposed to loading the entire XML document into RAM and inserting the files all at once. We used a SAX parser instead of a DOM parser.
Optimization 2 - The parser generates and runs a SQL script after it finishes parsing the last XML tag. Opening a link to the mysql database and running each query one by one would be very slow so we do it all at once at the end.

Run the SQL scripts in the following order:

1. tina_plaza_table.sql
2. queries_all.sql
3. FixesAndUpdates.sql
4. AddStoreStoredProcedurePrototype.sql

We used an XML generator written in C++ to generate our XML files.


To compile the XML parser, run the following command inside the XMLParser folder.

javac runSQL\*.java xmlParse\*.java

Then run

java xmlParse.InsertXMLtoMysql

and type in the name of the XML file to insert. Then, it will ask for your database username and password.
If any errors are encountered, it will output them. Otherwise, it will output the insert statements as it generates them.

We made the web project on Eclipse so run it on there.