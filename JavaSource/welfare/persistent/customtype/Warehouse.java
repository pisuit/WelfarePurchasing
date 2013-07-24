package welfare.persistent.customtype;

public enum Warehouse {
	WH_CANTEEN("WH_CANTEEN","��ѧ����ҡ����������"),
	WH_HOTEL("WH_HOTEL","��ѧ��ͧ�ѡ����پ��"),
	WH_MEDICAL("WH_MEDICAL","��ѧ�Ǫ�ѳ��"),
	WH_NURSERRY("WH_NURSERRY","��ѧʶҹ�Ѻ����§��"),
	WH_SPORT("WH_SPORT","��ѧ�ҹ����"),
	WH_SWIMMING("WH_SWIMMING","��ѧ������¹��");
	
	private String id;
	private String value;
	
	Warehouse(String aID, String aValue){
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
	
	public static Warehouse find(String aID){
		for (Warehouse poType : Warehouse.values()) {
			if (poType.id.equals(aID)){
				return poType;
			}
		}
		return null;
	}
}
