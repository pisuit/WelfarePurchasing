package welfare.persistent.customtype;

public enum PRStatus {
	OPEN("OPEN","Open"), // OPEN : ใบแจ้งสามารถนำไปสร้างเป็นเอกสารจัดหาได้
	CLOSED("CLOSED","Closed"), // CLOSED : ใบแจ้งถูกนำไปสร้างเอกสารจัดหาแล้ว
	DELETED("DELETED","Deleted") // DELETED : ใบแจ้งถูกลบ ไม่สามารถนำไปใช้งาน
	;
	
	private String id;
	private String value;
	PRStatus(String aID, String aValue){
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
	
	public static PRStatus find(String aID){
		for (PRStatus type : PRStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
