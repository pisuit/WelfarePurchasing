package welfare.persistent.customtype;

public enum MovementStatus {
	/*
	 * ʶҹ� Lot
	 * 				TEMPORARY : �ѧ����ա�ûԴ��Ѻ
	 * 				CANCELED : �ա��¡��ԡ��Ѻ ���� ��ԡ ���� 㺤׹
	 * 				PERMANENT : �ա������͹��Ǩ�ԧ
	 */
	CANCELED("CANCELED","CANCELED"), 
	TEMPORARY("TEMPORARY","TEMPORARY"), 
	PERMANENT("PERMANENT","PERMANENT"), 
	;
	
	private String id;
	private String value;
	MovementStatus(String aID, String aValue){
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
	
	public static MovementStatus find(String aID){
		for (MovementStatus type : MovementStatus.values()) {
			if (type.id.equals(aID)){
				return type;
			}
		}
		return null;
	}

}
