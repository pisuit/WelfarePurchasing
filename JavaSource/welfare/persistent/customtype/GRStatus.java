package welfare.persistent.customtype;

public enum GRStatus {
	/*
	 * สถานะใบรับพัสดุ
	 * 				GR-FULL : ใบรับพัสดุนี้เป็นแบบรับของเต็มจำนวน
	 * 				GR-PAR : ใบรับพัสดุนี้เป็นแบบรับของบางส่วน
	 * 				DELETED : ใบรับพัสดุนี้ถูกยกเลิก
	 * 				RETURN-PAR : ใบรับพัสดุนี้มีการคืนของบางส่วน
	 * 				RETURN-ALL : ใบรับพัสดุนี้มีการคืนของทั้งหมด
	 * 				CLOSED : ใบรับพัสดุถูกปิด ไม่สามารถทำการยกเลิก
	 */
	OPEN("OPEN","OPEN"), 
	GR_FULL("GR-FULL","GR-FULL"), 
	GR_PAR("GR-PAR","GR-PAR"), 
	DELETED("DELTED","DELTED"), 
	RET_ALL("RETURN-ALL","RETURN-ALL"), 
	RET_PAR("RETURN-PAR","RETURN-PAR"), 
	CLOSED("CLOSED","CLOSED"), 
	;
	
	private String id;
	private String value;
	GRStatus(String aID, String aValue){
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
	
	public static GRStatus find(String aID){
		for (GRStatus type : GRStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
