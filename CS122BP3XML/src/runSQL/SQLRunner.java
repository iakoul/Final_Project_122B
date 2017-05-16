package runSQL;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SQLRunner {

	private String sqlScript, username, password;
	
	public SQLRunner(String sqlScript, String username, String password) {
		this.sqlScript = sqlScript;
		this.username = username;
		this.password = password;
	}
	
	public void ExecuteSql() {
		try {
			if (System.getProperty("os.name").startsWith("Windows")) {
				PrintWriter writer = new PrintWriter("tmp.bat");
				writer.println("\"C:\\Program Files\\MySQL\\MySQL Workbench 6.3 CE\\mysql.exe\" -u" + username + " -p" + password + " < " + sqlScript);
				writer.close();
				
				Process p = Runtime.getRuntime().exec("cmd /c tmp.bat");
				p.waitFor();
				BufferedReader stdOutput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String s = null;
				while ((s = stdOutput.readLine()) != null) {
				    System.out.println(s);
				}
				p = Runtime.getRuntime().exec("cmd /c del tmp.bat");
				p.waitFor();
				stdOutput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				s = null;
				while ((s = stdOutput.readLine()) != null) {
				    System.out.println(s);
				}
				DeleteSQLFile();
			} else {
				PrintWriter writer = new PrintWriter("tmp.sh");
				writer.println("mysql  -u" + username + " -p" + password + " < " + sqlScript);
				writer.close();
				
				Process p = Runtime.getRuntime().exec("chmod 777 tmp.sh");
				p.waitFor();
				BufferedReader stdOutput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String s = null;
				while ((s = stdOutput.readLine()) != null) {
				    System.out.println(s);
				}
				p = Runtime.getRuntime().exec("./tmp.sh");
				p.waitFor();
				stdOutput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				s = null;
				while ((s = stdOutput.readLine()) != null) {
				    System.out.println(s);
				}
				p = Runtime.getRuntime().exec("rm tmp.sh");
				p.waitFor();
				stdOutput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				s = null;
				while ((s = stdOutput.readLine()) != null) {
				    System.out.println(s);
				}
				DeleteSQLFile();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void DeleteSQLFile() {
		try {
			if (System.getProperty("os.name").startsWith("Windows")) {
				Process p = Runtime.getRuntime().exec("cmd /C del \"" + this.sqlScript + "\"");
				p.waitFor();
				BufferedReader stdOutput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String s = null;
				while ((s = stdOutput.readLine()) != null) {
				    System.out.println(s);
				}
			} else {
				Process p = Runtime.getRuntime().exec("chmod 777 " + this.sqlScript);
				p.waitFor();
				BufferedReader stdOutput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String s = null;
				while ((s = stdOutput.readLine()) != null) {
				    System.out.println(s);
				}
				p = Runtime.getRuntime().exec("rm " + this.sqlScript);
				p.waitFor();
				stdOutput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				s = null;
				while ((s = stdOutput.readLine()) != null) {
				    System.out.println(s);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
