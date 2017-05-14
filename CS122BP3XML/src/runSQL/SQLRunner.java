package runSQL;

import java.io.File;
import java.io.IOException;
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
				p = Runtime.getRuntime().exec("cmd /c del tmp.bat");
				p.waitFor();
				DeleteSQLFile();
			} else {
				PrintWriter writer = new PrintWriter("tmp.sh");
				writer.println("mysql  -u" + username + " -p" + password + " < " + sqlScript);
				writer.close();
				
				Process p = Runtime.getRuntime().exec("chmod 777 tmp.sh");
				p.waitFor();
				p = Runtime.getRuntime().exec("./tmp.sh");
				p.waitFor();
				p = Runtime.getRuntime().exec("rm tmp.sh");
				p.waitFor();
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
			} else {
				Process p = Runtime.getRuntime().exec("chmod 777 " + this.sqlScript);
				p.waitFor();
				p = Runtime.getRuntime().exec("rm " + this.sqlScript);
				p.waitFor();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
