package main;

import java.util.Map;
import java.util.LinkedHashMap;

public class MetadataTable {
	private String name;
	private Map<String,String> columns;

	public MetadataTable(String name) {
		this.name = name;
		columns = new LinkedHashMap<String,String>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void insertColumn(String column, String columnType) {
		columns.put(column, columnType);
	}

	public Map<String,String> getColumns() {
		return columns;
	}
}