package welfare.persistent.customtype;

public enum GRType {
	PO("P","รับผ่านใบสั่งซื้อ"),
	NOPO("NP","รับไม่ผ่านใบสั่งซื้อ");
	
	private String id;
	private String value;
	
	GRType(String aID, String aValue){
		id = aID;
		value = aValue;
	}
	
	public String getID(){
		return id;
	}
	
	public String getValue(){
		return value;
	}

	public String toString() {
		return value;
	}
	
	public static GRType find(String aID){
		for (GRType poType : GRType.values()) {
			if (poType.id.equals(aID)){
				return poType;
			}
		}
		return null;
	}
}
