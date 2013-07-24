/**
 * 
 */
package welfare.presentation.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import welfare.persistent.controller.LotController;
import welfare.persistent.controller.MatController;
import welfare.persistent.controller.MovementController;
import welfare.persistent.customtype.StorageLocation;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.MaterialGroup;
import welfare.persistent.domain.stock.StockMovement;
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
public class StockMovementManager {
	private MovementController controller = new MovementController();
	private MatController matController = new MatController();
	private WarehouseController warehouseController = new WarehouseController();
	private LotController lotController = new LotController();
	private String warehouseCode;
	
	private ArrayList<StockMovement> stockMovementList = new ArrayList<StockMovement>();
	private ArrayList<SelectItem> storageLocationSelectItemList  = new ArrayList<SelectItem>();
	private String selectedStorageLocationID;
	private ArrayList<SelectItem> materialGroupSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialGroupID;
	private ArrayList<SelectItem> materialSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialID;
	private Material selectedMaterial;
	private MaterialGroup selectedMaterialGroup;
	private StorageLocation selectedStorageLocation;
	private Date movementStartDate;
	private Date movementEndDate;
	private BigDecimal availableQty = new BigDecimal("0.00");
	
	public StockMovementManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		try {
			warehouseCode = warehouseController.getWareHouseCode(securityUser);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		refreshData();
	}
	
	public String getSelectedStorageLocationID() {
		return selectedStorageLocationID;
	}

	public void setSelectedStorageLocationID(String selectedStorageLocationID) {
		this.selectedStorageLocationID = selectedStorageLocationID;
	}

	public Long getSelectedMaterialGroupID() {
		return selectedMaterialGroupID;
	}

	public void setSelectedMaterialGroupID(Long selectedMaterialGroupID) {
		this.selectedMaterialGroupID = selectedMaterialGroupID;
	}

	public Long getSelectedMaterialID() {
		return selectedMaterialID;
	}

	public void setSelectedMaterialID(Long selectedMaterialID) {
		this.selectedMaterialID = selectedMaterialID;
	}

	public ArrayList<StockMovement> getStockMovementList() {
		return stockMovementList;
	}

	public ArrayList<SelectItem> getStorageLocationSelectItemList() {
		return storageLocationSelectItemList;
	}

	public ArrayList<SelectItem> getMaterialGroupSelectItemList() {
		return materialGroupSelectItemList;
	}

	public ArrayList<SelectItem> getMaterialSelectItemList() {
		return materialSelectItemList;
	}

	public Date getMovementStartDate() {
		return movementStartDate;
	}

	public void setMovementStartDate(Date movementStartDate) {
		this.movementStartDate = movementStartDate;
	}

	public Date getMovementEndDate() {
		return movementEndDate;
	}

	public void setMovementEndDate(Date movementEndDate) {
		this.movementEndDate = movementEndDate;
	}

	public BigDecimal getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(BigDecimal availableQty) {
		this.availableQty = availableQty;
	}

	public void refreshData(){
		createMaterialGroupSelectItemList();
		createStorageLocationSelectItemList();
		clearAll();
	}
	
	public void searchStockMovement(){
		try {
			stockMovementList.clear();
			stockMovementList = controller.getStockMovements(warehouseCode, selectedStorageLocation, selectedMaterial, movementStartDate, movementEndDate);
			availableQty = lotController.getAvailableQuantity(warehouseCode, selectedStorageLocation, selectedMaterial);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void clearAll(){
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedStorageLocationID = "-1";
		stockMovementList.clear();
		movementEndDate = CalendarUtils.getDateTimeInstance().getTime();
		Calendar calendar = CalendarUtils.getDateTimeInstance();
		calendar.add(Calendar.MONTH, -1);
		movementStartDate = calendar.getTime();
	}
	
	public void storageLocationComboBoxSelected(){
		selectedStorageLocation = StorageLocation.find(selectedStorageLocationID);
		/*
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedMaterialGroup = null;
		selectedMaterial = null;
		stockMovementList.clear();
		*/
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
	
	public void materialComboBoxSelected(){
		try {
			selectedMaterial = matController.getMaterial(selectedMaterialID);
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
			if (selectedMaterialGroup == null ){
				ArrayList<Material> matList = matController.getMaterials(warehouseCode);
				materialSelectItemList.clear();
				materialSelectItemList.add(new SelectItem(Long.valueOf(-1),"เลือกวัสดุ"));
				SelectItem selectItem;
				for (Material material : matList) {
					selectItem = new SelectItem(material.getId(), material.toString());
					materialSelectItemList.add(selectItem);
				}
			} else {
				ArrayList<Material> matList = matController.getMaterials(selectedMaterialGroup);
				materialSelectItemList.clear();
				materialSelectItemList.add(new SelectItem(Long.valueOf(-1),"เลือกวัสดุ"));
				SelectItem selectItem;
				for (Material material : matList) {
					selectItem = new SelectItem(material.getId(), material.toString());
					materialSelectItemList.add(selectItem);
				}
			}
		} catch (ControllerException e) {
			e.printStackTrace();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
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

}
