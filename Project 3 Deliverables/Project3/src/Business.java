public class Business {
	private long id;
	private String name;
	private String address;
	private String phoneNumber;
	private int yearOpened;
	private int typeId;
	private Plaza plaza;
	// private int ownerId;

	public Business(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Business(long id, String name, String address, String phoneNumber) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public Business(long id, String name, String address, String phoneNumber, int yearOpened, int typeId, Plaza plaza) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.yearOpened = yearOpened;
		this.typeId = typeId;
		this.plaza = plaza;
		// this.ownerId = ownerId;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public int getYearOpened() {
		return yearOpened;
	}

	public int getTypeId() {
		return typeId;
	}

	public Plaza getPlaza() {
		return plaza;
	}

	// public int getOwnerId() {
	// 	return ownerId;
	// }
}