/**
 * 
 */
package welfare.presentation.stock;

import java.util.ArrayList;
import java.util.Calendar;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import welfare.persistent.controller.BudgetController;
import welfare.persistent.controller.GIController;
import welfare.persistent.controller.GRController;
import welfare.persistent.controller.MatController;
import welfare.persistent.controller.POController;
import welfare.persistent.controller.PRController;
import welfare.persistent.controller.VendorController;
import welfare.persistent.customtype.GIType;
import welfare.persistent.customtype.StorageLocation;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.purchasing.GoodsReceiptItem;
import welfare.persistent.domain.stock.GoodsIssue;
import welfare.persistent.domain.stock.GoodsIssueItem;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.MaterialGroup;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.WarehouseController;
import welfare.security.SecurityUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

/**
 * @author Manop
 *
 */
public class StockGIManager {
	private GIController controller = new GIController();
	private MatController matController = new MatController();
	private WarehouseController warehouseController = new WarehouseController();
	private String warehouseCode;
	
	private ArrayList<GoodsIssue> giList = new ArrayList<GoodsIssue>();
	private GoodsIssue editGI;
	private GoodsIssueItem editGIItem;
	private ArrayList<String> issuerNameList = new ArrayList<String>();
	private ArrayList<SelectItem> giTypeSelectItemList = new ArrayList<SelectItem>();
	private String selectedGITypeID;
	private ArrayList<GoodsIssueItem> giItemList = new ArrayList<GoodsIssueItem>(); // รายการย่อยในใบสั่งซื้อ
	private ArrayList<String> issueUnitList = new ArrayList<String>();
	private ArrayList<SelectItem> storageLocationSelectItemList = new ArrayList<SelectItem>();
	private String selectedStorageLocationID = "-1";
	private ArrayList<SelectItem> materialGroupSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialGroupID;
	private ArrayList<SelectItem> materialSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialID;
	private MaterialGroup selectedMaterialGroup;
	private Material selectedMaterial;
	private StorageLocation selectedStorageLocation;
	private GIType selectedGIType;
	
	public StockGIManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		try {
			warehouseCode = warehouseController.getWareHouseCode(securityUser);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		refreshData();
	}
	
	public ArrayList<GoodsIssue> getGiList() {
		return giList;
	}

	public GoodsIssue getEditGI() {
		return editGI;
	}

	public void setEditGI(GoodsIssue editGI) {
		this.editGI = editGI;
	}

	public GoodsIssueItem getEditGIItem() {
		return editGIItem;
	}

	public void setEditGIItem(GoodsIssueItem editGIItem) {
		this.editGIItem = editGIItem;
	}

	public ArrayList<SelectItem> getGiTypeSelectItemList() {
		return giTypeSelectItemList;
	}

	public String getSelectedGITypeID() {
		return selectedGITypeID;
	}

	public void setSelectedGITypeID(String selectedGITypeID) {
		this.selectedGITypeID = selectedGITypeID;
	}

