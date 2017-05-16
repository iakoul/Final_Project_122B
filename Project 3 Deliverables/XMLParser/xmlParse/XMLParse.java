package xmlParse;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.*;

public class XMLParse {

	private String xmlFile;
	
	public XMLParse(String xmlFile) {
		this.xmlFile = xmlFile;
	}
	
	public String convertToSql() {
		//create SQL file using SAX parser
		String sqlFileName = xmlFile + ".sql";
		
		try {
			File inputFile = new File(xmlFile);
			SAXParserFactory fac = SAXParserFactory.newInstance();
			SAXParser saxPars = fac.newSAXParser();
			SMHandler handler = new SMHandler();
			handler.sqlFile = sqlFileName;
			saxPars.parse(inputFile, handler);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return sqlFileName;
	}
	
}
