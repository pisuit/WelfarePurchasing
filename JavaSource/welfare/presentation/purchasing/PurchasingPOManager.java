/**
 * 
 */
package welfare.presentation.purchasing;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;

import com.sun.corba.se.internal.Interceptors.PIORB;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import welfare.persistent.controller.BudgetController;
import welfare.persistent.controller.MatController;
import welfare.persistent.controller.POController;
import welfare.persistent.controller.PRController;
import welfare.persistent.controller.SecurityController;
import welfare.persistent.controller.VendorController;
import welfare.persistent.customtype.Status;
import welfare.persistent.customtype.POStatus;
import welfare.persistent.customtype.POType;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.purchasing.PurchaseOrder;
import welfare.persistent.domain.purchasing.PurchaseOrderItem;
import welfare.persistent.domain.purchasing.PurchaseRequisition;
import welfare.persistent.domain.purchasing.PurchaseRequisitionItem;
import welfare.persistent.domain.purchasing.Vendor;
import welfare.persistent.domain.security.BudgetAuthorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.MaterialGroup;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.presentation.common.WarehouseController;
import welfare.reportdata.PurchasingPOLessReportData;
import welfare.reportdata.PurchasingPOMoreReportData;
import welfare.security.SecurityUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;
import welfare.utils.ReportUtils;

/**
 * @author Manop
 *
 */
public class PurchasingPOManager {
	private POController controller = new POController();
	private PRController prController =  new PRController();
	private MatController matController = new MatController();
	private BudgetController budController = new BudgetController();
	private VendorController vendorController = new VendorController();
	private WarehouseController warehouseController = new WarehouseController();
	private SecurityController secController = new SecurityController();
	private int budgetYear;
	private Budget budget;
	private String warehouseCode;

	// PO List panel
	private List<PurchaseOrder> poList = new ArrayList<PurchaseOrder>();
	private PurchaseOrder editPO = new PurchaseOrder();
	
	// PO Header panel
	private ArrayList<SelectItem> prSelectItemList = new ArrayList<SelectItem>();
	private Long selectedPrID = Long.valueOf(-1);
	private ArrayList<SelectItem> poTypeSelectItemList = new ArrayList<SelectItem>();
	private String selectedPOType = "-1";
	private PurchaseRequisition editPR;
	private ArrayList<SelectItem> vendorSelectItemList = new ArrayList<SelectItem>();
	private Long selectedVendorID = Long.valueOf(-1);
	private ArrayList<String> buyerNameList = new ArrayList<String>();
	private ArrayList<String> receiverNameList = new ArrayList<String>();
	private ArrayList<String> requisitionerNameList = new ArrayList<String>();
	private ArrayList<String> requisitionerPosList = new ArrayList<String>();
	private ArrayList<String> inspectorNameList = new ArrayList<String>();
	private ArrayList<String> inspectorPosList = new ArrayList<String>();
	private ArrayList<String> approverNameList = new ArrayList<String>();
	private ArrayList<String> approverPosList = new ArrayList<String>();
	
	// PO Item panel
	private ArrayList<PurchaseOrderItem> poItemList = new ArrayList<PurchaseOrderItem>(); // รายการย่อยในใบสั่งซื้อ
	private ArrayList<String> orderUnitList = new ArrayList<String>();
	private PurchaseOrderItem editPOItem = new PurchaseOrderItem();
	private ArrayList<SelectItem> budgetItemSelectItemList = new ArrayList<SelectItem>(); // combobox Item สำหรับให้เลือกหมวดงบประมาณ
	private Long selectedBudgetItemID; // ID ของหมวดงบประมาณที่ต้องการใช้
	private ArrayList<SelectItem> materialGroupSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialGroupID;
	private ArrayList<SelectItem> materialSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialID;
	private BudgetItem selectedBudgetItem;
	private MaterialGroup selectedMaterialGroup;
	private Material selectedMaterial;
	private Vendor selectedVendor;
	private boolean isNewPoItem;
	private int fakeItemNo = 0;
	private boolean isPoSaved = false;
	private Vendor vendor;
	private String welcomeMsg = null;
	
