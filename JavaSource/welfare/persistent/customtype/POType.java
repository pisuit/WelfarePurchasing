package welfare.persistent.customtype;


public enum POType {
	APPROVAL_REPORT("APPROVAL_REPORT","รายงานอนุมัติจัดหา"),
	PO_DEAIL("PO-DEAIL","PO-ตกลงราคา"),
	PO_TEST("PO-TEST","PO-สอบราคา"),
	PO_BID("PO-BID","PO-ประกวดราคา"),
	PO_EMERGENCY("PO-EMERGENCY","PO-กรณีฉุกเฉิน"),
	PO_SPECIALCASE("PO-SPECIALCASE","PO-วิธีกรณีพิเศษ"),
	PO_SPECIAL("PO-SPECIAL","PO-วิธีพิเศษ"),
	PO_EAUCTION("PO-EAUCTION","PO-e-Auction"),
	EXCHANGE("EXCHANGE","แลกเปลี่ยน")
	;
	
	private String id;
	private String value;
	POType(String aID, String aValue){
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
	
	public static POType find(String aID){
		for (POType poType : POType.values()) {
			if (poType.id.equals(aID)){
				return poType;
			}
		}
		return null;
	}
	
}
