/**
 * 
 */
package welfare.presentation.stock;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import welfare.persistent.controller.MatController;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.stock.MaterialGroup;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.presentation.common.WarehouseController;
import welfare.security.SecurityUser;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

/**
 * @author Manop
 *
 */
public class StockMatGroupManager {
	private String warehouseCode;
	private MatController controller = new MatController();
	private WarehouseController warehouseController = new WarehouseController();
	
	// list panel
	private ArrayList<MaterialGroup> materialGroupList = new ArrayList<MaterialGroup>();
	// edit panel
	private MaterialGroup editMaterialGroup = new MaterialGroup();
	private String welcomeMsg = null;
	
	public StockMatGroupManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		try {
			warehouseCode = warehouseController.getWareHouseCode(securityUser);
			editMaterialGroup.setCode(controller.getNextMaterialGroupCode(warehouseCode));
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		editMaterialGroup.setWarehouseCode(warehouseCode);
		System.out.println("Warehoust : "+warehouseCode);
		listMaterialGroup();
	}
	
	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public ArrayList<MaterialGroup> getMaterialGroupList() {
		return materialGroupList;
	}	
	
	public MaterialGroup getEditMaterialGroup() {
		return editMaterialGroup;
	}

	public void setEditMaterialGroup(MaterialGroup editMaterialGroup) {
		this.editMaterialGroup = editMaterialGroup;
	}

	private void listMaterialGroup(){
		try {
			materialGroupList.clear();
			materialGroupList = controller.getMaterialGroups(warehouseCode);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void materialTableRowClicked(){		
	}
	
	public void saveMaterialGroup(){
		LogManager log = new LogManager();
		System.out.println("===================="+editMaterialGroup.getId());
		try {
			// TODO ตรวจสอบว่าสามารถลบกลุ่มนี้ได้หรือไม่ ลบไม่ได้ถ้ายังมี PR/PO/GR/GI ค้างอยู่ 
			if (editMaterialGroup.getId() == null){
				log.recordCreateMaterialGroup(editMaterialGroup.getCode());
			} else {
				log.recordUpdateMaterialGroup(editMaterialGroup.getCode());
			}
			controller.saveMaterialGroup(editMaterialGroup);
			listMaterialGroup();
			newMaterialGroup();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deleteMaterialGroup(){
		LogManager log = new LogManager();
		try {
			// TODO ตรวจสอบว่าสามารถลบกลุ่มนี้ได้หรือไม่ ลบไม่ได้ถ้ายังมี PR/PO/GR/GI ค้างอยู่ 
			editMaterialGroup.setStatus(Status.DELETED);
			controller.saveMaterialGroup(editMaterialGroup);
			log.recordDeleteMaterialGroup(editMaterialGroup.getCode());
			listMaterialGroup();
			newMaterialGroup();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void newMaterialGroup(){
		try {
			editMaterialGroup = new MaterialGroup();
			editMaterialGroup.setWarehouseCode(warehouseCode);
			editMaterialGroup.setCode(controller.getNextMaterialGroupCode(warehouseCode));
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public boolean isEditable(){
		return !warehouseCode.equals(null);
	}
}