	public PurchasingPOManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		try {
			warehouseCode = warehouseController.getWareHouseCode(securityUser);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		// หาปีงบประมาณ
		Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
		budgetYear = CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime());
		try {
			budget = budController.getBudget(budgetYear);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		welcomeMsg = securityUser.getFullName();
		createPRSelectItemList();
		createPOTypeSelectItemList();
		createVendorSelectItemList();
		createMaterialGroupSelectItemList();
		createBudgetItemSelectItemList();
		getSuggestionList();
		listPO();
		newPO();
	}
	
	public User getUserFromSession(){
		User user = new User();
		try {
			SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
			user = secController.getUser(securityUser.getUsername(), securityUser.getSystemName(), securityUser.getSubSystemName());			
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public void refreshData(){
		createPRSelectItemList();
		createVendorSelectItemList();
		getSuggestionList();
		listPO();
		newPO();
	}
	
	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public PurchaseOrder getEditPO() {
		return editPO;
	}

	public void setEditPO(PurchaseOrder editPO) {
		this.editPO = editPO;
	}

	public Long getSelectedPrID() {
		return selectedPrID;
	}

	public void setSelectedPrID(Long selectedPrID) {
		this.selectedPrID = selectedPrID;
	}

	public String getSelectedPOType() {
		return selectedPOType;
	}

	public void setSelectedPOType(String selectedPOType) {
		this.selectedPOType = selectedPOType;
	}

	public Long getSelectedVendorID() {
		return selectedVendorID;
	}

	public void setSelectedVendorID(Long selectedVendorID) {
		this.selectedVendorID = selectedVendorID;
	}

	public ArrayList<SelectItem> getVendorSelectItemList() {
		return vendorSelectItemList;
	}

	public Long getSelectedBudgetItemID() {
		return selectedBudgetItemID;
	}

	public void setSelectedBudgetItemID(Long selectedBudgetItemID) {
		this.selectedBudgetItemID = selectedBudgetItemID;
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

	public Long getSelectedMaterialID() {
		return selectedMaterialID;
	}

	public void setSelectedMaterialID(Long selectedMaterialID) {
		this.selectedMaterialID = selectedMaterialID;
	}

	public List<PurchaseOrder> getPoList() {
		return poList;
	}

	public ArrayList<SelectItem> getPrSelectItemList() {
		return prSelectItemList;
	}

	public ArrayList<SelectItem> getPoTypeSelectItemList() {
		return poTypeSelectItemList;
	}

	public ArrayList<String> getBuyerNameList() {
		return buyerNameList;
	}

	public ArrayList<String> getReceiverNameList() {
		return receiverNameList;
	}

	public ArrayList<String> getRequisitionerNameList() {
		return requisitionerNameList;
	}

	public ArrayList<String> getRequisitionerPosList() {
		return requisitionerPosList;
	}

	public ArrayList<String> getInspectorNameList() {
		return inspectorNameList;
	}

	public ArrayList<String> getInspectorPosList() {
		return inspectorPosList;
	}

	public ArrayList<String> getApproverNameList() {
		return approverNameList;
	}

	public ArrayList<String> getApproverPosList() {
		return approverPosList;
	}

	public ArrayList<PurchaseOrderItem> getPoItemList() {
		return poItemList;
	}

	public PurchaseOrderItem getEditPOItem() {
		return editPOItem;
	}

	public void setEditPOItem(PurchaseOrderItem editPOItem) {
		this.editPOItem = editPOItem;
	}

	public ArrayList<String> getOrderUnitList() {
		return orderUnitList;
	}

	public ArrayList<SelectItem> getBudgetItemSelectItemList() {
		return budgetItemSelectItemList;
	}

	public BudgetItem getSelectedBudgetItem() {
		return selectedBudgetItem;
	}

	private void listPO(){
		try {
			poList.clear();
			poList = controller.getPurchaseOrders(warehouseCode);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void poTableRowClicked(){
		try {
			System.out.println("PO ID : "+editPO.getId());
			editPO = controller.getPurchaseOrder(editPO.getId());
			if(editPO.getVendor() != null){
				selectedVendorID = editPO.getVendor().getId();
			}		
			selectedPOType = editPO.getPoType().getID();
			poItemList.clear();
			poItemList.addAll(editPO.getPurchaseOrderItems());
			fakeItemNo = poItemList.size()+1;
			newPOItem();
			System.out.println(editPO.getRequisitionerName());
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void prComboBoxSelected(){
		try {
			/*
			if(selectedPOType == null || selectedPOType.equalsIgnoreCase("-1")) {
				selectedPrID = Long.valueOf(-1);
				throw new ControllerException("กรุณาเลือกประเภทการจัดซื้อ/จ้าง");
			}
			*/
			//newPO();  ใส่แล้วรวน
			if(selectedPrID.equals(Long.valueOf(-1))){
				newPO();
			}
			editPR = prController.getPurchaseRequisition(selectedPrID);
			if (editPR == null ) return;
			// สร้าง PO โดย copy ข้อมูลมาจาก PR
			editPO.setPurchaseRequisition(editPR);
			editPO.setReason(editPR.getReason());
			editPO.setRequisitionerName(editPR.getRequisitionerName());
			editPO.setRequisitionerPos(editPR.getRequisitionerPos());
			editPO.setInspectorName(editPR.getInspectorName());
			editPO.setInspectorPos(editPR.getInspectorPos());
			editPO.setApproverName(editPR.getApproverName());
			editPO.setApproverPos(editPR.getApproverPos());
			editPO.setTotalPrice(editPR.getTotalPrice());
			PurchaseOrderItem orderItem;
			poItemList.clear();
			for (PurchaseRequisitionItem prItem : editPR.getPurchaseRequisitionItems()) {
				orderItem = new PurchaseOrderItem();
				orderItem.setPurchaseOrder(editPO);
				orderItem.setItemNumber(prItem.getItemNumber());
				orderItem.setDeliveryDate(prItem.getDeliveryDate());
				orderItem.setMaterial(prItem.getMaterial());
				orderItem.setOtherMaterial(prItem.getOtherMaterial());
				orderItem.setBudgetItem(prItem.getBudgetItem());
				orderItem.setQuantity(prItem.getQuantity());
				orderItem.setOrderUnit(prItem.getOrderUnit());
				orderItem.setUnitPrice(prItem.getUnitPrice());
				orderItem.setReceivedQuantity(new BigDecimal("0.00"));
				orderItem.setBudgetReservedAmount(prItem.getBudgetReservedAmount());
				poItemList.add(orderItem);
			}	
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void poTypeComboBoxSelected(){
		editPO.setPoType(POType.find(selectedPOType));
		//createPRSelectItemList();
		System.out.println("PO Type : "+ editPO.getPoType());
	}
	
	public void vendorComboBoxSelected(){
		try {
			selectedVendor = vendorController.getVendor(selectedVendorID);
			editPO.setVendor(selectedVendor);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void savePO(){
		LogManager log = new LogManager();
		try {
			if (editPO.getPoType() == null) {
				throw new ControllerException("กรุณาเลือกประเภทการจัดซื้อ");
			}
			if (editPO.getVendor() == null && StringUtils.isEmpty(editPO.getOtherVendor())){
				throw new ControllerException("กรุณาเลือกชื่อร้านค้า");
			}
			if (editPO.getVendor() != null && !StringUtils.isEmpty(editPO.getOtherVendor())){
				throw new ControllerException("กรุณาเลือกชื่อร้านค้าหรือชื่อร้านค้าอื่น อย่างใดอย่างหนึ่งเท่านั้น");
			}	
//			if(editPO.getVendor() != null && (editPO.getOtherVendor() != null || !StringUtils.isEmpty(editPO.getOtherVendor()))){
//				throw new ControllerException("กรุณาเลือกชื่อร้านค้าหรือชื่อร้านค้าอื่น อย่างใดอย่างหนึ่งเท่านั้น");
//			}
			
			int poItemSize = 0;
			for (PurchaseOrderItem poItem : poItemList) {
				if (poItem.getStatus().equals(Status.NORMAL)){
					poItemSize++;
				}
				if (poItemSize == 0) {
					throw new ControllerException("กรุณาใส่รายการที่ต้องการแจ้งจัดหาอย่างน้อย 1 รายการ");
				}
			}
			editPO.setPurchaseOrderItems(poItemList);
			if (selectedBudgetItem != null) {
				if (selectedBudgetItem.isControlled() == true) {
					if (editPO.getSumPrice().compareTo(selectedBudgetItem.getAvailableAmount()) == 1) {
						throw new ControllerException("งบประมาณที่คงเหลืออยู่ไม่พอสำหรับการสร้างใบแจ้งจัดซื้อ");
					}
				}
			}
			if (editPO.getId() == null){
				editPO.setBudgetYear(budgetYear);
				editPO = controller.savePurchaseOrder(editPO);
				log.recordCreatePo(editPO.getFormattedPoNumber(), editPO.getTotalPrice());
			} else {
				editPO = controller.savePurchaseOrder(editPO);
				log.recordUpdatePo(editPO.getFormattedPoNumber(), editPO.getTotalPrice());
			}			
			createPRSelectItemList();
			listPO();
			poTableRowClicked();
			isPoSaved = true;
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deletePO(){
		LogManager log = new LogManager();
		try {
			if (editPO.getId() == null ){
				throw new ControllerException("ไม่สามารถลบใบสั่งซื้อที่ยังไม่ได้บันทึกได้");
			}
			log.recordDeletePo(editPO.getFormattedPoNumber());
			editPO = controller.deletePurchaseOrder(editPO);
			createPRSelectItemList();
			listPO();
			poTableRowClicked();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void closePO(){
		LogManager log = new LogManager();
		try {
			if (editPO.getId() == null ){
				throw new ControllerException("ไม่สามารถปิดใบสั่งซื้อที่ยังไม่ได้บันทึกได้");
			}
			log.recordClosePo(editPO.getFormattedPoNumber());
			editPO =  controller.closePurchaseOrder(editPO);
			createPRSelectItemList();
			listPO();
			poTableRowClicked();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void printPOLess(){
		LogManager log = new LogManager();
		try {
			JasperReport report = null;
			report = (JasperReport)JRLoader.loadObject(this.getClass().getResource("/welfare/reports/poForm1.jasper"));
			//report = (JasperReport)JRLoader.loadObject("D:/poForm.jasper");
			
			ArrayList<PurchasingPOLessReportData> poReportList = new ArrayList<PurchasingPOLessReportData>();
			
			/*for (int i=1; i<poItemList.size(); i++) {
				PurchaseOrderItem purchaseOrderItem = ((PurchaseOrderItem)poItemList.get(i));
				PurchasingPOReportData poReport = new PurchasingPOReportData();
				
				poReport.setItemNo(purchaseOrderItem.getItemNumber());
				poReport.setDeliveryDate(purchaseOrderItem.getDeliveryDate());
				poReport.setQty(purchaseOrderItem.getQuantity());
				poReport.setUnit(purchaseOrderItem.getOrderUnit());
				poReport.setUnitPrice(purchaseOrderItem.getUnitPrice());
				
				String material = "";
				if (purchaseOrderItem.getMaterial() == null) {
					material = purchaseOrderItem.getOtherMaterial();
				} else {
					material = purchaseOrderItem.getMaterial().toString();
				}
				
				poReport.setMaterial(material);
				poReportList.add(poReport);
			}*/
			
			for (PurchaseOrderItem purchaseOrderItem : poItemList) {
				PurchasingPOLessReportData poLessReport = new PurchasingPOLessReportData();
								
				poLessReport.setItemNo(purchaseOrderItem.getItemNumber());
				poLessReport.setDeliveryDate(purchaseOrderItem.getDeliveryDate());
				poLessReport.setQty(purchaseOrderItem.getQuantity());
				poLessReport.setUnit(purchaseOrderItem.getOrderUnit());
				poLessReport.setUnitPrice(purchaseOrderItem.getUnitPrice());
				
				String material = "";
				if (purchaseOrderItem.getMaterial() == null) {
					material = purchaseOrderItem.getOtherMaterial();
				} else {
					material = purchaseOrderItem.getMaterial().getDescription();
				}
				
				poLessReport.setMaterial(material);
				poReportList.add(poLessReport);
			}		
			
			vendor = controller.getVendorForReport(editPO.getId());
						
			HashMap poReportHashMap = new HashMap();
			poReportHashMap.put("poNo", editPO.getFormattedPoNumber());
			poReportHashMap.put("createdDate", editPO.getPostingDate());
			poReportHashMap.put("text", editPO.getReason());
			poReportHashMap.put("purchasingType", editPO.getPoType().toString());
			if(vendor != null){
				poReportHashMap.put("vendor", vendor.getName());
			} else {
				poReportHashMap.put("vendor", editPO.getOtherVendor());
			}
			poReportHashMap.put("buyerName", editPO.getBuyerName());
			poReportHashMap.put("receiverName", editPO.getReceiverName());
			poReportHashMap.put("purchaserName", editPO.getRequisitionerName());
			poReportHashMap.put("inspectorName", editPO.getInspectorName());
			poReportHashMap.put("approverName", editPO.getApproverName());
			poReportHashMap.put("purchaserPos", editPO.getRequisitionerPos());
			poReportHashMap.put("inspectorPos", editPO.getInspectorPos());
			poReportHashMap.put("approverPos", editPO.getApproverPos());
			poReportHashMap.put("totalPrice", editPO.getTotalPrice());
			poReportHashMap.put("LOGO", this.getClass().getResource("/welfare/reports/logo.gif"));
			poReportHashMap.put("logo", this.getClass().getResource("/welfare/reports/aerologo.png"));
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, poReportHashMap, new JRBeanCollectionDataSource(poReportList));
						
			//JasperExportManager.exportReportToPdfFile(jasperPrint,"D:/testPO.pdf");			
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			ReportUtils.displayPdfReport(bytes);
			
			log.recordPrintPoLess(editPO.getFormattedPoNumber());
			
		} catch (JRException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO: handle exception
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void printPOMore(){
		LogManager log = new LogManager();
		try {
			JasperReport report = null;
			report = (JasperReport)JRLoader.loadObject(this.getClass().getResource("/welfare/reports/poForm2.jasper"));
			//report = (JasperReport)JRLoader.loadObject("D:/poForm.jasper");
			
			ArrayList<PurchasingPOMoreReportData> poReportList = new ArrayList<PurchasingPOMoreReportData>();					
			
			for (PurchaseOrderItem purchaseOrderItem : poItemList) {
				PurchasingPOMoreReportData poMoreReport = new PurchasingPOMoreReportData();
								
				poMoreReport.setItemNo(purchaseOrderItem.getItemNumber());
				poMoreReport.setQty(purchaseOrderItem.getQuantity());
				poMoreReport.setUnit(purchaseOrderItem.getOrderUnit());
				poMoreReport.setUnitPrice(purchaseOrderItem.getUnitPrice());
				
				String material = "";
				if (purchaseOrderItem.getMaterial() == null) {
					material = purchaseOrderItem.getOtherMaterial();
				} else {
					material = purchaseOrderItem.getMaterial().getDescription();
				}
				
				poMoreReport.setMaterial(material);
				poReportList.add(poMoreReport);
			}		
			
			vendor = controller.getVendorForReport(editPO.getId());
						
			HashMap poReportHashMap = new HashMap();
			poReportHashMap.put("poNo", editPO.getFormattedPoNumber());
			if(vendor != null){
				poReportHashMap.put("vendor", vendor.getName());
				if (vendor.getAddress1() != null)
					poReportHashMap.put("vendorHouseNo", vendor.getAddress1());
					if (vendor.getAddress2() != null)
					poReportHashMap.put("vendorAddr1", vendor.getAddress1() + " " + vendor.getAddress2());
					if (vendor.getAddress3() != null)
					poReportHashMap.put("vendorAddr2", vendor.getAddress3());
			} else {
				poReportHashMap.put("vendor", editPO.getOtherVendor());
			}	
			poReportHashMap.put("approverName", editPO.getApproverName());
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, poReportHashMap, new JRBeanCollectionDataSource(poReportList));
						
			//JasperExportManager.exportReportToPdfFile(jasperPrint,"D:/testPO.pdf");			
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			ReportUtils.displayPdfReport(bytes);
			
			log.recordPrintPoMore(editPO.getFormattedPoNumber());
			
		} catch (JRException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO: handle exception
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void newPO(){
		editPO = new PurchaseOrder();
		selectedBudgetItem = null;
		editPO.setPostingDate(CalendarUtils.getDateTimeInstance().getTime());
		editPO.setWarehouseCode(warehouseCode);
		selectedVendorID = Long.valueOf(-1);
		selectedBudgetItemID = Long.valueOf(-1);
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedPrID = Long.valueOf(-1);
		selectedPOType = "-1";		
		poItemList.clear();
		fakeItemNo = 0;
		newPOItem();
		isPoSaved = false;
	}
	
	
	public void poItemTableRowClicked(){
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedBudgetItemID = editPOItem.getBudgetItem().getId();
		if (editPOItem.getMaterial() != null){
			selectedMaterialGroupID = editPOItem.getMaterial().getMaterialGroup().getId();
			selectedMaterialID = editPOItem.getMaterial().getId();
			}
		materialGroupComboBoxSelected();
		isNewPoItem = false;
	}
	
	public void savePOItem(){
		try {
			// ตรวจสอบว่ามีการเลือก material หรือ ใส่ other material หรือไม่
			if (editPOItem.getMaterial() == null && ( editPOItem.getOtherMaterial()== null || editPOItem.getOtherMaterial().trim().equals("")) ){
				throw new ControllerException("กรุณาเลือกวัสดุที่ต้องการจัดหา");
			}
			// ไม่ให้ใส่ทั้ง material และ othermaterial
			if (editPOItem.getMaterial() != null && (editPOItem.getOtherMaterial() != null && !editPOItem.getOtherMaterial().trim().equals("")) ){
				throw new ControllerException("กรุณาเลือกวัสดุ หรือ วัสดุอื่นๆอย่างใดอย่างหนึ่งเท่านั้น");
			}
			// ตรวจสอบว่ามีการเลือกหมดงบประมาณ หรือไม่
			if (editPOItem.getBudgetItem() == null){
				throw new ControllerException("กรุณาเลือกหมวดงบประมาณ");
			}
			// ตรวจสอบว่ามีรายการซ้ำหรือไม่
			for (PurchaseOrderItem poItem : poItemList) {
				if (editPOItem.getItemNumber() != poItem.getItemNumber()){
					if (editPOItem.getMaterial() != null && poItem.getMaterial() != null && editPOItem.getMaterial().getId().equals(poItem.getMaterial().getId())){
						System.out.println("Same material");
						throw new ControllerException("วัสดุ "+editPOItem.getMaterial()+" มีอยู่แล้วไม่สามารดำเนินการต่อได้");
					} else if (editPOItem.getOtherMaterial() != null && poItem.getOtherMaterial() != null 
							&& !editPOItem.getOtherMaterial().trim().equals("")
							&& !editPOItem.getOtherMaterial().trim().equals("")
							&& editPOItem.getOtherMaterial().equals(poItem.getOtherMaterial()) ) {
						System.out.println("Same othermaterial");
						throw new ControllerException("วัสดุ "+editPOItem.getOtherMaterial()+" มีอยู่แล้วไม่สามารดำเนินการต่อได้");
					}
				}
			}
			if (isNewPoItem){
				poItemList.add(editPOItem);
			}
			System.out.println("Numberrrrrrrrrrrrrrrrrrr"+editPOItem.getItemNumber());
			newPOItem();			
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deletePOItem(){
		if (editPOItem.getId() == null) {
			int index = 0;
			for (PurchaseOrderItem item : poItemList) {
				if (item.getItemNumber() == editPOItem.getItemNumber()){
					poItemList.remove(index);
					break;
				}
				index++;
			}
		} else {
			editPOItem.setStatus(Status.DELETED);
		}	
		
	}
	
	public void newPOItem(){
		editPOItem = new PurchaseOrderItem();
		if(isEmergencyOrExchange()) {
			editPOItem.setBudgetItem(selectedBudgetItem);
		}
		editPOItem.setPurchaseOrder(editPO);
		editPOItem.setDeliveryDate(CalendarUtils.getDateTimeInstance().getTime());
		editPOItem.setItemNumber(fakeItemNo++);
		editPOItem.setStatus(Status.NORMAL);
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		//selectedBudgetItemID = Long.valueOf(-1);
		isNewPoItem = true;
	}
	
	public void materialGroupComboBoxSelected(){
		try {
			selectedMaterialGroup = matController.getMaterialGroup(selectedMaterialGroupID);
			//editPRItem.setOtherMaterial("");
			createMaterialSelectItem();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void budgetItemComboBoxSelected(){
		// find budgetItem 
		try {
			if (!selectedBudgetItemID.equals(Long.valueOf(-1))) {
				selectedBudgetItem = budController.getBudgetItem(selectedBudgetItemID);
				if (selectedBudgetItem.isAvailable() == false) {
					throw new ControllerException("งบประมาณถูกปิดการเบิกใช้");
				}
				if (selectedBudgetItem.isPurchasingBudget() == false) {
					throw new ControllerException("งบประมาณหมวดนี้ไม่สามารถใช้สร้างใบแจ้งจัดซื้อได้");
				}
				System.out.println("Budget Item : " + selectedBudgetItem.getCategory() );
				editPOItem.setBudgetItem(selectedBudgetItem);
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void materialComboBoxSelected(){
		try {
			selectedMaterial = matController.getMaterial(selectedMaterialID);
			editPOItem.setMaterial(selectedMaterial);
			if (selectedMaterial != null ){
				editPOItem.setOrderUnit(selectedMaterial.getOrderUnit());
				editPOItem.setUnitPrice(selectedMaterial.getOrderUnitPrice());
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void createPOTypeSelectItemList(){
		SelectItem selectItem;
		poTypeSelectItemList.clear();
		poTypeSelectItemList.add(new SelectItem("-1","เลือกประเภทการจัดซื้อ/จ้าง"));
		for (POType poType : POType.values()) {
			selectItem = new SelectItem();
			selectItem.setValue(poType.getID());
			selectItem.setLabel(poType.getValue());
			poTypeSelectItemList.add(selectItem);
		}
	}
	
	private void createPRSelectItemList(){
		try {
			prSelectItemList.clear();
			ArrayList<PurchaseRequisition> openPrList = prController.getOpenPurchaseRequisitions(warehouseCode);
			prSelectItemList.add(new SelectItem(Long.valueOf(-1),"เลือกใบแจ้งจัดหา"));
			SelectItem selectItem;
			for(PurchaseRequisition pr : openPrList){
				selectItem = new SelectItem();
				selectItem.setValue(pr.getId());
				selectItem.setLabel(pr.getFormattedPrNumber());
				prSelectItemList.add(selectItem);
			}
//			BigDecimal comparedValue = new BigDecimal(10000);
//			if (selectedPOType.equalsIgnoreCase(POType.APPROVAL_REPORT.getID())) {
//				for (PurchaseRequisition pr : openPrList) {
//					if(pr.getTotalPrice().compareTo(comparedValue) == -1) {
//						selectItem = new SelectItem();
//						selectItem.setValue(pr.getId());
//						selectItem.setLabel(pr.getFormattedPrNumber());
//						prSelectItemList.add(selectItem);
//					}			
//				}
//			} else if (!selectedPOType.equals("-1")){
//				for (PurchaseRequisition pr : openPrList) {
//					if(pr.getTotalPrice().compareTo(comparedValue) == 1) {
//						selectItem = new SelectItem();
//						selectItem.setValue(pr.getId());
//						selectItem.setLabel(pr.getFormattedPrNumber());
//						prSelectItemList.add(selectItem);
//					}			
//				}
//			}
			
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void createBudgetItemSelectItemList(){
		try {
			budgetItemSelectItemList.clear();
			ArrayList<BudgetAuthorization> budgetAuthorizations = budController.getBudgetAuthorizationsForCurrentUser(getUserFromSession().getId());
			Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
			int budgetYear = CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime());
			ArrayList<BudgetItem> authorizedBudgetList = new ArrayList<BudgetItem>();
			BudgetItem item;
			for (BudgetAuthorization auth : budgetAuthorizations) {
				item = new BudgetItem();
				item = budController.getAuthorizedBudgetItemForCurrentUser(budgetYear, auth.getBudgetAuth());
				authorizedBudgetList.add(item);
			}
			/*ArrayList<BudgetItem> budgetItemList = new ArrayList<BudgetItem>();
			for(BudgetItem items : authorizedBudgetList) {
				getSubBudgetItemsForAuthorizedUser(items,budgetItemList);
			}*/				
			//ArrayList<BudgetItem> budgetItemList = budController.getAllBugetItems(budgetYear);
			budgetItemSelectItemList.add(new SelectItem(Long.valueOf(-1),"เลือกงบประมาณ"));
			/*SelectItem selectItem;
			for (BudgetItem budgetItem : budgetItemList) {
				//if (budgetItem.isPurchasingBudget() && budgetItem.getSubBudgetItems().size() == 0 && budgetItem.isAvailable()){
				if (budgetItem.isPurchasingBudget() && budgetItem.isAvailable()){
					selectItem = new SelectItem();
					selectItem.setValue(budgetItem.getId());
					selectItem.setLabel(budgetItem.toString());
					budgetItemSelectItemList.add(selectItem);
				}
			}*/
			SelectItem selectItem;
			for (BudgetItem budgetItem : authorizedBudgetList) {
				if (budgetItem.isPurchasingBudget() && budgetItem.isAvailable()){
					selectItem = new SelectItem();
					selectItem.setValue(budgetItem.getId());
					selectItem.setLabel(budgetItem.toString());
					budgetItemSelectItemList.add(selectItem);
				}
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void getSubBudgetItemsForAuthorizedUser(BudgetItem budgetItem, List<BudgetItem> budgetItemList) {
		try {
			ArrayList<BudgetItem> budgetItems;	
			if (budgetItem == null){
				return;
			}
			budgetItemList.add(budgetItem);
			budgetItems = budController.getSubBudgetItemsForAuthorizedUser(budgetItem);
			for (BudgetItem budgetItem2 : budgetItems) {
				getSubBudgetItemsForAuthorizedUser(budgetItem2, budgetItemList);
			}
			
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		
	}
	
	private void createVendorSelectItemList(){
		try {
			vendorSelectItemList.clear();
			ArrayList<Vendor> venderList = vendorController.getActiveVendors();
			vendorSelectItemList.add(new SelectItem(Long.valueOf(-1),"เลือกร้านค้า"));
			SelectItem selectItem;
			for (Vendor vendor : venderList) {
				selectItem = new SelectItem();
				selectItem.setValue(vendor.getId());
				selectItem.setLabel(vendor.getName());
				vendorSelectItemList.add(selectItem);
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
			// ชื่อผู้ลงนามในใบจัดซื้อ
			buyerNameList.clear();
			buyerNameList = controller.getBuyerNames(warehouseCode);
			receiverNameList.clear();
			receiverNameList = controller.getReceiverNames(warehouseCode);
			requisitionerNameList.clear();
			requisitionerNameList = controller.getRequisitionerNames(warehouseCode);
			requisitionerPosList.clear();
			requisitionerPosList = controller.getRequisitionerPos(warehouseCode);
			inspectorNameList.clear();
			inspectorNameList = controller.getInspectorNames(warehouseCode);
			inspectorPosList.clear();
			inspectorPosList = controller.getInspectorPos(warehouseCode);
			approverNameList.clear();
			approverNameList = controller.getApproverNames(warehouseCode);
			approverPosList.clear();
			approverPosList = controller.getApproverPos(warehouseCode);
			// หน่วยซื้อ
			orderUnitList = matController.getOrderUnits(warehouseCode);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public boolean isEmergencyOrExchange(){
		return selectedPOType.equalsIgnoreCase(POType.PO_EMERGENCY.getID())
				|| selectedPOType.equalsIgnoreCase(POType.EXCHANGE.getID());
	}
	
	public boolean isPoItemEditable(){
		//System.out.println("isEmergency = "+isEmergencyOrExchange());
		return isPoEditable() && editPOItem != null && editPOItem.getStatus().equals(Status.NORMAL) && isEmergencyOrExchange();
	}
	
	public boolean isPoItemDeletable(){
		return !isNewPoItem && isEmergencyOrExchange();
	}
	
	public boolean isPoItemCreatable(){
		return !isNewPoItem && isEmergencyOrExchange();
	}
	
	public boolean isPoCreatable(){
		return editPO != null && editPO.getId() != null;
	}
	
	public boolean isPoEditable(){
		return budget != null && budget.isAvailable() && editPO != null && editPO.getStatus().equals(POStatus.OPEN) && !isPoSaved && !selectedPOType.equals("-1");
	}
	
	public boolean isPoDeletable(){
		return isPoEditable() && editPO.getId() != null;
	}
	
	public boolean isPoLessPrintable(){
		return editPO != null && editPO.getId() != null && editPO.getStatus().equals(POStatus.OPEN);
	}
	
	public boolean isPoMorePrintable(){
		BigDecimal comparedValue = new BigDecimal(10000);
		return editPO != null && editPO.getId() != null && editPO.getStatus().equals(POStatus.OPEN) && editPO.getTotalPrice().compareTo(comparedValue) == 1;
	}
	
	public boolean isPoClosable(){
		
		return editPO.getStatus().equals(POStatus.GR_PARTIAL) && editPO.getId() != null;
	}
	
}
