package main;

public class Owner {
	private int id;
	private String name;
	private String phoneNumber;
	private String primaryLanguage;
	private String secondaryLanguage;

	public Owner(int id, String name, String phoneNumber, String primaryLanguage, String secondaryLanguage) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.primaryLanguage = primaryLanguage;
		this.secondaryLanguage = secondaryLanguage;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public String getSecondaryLanguage() {
		return secondaryLanguage;
	}
}