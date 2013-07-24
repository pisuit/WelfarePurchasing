package welfare.persistent.customtype;

public enum Warehouse {
	WH_CANTEEN("WH_CANTEEN","คลังโภชนาการและสโมสร"),
	WH_HOTEL("WH_HOTEL","คลังห้องพักงามดูพลี"),
	WH_MEDICAL("WH_MEDICAL","คลังเวชภัณฑ์"),
	WH_NURSERRY("WH_NURSERRY","คลังสถานรับเลี้ยงเด็ก"),
	WH_SPORT("WH_SPORT","คลังงานกีฬา"),
	WH_SWIMMING("WH_SWIMMING","คลังสระว่ายน้ำ");
	
	private String id;
	private String value;
	
	Warehouse(String aID, String aValue){
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
	
	public static Warehouse find(String aID){
		for (Warehouse poType : Warehouse.values()) {
			if (poType.id.equals(aID)){
				return poType;
			}
		}
		return null;
	}
}
