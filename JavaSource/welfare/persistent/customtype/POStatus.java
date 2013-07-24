package welfare.persistent.customtype;

public enum POStatus {
	OPEN("OPEN","Open"), // OPEN : เอกสารจัดหานี้สามารถทำการรับของได้เต็มจำนวน
	CLOSED("CLOSED","Closed"), // CLOSED : เอกสารจัดหานี้ทำการรับของแล้วเต็มจำนวน 
	GR_PARTIAL("GR-PAR","GR Patial"), // GR-PAR : เอกสารจัดหานี้ทำการรับของแล้วบางส่วน
	DELETED("DELETED","Deleted") // DELETED : เอกสารจัดหาถูกลบ ไม่สามารถทำงานต่อได้
	;
	
	private String id;
	private String value;
	POStatus(String aID, String aValue){
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
	
	public static POStatus find(String aID){
		for (POStatus type : POStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
