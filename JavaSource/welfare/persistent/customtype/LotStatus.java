package welfare.persistent.customtype;

public enum LotStatus {
	/*
	 * ʶҹ� Lot
	 * 				OPEN : �Դ����ԡ�����ͧ�ҡ�ѧ���Դ��Ѻ
	 * 				CLOSED : �Դ����ԡ�����ͧ�ҡ�ѧ���Դ��Ѻ
	 * 				FULL : �Դ����ԡ�����ͧ�ҡ��ͧ�������
	 * 				CANCELED : �Դ����ԡ�����ͧ�ա��¡��ԡ��Ѻ
	 */
	FULL("FULL","FULL"), 
	OPEN("OPEN","OPEN"), 
	CANCELED("CANCELED","CANCELED"), 
	CLOSED("CLOSED","CLOSED"), 
	;
	
	private String id;
	private String value;
	LotStatus(String aID, String aValue){
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
	
	public static LotStatus find(String aID){
		for (LotStatus type : LotStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
