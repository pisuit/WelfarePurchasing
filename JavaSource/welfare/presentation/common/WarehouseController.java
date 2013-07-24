package welfare.presentation.common;

import javax.faces.context.FacesContext;

import welfare.persistent.exception.ControllerException;
import welfare.security.SecurityUser;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

public class WarehouseController {
	public String getWareHouseCode(SecurityUser aUser) throws ControllerException {
		try {
//			if (Constants.SYS_PURCHASING_CANTEEN.equals(aUser.getSubSystemName())){
//				return Constants.WH_CANTEEN;
//			} else if (Constants.SYS_PURCHASING_HOTEL.equals(aUser.getSubSystemName())) {
//				return Constants.WH_HOTEL;
//			} else if (Constants.SYS_PURCHASING_MEDICAL.equals(aUser.getSubSystemName())) {
//				return Constants.WH_MEDICAL;
//			} else if (Constants.SYS_PURCHASING_NURSERY.equals(aUser.getSubSystemName())) {
//				return Constants.WH_NURSERRY;
//			} else if (Constants.SYS_PURCHASING_SPORT.equals(aUser.getSubSystemName())) {
//				return Constants.WH_SPORT;
//			} else if (Constants.SYS_PURCHASING_SWIMMING.equals(aUser.getSubSystemName())) {
//				return Constants.WH_SWIMMING;
//			} else {
//				throw new ControllerException("ไม่เจอคลังที่เหมาะสมกับผู้ใช้งาน");
//			}
			if (Constants.WH_MAHAMEK.equals(aUser.getWarehouseCode())) {
				return Constants.WH_MAHAMEK;
			} else if (Constants.WH_CHIANGMAI.equals(aUser.getWarehouseCode())) {
				return Constants.WH_CHIANGMAI;
			} else if (Constants.WH_HADYAI.equals(aUser.getWarehouseCode())) {
				return Constants.WH_HADYAI;
			} else if (Constants.WH_HUAHIN.equals(aUser.getWarehouseCode())) {
				return Constants.WH_HUAHIN;
			} else if (Constants.WH_NAKORN.equals(aUser.getWarehouseCode())) {
				return Constants.WH_NAKORN;
			} else if (Constants.WH_PHITSANULOK.equals(aUser.getWarehouseCode())) {
				return Constants.WH_PHITSANULOK;
			} else if (Constants.WH_PUKET.equals(aUser.getWarehouseCode())) {
				return Constants.WH_PUKET;
			} else if (Constants.WH_SURAT.equals(aUser.getWarehouseCode())) {
				return Constants.WH_SURAT;
			} else if (Constants.WH_UBON.equals(aUser.getWarehouseCode())) {
				return Constants.WH_UBON;
			} else if (Constants.WH_UDON.equals(aUser.getWarehouseCode())) {
				return Constants.WH_UDON;
			} else {
				throw new ControllerException("ไม่เจอคลังที่เหมาะสมกับผู้ใช้งาน");
			}
		} catch (Exception e){
			throw new ControllerException(e);
		}
	}
}
