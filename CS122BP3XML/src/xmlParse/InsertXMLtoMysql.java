package xmlParse;

import java.util.Scanner;
import runSQL.SQLRunner;

public class InsertXMLtoMysql {
	public static void main(String[] args) {
		
		System.out.println("Enter name of XML file");
		Scanner scan = new Scanner(System.in);
		String xmlFile = scan.next();
		
		XMLParse x = new XMLParse(xmlFile);
		String sqlFile = x.convertToSql();
		
		System.out.println("Enter the database's username followed by the password");
		String username = scan.next().trim();
		String password = scan.next().trim();
		
		runSQL.SQLRunner s = new SQLRunner(sqlFile, username, password);
		s.ExecuteSql();
		
		System.out.println("Done");
		scan.close();
	}
}
