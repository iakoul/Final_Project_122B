public class Item {
	private int id;
	private String name;
	private int type;
	private double price;
	private int quantity;

	public Item(String name) {
		this.name = name;
	}

	public Item(int id, String name, int type, double price) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.price = price;
	}

	// for cart items
	public Item(int id, String name, double price, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if(quantity < 0) {
			this.quantity = 0;
		}
		this.quantity = quantity;
	}
}