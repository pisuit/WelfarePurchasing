package welfare.persistent.customtype;


public enum POType {
	APPROVAL_REPORT("APPROVAL_REPORT","��§ҹ͹��ѵԨѴ��"),
	PO_DEAIL("PO-DEAIL","PO-��ŧ�Ҥ�"),
	PO_TEST("PO-TEST","PO-�ͺ�Ҥ�"),
	PO_BID("PO-BID","PO-��СǴ�Ҥ�"),
	PO_EMERGENCY("PO-EMERGENCY","PO-�óթء�Թ"),
	PO_SPECIALCASE("PO-SPECIALCASE","PO-�Ըաóվ����"),
	PO_SPECIAL("PO-SPECIAL","PO-�Ըվ����"),
	PO_EAUCTION("PO-EAUCTION","PO-e-Auction"),
	EXCHANGE("EXCHANGE","�š����¹")
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
