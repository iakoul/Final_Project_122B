To create and populate the mySql tables, use the following commands

mysql -uroot -pmysqlpass
source createtable_95.sql
source data.sql


The program was compiled using Eclipse's Export to Jar option

Right-click on project name, click "Export to Runnable JAR file", select "Copy required libraries into a sub-folder next to the generated JAR", select export location and name (proj1.jar for this example), and Finish.

Alternatively, the following commands may be used instead
javac *.java
java ConsoleApp

To run, use following command in CMD or Linux Terminal while in the same directory as the jar file

java -jar proj1.jar

The address of one of our AWS servers is
ec2-54-65-230-189.ap-northeast-1.compute.amazonaws.com
and the pem file for SSH'ing in is the one included in the ZIP.