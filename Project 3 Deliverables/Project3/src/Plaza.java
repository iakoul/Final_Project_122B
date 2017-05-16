public class Plaza {
	private double id;
	private String name;
	private City city;

	public Plaza(double id, String name) {
		this.id = id;
		this.name = name;
	}

	public Plaza(double id, String name, City city) {
		this.id = id;
		this.name = name;
		this.city = city;
	}

	public double getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public City getCity() {
		return city;
	}
}