	private void listGI(){
		try {
			giList.clear();
			giList = controller.getGoodsIssues(warehouseCode);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void refreshData(){
		createMaterialGroupSelectItemList();
		getSuggestionList();
		createGITypeSelectItemList();
		listGI();
		selectedMaterial = null;
		selectedMaterialID = Long.valueOf(-1);
		selectedMaterialGroup = null;
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedStorageLocation = null;
		selectedStorageLocationID = "-1";
	}
	
	public void giTableRowClicked(){
		try {
			editGI = controller.getGoodsIssue(editGI.getId());
			giItemList.clear();
			giItemList.addAll(editGI.getGoodsIssueItems());
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void saveGI(){
		try {
			controller.saveGoodsIssue(editGI);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void cancelGI() {
		
	}
	
	public void newGI(){
		editGI = new GoodsIssue();
		editGI.setWarehouse(warehouseCode);
		editGI.setIssuedDate(CalendarUtils.getDateTimeInstance().getTime());
		editGI.setIssueType(selectedGIType);
	}
	
	public void saveGIItem(){
		giItemList.add(editGIItem);
	}
	
	public void deleteGIITem(){
		
	}
	
	public void newGIItem(){
		editGIItem = new GoodsIssueItem();
		editGIItem.setGoodsIssue(editGI);
		editGIItem.setWarehouse(warehouseCode);
		editGIItem.setStorageLocation(selectedStorageLocation);
	}
	
	public void materialGroupComboBoxSelected(){
		try {
			selectedMaterialGroup = matController.getMaterialGroup(selectedMaterialGroupID);
			createMaterialSelectItem();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void giTypeComboBoxSelected(){
		selectedGIType = GIType.find(selectedGITypeID);
	}
	
	public void materialComboBoxSelected(){
		try {
			selectedMaterial = matController.getMaterial(selectedMaterialID);
			editGIItem.setMaterial(selectedMaterial);
			if (selectedMaterial != null ){
				editGIItem.setUnit(selectedMaterial.getIssueUnit());
				editGIItem.setUnitPrice(selectedMaterial.getOrderUnitPrice());
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void createMaterialGroupSelectItemList(){
		try {
			ArrayList<MaterialGroup> materialGroupList = matController.getMaterialGroups(warehouseCode);
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
	
	private void createMaterialSelectItem(){
		try {
			ArrayList<Material> matList = matController.getMaterials(selectedMaterialGroup);
			materialSelectItemList.clear();
			materialSelectItemList.add(new SelectItem(Long.valueOf(-1),"เลือกวัสดุ"));
			SelectItem selectItem;
			for (Material material : matList) {
				selectItem = new SelectItem(material.getId(), material.toString());
				materialSelectItemList.add(selectItem);
			}
		} catch (ControllerException e) {
			e.printStackTrace();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
		}
	}

	private void getSuggestionList(){
		try {
			// ชื่อผู้ลงนามในใบแจ้ง
			issuerNameList.clear();
			issuerNameList = controller.getIssuerNames(warehouseCode);
			// หน่วยซื้อ
			issueUnitList = matController.getIssueUnits(warehouseCode);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private void createStorageLocationSelectItemList(){
		SelectItem selectItem;
		storageLocationSelectItemList.clear();
		storageLocationSelectItemList.add(new SelectItem("-1","เลือกสถานที่จัดเก็บ"));
		for (StorageLocation stLocation : StorageLocation.warehouse(warehouseCode)) {
			selectItem = new SelectItem();
			selectItem.setValue(stLocation.getID());
			selectItem.setLabel(stLocation.getValue());
			storageLocationSelectItemList.add(selectItem);
		}
	}
	
	private void createGITypeSelectItemList(){
		SelectItem selectItem;
		storageLocationSelectItemList.clear();
		storageLocationSelectItemList.add(new SelectItem("-1","เลือกสถานที่จัดเก็บ"));
		for (StorageLocation stLocation : StorageLocation.warehouse(warehouseCode)) {
			selectItem = new SelectItem();
			selectItem.setValue(stLocation.getID());
			selectItem.setLabel(stLocation.getValue());
			storageLocationSelectItemList.add(selectItem);
		}
	}

	public ArrayList<String> getIssueUnitList() {
		return issueUnitList;
	}

	public String getSelectedStorageLocationID() {
		return selectedStorageLocationID;
	}

	public void setSelectedStorageLocationID(String selectedStorageLocationID) {
		this.selectedStorageLocationID = selectedStorageLocationID;
	}

	public ArrayList<SelectItem> getMaterialGroupSelectItemList() {
		return materialGroupSelectItemList;
	}

	public Long getSelectedMaterialGroupID() {
		return selectedMaterialGroupID;
	}

	public void setSelectedMaterialGroupID(Long selectedMaterialGroupID) {
		this.selectedMaterialGroupID = selectedMaterialGroupID;
	}

	public ArrayList<SelectItem> getMaterialSelectItemList() {
		return materialSelectItemList;
	}


}
