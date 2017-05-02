public class Item {
	private int id;
	private String name;
	private int type;
	private double price;

	public Item(int id, String name, int type, double price) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public double getPrice() {
		return price;
	}
}