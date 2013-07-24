/**
 * 
 */
package welfare.presentation.stock;

import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import welfare.persistent.controller.MatController;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.stock.Material;
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
public class StockMatManager {
	private String warehouseCode;
	private MatController controller = new MatController();
	private WarehouseController warehouseController = new WarehouseController();
	
	// list panel
	private MaterialGroup editMaterialGroup = new MaterialGroup();
	private ArrayList<SelectItem> materialGroupSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialGroupID;
	private ArrayList<Material> materialList = new ArrayList<Material>();
	// edit panel
	private Material editMaterial = new Material();
	private ArrayList<String> issueUnitList = new ArrayList<String>();
	private ArrayList<String> orderUnitList = new ArrayList<String>();
	private String welcomeMsg = null;
	
	public StockMatManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		System.out.println("Constructor");
		try {
			warehouseCode = warehouseController.getWareHouseCode(securityUser);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		editMaterialGroup.setWarehouseCode(warehouseCode);
		selectedMaterialGroupID = Long.valueOf(-1);
		System.out.println("Warehoust : "+warehouseCode);
	}
	
	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public Long getSelectedMaterialGroupID() {
		return selectedMaterialGroupID;
	}

	public void setSelectedMaterialGroupID(Long selectedMaterialGroupID) {
		this.selectedMaterialGroupID = selectedMaterialGroupID;
	}

	public ArrayList<SelectItem> getMaterialGroupSelectItemList() {
		// TODO หาวิธีที่ดีกว่านี้ในการ refresh ข้อมูล
		createMaterialGroupSelectItemList();
		return materialGroupSelectItemList;
	}

	public ArrayList<Material> getMaterialList() {
		return materialList;
	}

	public MaterialGroup getEditMaterialGroup() {
		return editMaterialGroup;
	}

	public Material getEditMaterial() {
		return editMaterial;
	}

	public void setEditMaterial(Material editMaterial) {
		this.editMaterial = editMaterial;
	}
	
	public ArrayList<String> getIssueUnitList() {
		return issueUnitList;
	}

	public ArrayList<String> getOrderUnitList() {
		return orderUnitList;
	}

	private void createMaterialGroupSelectItemList(){
		try {
			ArrayList<MaterialGroup> materialGroupList = controller.getMaterialGroups(warehouseCode);
			SelectItem selectItem;
			materialGroupSelectItemList.clear();
			materialGroupSelectItemList.add(new SelectItem(Long.valueOf(-1),"เลือกกลุ่มวัสดุ"));
			for (MaterialGroup materialGroup : materialGroupList) {
				selectItem = new SelectItem(materialGroup.getId(), materialGroup.toString());
				materialGroupSelectItemList.add(selectItem);
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}


	private void listMaterial(){
		try {
			materialList.clear();
			materialList = controller.getMaterials(editMaterialGroup);
			issueUnitList = controller.getIssueUnits(warehouseCode);
			orderUnitList = controller.getOrderUnits(warehouseCode);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void materialGroupComboboxSelected(){
		try {
			editMaterialGroup = controller.getMaterialGroup(selectedMaterialGroupID);
			listMaterial();
			newMaterial();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void materialTableRowClicked(){		
	}
	
	public void saveMaterial(){
		LogManager log = new LogManager();
		try {
			if (editMaterial.getId() == null) {
				log.recordCreateMaterial(editMaterial.getCode(), editMaterial.getMaterialGroup().getCode());
			} else {
				log.recordUpdateMaterial(editMaterial.getCode(), editMaterial.getMaterialGroup().getCode());
			}
			controller.saveMaterial(editMaterial);
			listMaterial();
			newMaterial();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deleteMaterial(){
		LogManager log = new LogManager();
		try {
			if (editMaterial.getId() == null){
				throw new ControllerException("กรุณาเลือกวัสดุที่ต้องการลบก่อน");
			}
			// TODO ตรวจสอบว่าสามารถลบกลุ่มนี้ได้หรือไม่ ลบไม่ได้ถ้ายังมี PR/PO/GR/GI ค้างอยู่ 
			editMaterial.setStatus(Status.DELETED);
			controller.saveMaterial(editMaterial);
			log.recordDeleteMaterial(editMaterial.getCode(), editMaterial.getMaterialGroup().getCode());
			listMaterial();
			newMaterial();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void newMaterial(){
		try {
			editMaterial = new Material();
			editMaterial.setWarehouseCode(warehouseCode);
			editMaterial.setMaterialGroup(editMaterialGroup);
			editMaterial.setCode(controller.getNextMaterialCode(warehouseCode, editMaterialGroup));
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public boolean isEditable(){
		return !warehouseCode.equals(null) && editMaterial != null && editMaterialGroup != null && editMaterialGroup.getId() != null;
	}
}
