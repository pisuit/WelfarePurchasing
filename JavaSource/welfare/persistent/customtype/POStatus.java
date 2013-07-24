package welfare.persistent.customtype;

public enum POStatus {
	OPEN("OPEN","Open"), // OPEN : �͡��èѴ�ҹ������ö�ӡ���Ѻ�ͧ������ӹǹ
	CLOSED("CLOSED","Closed"), // CLOSED : �͡��èѴ�ҹ��ӡ���Ѻ�ͧ��������ӹǹ 
	GR_PARTIAL("GR-PAR","GR Patial"), // GR-PAR : �͡��èѴ�ҹ��ӡ���Ѻ�ͧ���Ǻҧ��ǹ
	DELETED("DELETED","Deleted") // DELETED : �͡��èѴ�Ҷ١ź �������ö�ӧҹ�����
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
