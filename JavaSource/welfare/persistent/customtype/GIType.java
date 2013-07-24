package welfare.persistent.customtype;

public enum GIType {
	ISSUE("ISSUE","เบิกวัสดุ"),
	SCRAB("SCRAB","ตัดจำหน่าย"),
	EXCHANGE("EXCHANGE","แลกเปลี่ยน");
	
	private String id;
	private String value;
	
	GIType(String aID, String aValue){
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
	
	public static GIType find(String aID){
		for (GIType poType : GIType.values()) {
			if (poType.id.equals(aID)){
				return poType;
			}
		}
		return null;
	}
}
