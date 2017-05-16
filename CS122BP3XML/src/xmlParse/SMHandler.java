package xmlParse;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SMHandler extends DefaultHandler {
	
	private boolean types = false;
	private boolean type = false;

	private boolean items = false;
	private boolean item = false;
	private boolean item_name = false;
	private boolean item_type = false;
	private boolean item_price = false;
	private boolean item_pic = false;
	
	private boolean stores = false;
	private boolean store = false;
	private boolean store_name = false;
	private boolean address = false;
	private boolean store_phone = false;
	private boolean store_year = false;
	private boolean store_type_id = false;
	private boolean plaza_id = false;
	private boolean owner_id = false;
	
	private boolean store_sells = false;
	private boolean store_sell = false;
	private boolean store_sell_store_id = false;
	private boolean store_sell_item_id = false;
	
	private boolean writerExists = false;
	PrintWriter writer = null;
	
	private String query = null;
	
	private String sType_id = "null";
	private String sType = "null";
	
	private String sItem_id = "null";
	private String sItem_name = "null";
	private String sItem_type = "null";
	private String sItem_price = "null";
	private String sItem_pic = "null";
	
	private String sStore_id = "null";
	private String sStore_name = "null";
	private String sAddress = "null";
	private String sStorePhone = "null";
	private String sYear = "null";
	private String sStoreType_id = "null";
	private String sPlaza_id = "null";
	private String sOwner_id = "null";
	
	private String sSellStore_id = "1";
	private String sSellItem_id = "1";
	
	public String sqlFile = "default.sql";
	
	@Override
	public void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException {
		if (this.items) {
			if (qname.equals("item")) {
				this.item = true;
				if (attributes.getLength() == 1) {
					//this.item_id = true;
					this.sItem_id = attributes.getValue("item-id");
				}
			}
			if (qname.equals("item-name")) {
				this.item_name = true;
			}
			if (qname.equals("item-type")) {
				this.item_type = true;
			}
			if (qname.equals("item-price")) {
				this.item_price = true;
			}
			if (qname.equals("item-pic")) {
				this.item_pic = true;
			}
		} else if (this.stores) {
			if (qname.equals("store")) {
				this.store = true;
				if (attributes.getLength() == 1) {
					this.sStore_id = attributes.getValue("store-id");
				}
			}
			if (qname.equals("store-name")) {
				this.store_name = true;
			}
			if (qname.equals("address")) {
				this.address = true;
			}
			if (qname.equals("store-phone-number")) {
				this.store_phone = true;
			}
			if (qname.equals("year-opened")) {
				this.store_year = true;
			}
			if (qname.equals("type-id")) {
				this.store_type_id = true;
			}
			if (qname.equals("plaza-id")) {
				this.plaza_id = true;
			}
			if (qname.equals("owner-id")) {
				this.owner_id = true;
			}
		} else if (this.store_sells) {
			if (qname.equals("store-sell")) {
				this.store_sell = true;
			}
			if (qname.equals("store-id")) {
				this.store_sell_store_id = true;
			}
			if (qname.equals("item-id")) {
				this.store_sell_item_id = true;
			}
		} else if (this.types) {
			if (qname.equals("store-type")) {
				this.type = true;
				if (attributes.getLength() == 1) {
					sType_id = attributes.getValue("type-id");
				}
			}
		}
		
		if (qname.equals("items")) {
			this.items = true;
		} else if (qname.equals("stores")) {
			this.stores = true;
		} else if (qname.equals("store-sells")) {
			this.store_sells = true;
		} else if (qname.equals("store-types")) {
			this.types = true;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qname) throws SAXException {
		if (!writerExists) {
			try {
				writer = new PrintWriter(sqlFile, "UTF-8");
				writerExists = true;
				writer.println("USE \"storemarketing\";");
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.getMessage());
			}
		}
		
		if (qname.equals("item")) {
			this.query = "INSERT INTO MerchandiseTbl VALUES (\"" + this.sItem_id + "\", \"" + this.sItem_name + "\", \"" + this.sItem_type + "\", \"" + this.sItem_price + "\", \"" + this.sItem_pic + "\");";
			this.sItem_id = "null";
			this.sItem_name = "null";
			this.sItem_type = "null";
			this.sItem_price = "null";
			this.sItem_pic = "null";
			writer.println(this.query);
			System.out.println(this.query);
			this.item = false;
		} else if (qname.equals("store")) {
			this.query = "INSERT INTO StoreTbl VALUES (\"" + this.sStore_id + "\", \"" + this.sStore_name + "\", \"" + this.sAddress + "\", \"" + this.sStorePhone
					+ "\", \"" + this.sYear + "\", \"" + this.sStoreType_id + "\", \"" + this.sPlaza_id + "\", \"" + this.sOwner_id + "\");";
			this.sStore_id = "null";
			this.sStore_name = "null";
			this.sAddress = "null";
			this.sStorePhone = "null";
			this.sYear = "null";
			this.sStoreType_id = "null";
			this.sPlaza_id = "null";
			this.sOwner_id = "null";
			writer.println(this.query);
			System.out.println(this.query);
			this.store = false;
		} else if (qname.equals("store-sell")) {
			this.query = "INSERT INTO StoreSellsTbl VALUES (\"" + this.sSellStore_id + "\", \"" + this.sSellItem_id + "\");";
			this.sSellItem_id = "1";
			this.sSellStore_id = "1";
			writer.println(this.query);
			System.out.println(this.query);
			this.store_sell = false;
		} else if (qname.equals("store-type")) {
			this.query = "INSERT INTO StoreTypeTbl VALUES (\"" + this.sType_id + "\", \"" + this.sType + "\");";
			this.sType_id = "null";
			this.sType = "null";
			writer.println(this.query);
			System.out.println(this.query);
			this.type = false;
		}
		if (qname.equals("items")) {
			this.items = false;
		} else if (qname.equals("stores")) {
			this.stores = false;
		} else if (qname.equals("store-sells")) {
			this.store_sells = false;
		} else if (qname.equals("store-types")) {
			this.types = false;
		}
		if (qname.equals("input")) {
			writer.close();
		}
	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (this.items) {
			if (this.item_name) {
				this.sItem_name = new String(ch, start, length);
				this.item_name = false;
			}
			if (this.item_type) {
				this.sItem_type = new String(ch, start, length);
				try {
					Integer.parseInt(this.sItem_type.trim());
				} catch (Exception e) {
					System.out.println("Item Type ID: " + this.sItem_type + " is not a valid input.");
					this.sItem_type = "1";
				}
				this.item_type = false;
			}
			if (this.item_price) {
				this.sItem_price = new String(ch, start, length);
				try {
					Integer.parseInt(this.sItem_price.trim());
				} catch (Exception e) {
					System.out.println("Price: " + this.sItem_price + " is not a valid input.");
					this.sItem_price = "1.00";
				}
				this.item_price = false;
			}
			if (this.item_pic) {
				this.sItem_pic = new String(ch, start, length);
				this.item_pic = false;
			}
		} else if (this.stores) {
			if (this.store_name) {
				this.sStore_name = new String(ch, start, length);
				this.store_name = false;
			}
			if (this.address) {
				this.sAddress = new String(ch, start, length);
				this.address = false;
			}
			if (this.store_phone) {
				this.sStorePhone = new String(ch, start, length);
				this.store_phone = false;
			}
			if (this.store_year) {
				this.sYear = new String(ch, start, length);
				try {
					Integer.parseInt(this.sYear.trim());
				} catch (Exception e) {
					System.out.println("Store Year: " + this.sYear + " is not a valid input.");
					this.sYear = "1999";
				}
				this.store_year = false;
			}
			if (this.store_type_id) {
				this.sStoreType_id = new String(ch, start, length);
				try {
					Integer.parseInt(this.sStoreType_id.trim());
				} catch (Exception e) {
					System.out.println("Store Type ID: " + this.sStoreType_id + " is not a valid input.");
					this.sStoreType_id = "1";
				}
				this.store_type_id = false;
			}
			if (this.plaza_id) {
				this.sPlaza_id = new String(ch, start, length);
				try {
					Integer.parseInt(this.sPlaza_id.trim());
				} catch (Exception e) {
					System.out.println("Plaza ID: " + this.sPlaza_id + " is not a valid input.");
					this.sPlaza_id = "1";
				}
				this.plaza_id = false;
			}
			if (this.owner_id) {
				this.sOwner_id = new String(ch, start, length);
				try {
					Integer.parseInt(this.sOwner_id.trim());
				} catch (Exception e) {
					System.out.println("Owner ID: " + this.sOwner_id + " is not a valid input.");
					this.sOwner_id = "1";
				}
				this.owner_id = false;
			}
		} else if (this.store_sells) {
			if (this.store_sell_store_id) {
				this.sSellStore_id = new String(ch, start, length);
				try {
					Integer.parseInt(this.sSellStore_id.trim());
				} catch (Exception e) {
					System.out.println("StoreSells Store ID: " + this.sSellStore_id + " is not a valid input.");
					this.sSellStore_id = "1";
				}
				this.store_sell_store_id = false;
			}
			if (this.store_sell_item_id) {
				this.sSellItem_id = new String(ch, start, length);
				try {
					Integer.parseInt(this.sSellItem_id.trim());
				} catch (Exception e) {
					System.out.println("StoreSells Item ID: " + this.sSellItem_id + " is not a valid input.");
					this.sSellItem_id = "1";
				}
				this.store_sell_item_id = false;
			}
		} else if (this.types) {
			if (this.type) {
				this.sType = new String(ch, start, length);
				this.type = false;
			}
		}
	}
}
