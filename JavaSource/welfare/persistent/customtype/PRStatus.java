package welfare.persistent.customtype;

public enum PRStatus {
	OPEN("OPEN","Open"), // OPEN : �������ö������ҧ���͡��èѴ����
	CLOSED("CLOSED","Closed"), // CLOSED : ��駶١������ҧ�͡��èѴ������
	DELETED("DELETED","Deleted") // DELETED : ��駶١ź �������ö�����ҹ
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
