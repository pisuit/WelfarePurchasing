/**
 * 
 */
package welfare.presentation.purchasing;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

//import org.jboss.messaging.core.impl.postoffice.AddAllReplicatedDeliveriesMessage;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import welfare.persistent.controller.BudgetController;
import welfare.persistent.controller.MatController;
import welfare.persistent.controller.PRController;
import welfare.persistent.controller.SecurityController;
import welfare.persistent.customtype.GRType;
import welfare.persistent.customtype.PRType;
import welfare.persistent.customtype.Status;
import welfare.persistent.customtype.PRStatus;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.purchasing.PurchaseRequisition;
import welfare.persistent.domain.purchasing.PurchaseRequisitionItem;
import welfare.persistent.domain.security.BudgetAuthorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.MaterialGroup;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.presentation.common.WarehouseController;
import welfare.reportdata.PurchasingPRReportData;
import welfare.security.SecurityUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;
import welfare.utils.ReportUtils;

/**
 * @author Manop
 * 
 */
public class PurchasingPRManager {
	
	private String warehouseCode;
	private PRController controller = new PRController();
	private MatController matController = new MatController();
	private BudgetController budController = new BudgetController();
	private WarehouseController warehouseController = new WarehouseController();
	private SecurityController secController =  new SecurityController();
	private int budgetYear;
	private Budget budget;
	// pr list tabpanel
	private ArrayList<PurchaseRequisition> prList = new ArrayList<PurchaseRequisition>();
	private PurchaseRequisition editPR = new PurchaseRequisition();
	// pr header tabpanel
	private ArrayList<String> requisitionerNameList = new ArrayList<String>();
	private ArrayList<String> requisitionerPosList = new ArrayList<String>();
	private ArrayList<String> inspectorNameList = new ArrayList<String>();
	private ArrayList<String> inspectorPosList = new ArrayList<String>();
	private ArrayList<String> approverNameList = new ArrayList<String>();
	private ArrayList<String> approverPosList = new ArrayList<String>();
	// pr item panel
	private ArrayList<PurchaseRequisitionItem> prItemList = new ArrayList<PurchaseRequisitionItem>();
	private ArrayList<String> orderUnitList = new ArrayList<String>();
	private PurchaseRequisitionItem editPRItem = new PurchaseRequisitionItem();
	private ArrayList<SelectItem> budgetItemSelectItemList = new ArrayList<SelectItem>(); // combobox Item สำหรับให้เลือกหมวดงบประมาณ
	private Long selectedBudgetItemID; // ID ของหมวดงบประมาณที่ต้องการใช้
	private Long selectedBudgetItemIDMain;
	private ArrayList<SelectItem> materialGroupSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialGroupID;
	private ArrayList<SelectItem> materialSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialID;
	private BudgetItem selectedBudgetItem;
	private MaterialGroup selectedMaterialGroup;
	private Material selectedMaterial;
	private int fakeItemNo = 0;
	private boolean isNewPrItem;
	private boolean isPrSaved = false;
	private String welcomeMsg = null;
	private String selectedPRType = "-1";
	private ArrayList<SelectItem> prTypeSelectItemList = new ArrayList<SelectItem>();
	private boolean isPrCopyable = false;
	private BigDecimal currentReservedAmount = new BigDecimal("0.00");

	public PurchasingPRManager() {
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
		createBudgetItemSelectItemList();
		createMaterialGroupSelectItemList();
		createPRTypeSelectItemList();
		getSuggestionList();
		listPR();
		newPR();
	}
	
	public String getWelcomeMsg() {
		return welcomeMsg;
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
		createBudgetItemSelectItemList();
		createMaterialGroupSelectItemList();
		getSuggestionList();
		listPR();
		newPR();
	}

	public ArrayList<PurchaseRequisition> getPrList() {
		return prList;
	}

	public PurchaseRequisition getEditPR() {
		return editPR;
	}

	public void setEditPR(PurchaseRequisition editPurchaseRequisition) {
		this.editPR = editPurchaseRequisition;
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

	public ArrayList<String> getOrderUnitList() {
		return orderUnitList;
	}

	public ArrayList<PurchaseRequisitionItem> getPrItemList() {
		return prItemList;
	}

	public PurchaseRequisitionItem getEditPRItem() {
		return editPRItem;
	}

	public void setEditPRItem(PurchaseRequisitionItem editPRItem) {
		this.editPRItem = editPRItem;
	}

	public ArrayList<SelectItem> getBudgetItemSelectItemList() {
		return budgetItemSelectItemList;
	}

	public Long getSelectedBudgetItemID() {
		return selectedBudgetItemID;
	}

	public void setSelectedBudgetItemID(Long selectedBudgetItemID) {
		this.selectedBudgetItemID = selectedBudgetItemID;
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

	public Long getSelectedBudgetItemIDMain() {
		return selectedBudgetItemIDMain;
	}

	public void setSelectedBudgetItemIDMain(Long selectedBudgetItemIDMain) {
		this.selectedBudgetItemIDMain = selectedBudgetItemIDMain;
	}

	public ArrayList<SelectItem> getMaterialGroupSelectItemList() {
		return materialGroupSelectItemList;
	}

	public ArrayList<SelectItem> getMaterialSelectItemList() {
		return materialSelectItemList;
	}

	public String getSelectedPRType() {
		return selectedPRType;
	}

	public void setSelectedPRType(String selectedPRType) {
		this.selectedPRType = selectedPRType;
	}

	public ArrayList<SelectItem> getPrTypeSelectItemList() {
		return prTypeSelectItemList;
	}

	public void setPrTypeSelectItemList(ArrayList<SelectItem> prTypeSelectItemList) {
		this.prTypeSelectItemList = prTypeSelectItemList;
	}

	public BudgetItem getSelectedBudgetItem() {
		return selectedBudgetItem;
	}

	public void setSelectedBudgetItem(BudgetItem selectedBudgetItem) {
		this.selectedBudgetItem = selectedBudgetItem;
	}

	private void listPR(){
		try {
			prList.clear();
			prList =  controller.getPurchaseRequisitions(warehouseCode);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void getSuggestionList(){
		try {
			// ชื่อผู้ลงนามในใบแจ้ง
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
	
	public void prTableRowClicked(){
		try {
			editPR = controller.getPurchaseRequisition(editPR.getId());
			selectedBudgetItemIDMain = editPR.getPurchaseRequisitionItems().get(0).getBudgetItem().getId();
			selectedBudgetItem = editPR.getPurchaseRequisitionItems().get(0).getBudgetItem();
			prItemList.clear();
			prItemList.addAll(editPR.getPurchaseRequisitionItems());
			currentReservedAmount = editPR.getTotalPrice();
			selectedPRType = editPR.getPrType().getID();
			fakeItemNo = prItemList.size()+1;
			newPrItem();
			isPrCopyable = true;
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void copyPR(){
		PurchaseRequisition tempPR = editPR;
		tempPR.setId(null);
		tempPR.setReferenceDocNumber(null);
		tempPR.setPostingDate(CalendarUtils.getDateTimeInstance().getTime());
		tempPR.setTotalPrice(new BigDecimal("0.00"));
		tempPR.setStatus(PRStatus.OPEN);
		editPR = new PurchaseRequisition();
		editPR = tempPR;
		
		ArrayList<PurchaseRequisitionItem> tempPRItemList = new ArrayList<PurchaseRequisitionItem>();
		for(PurchaseRequisitionItem currentItem : prItemList){
			PurchaseRequisitionItem item = new PurchaseRequisitionItem();
			item = currentItem;
			item.setId(null);
			item.setQuantity(new BigDecimal("0.00"));
			item.setDeliveryDate(CalendarUtils.getDateTimeInstance().getTime());
			item.setBudgetReservedAmount(new BigDecimal("0.00"));
			item.setPurchaseRequisition(editPR);
			tempPRItemList.add(item);			
		}
		prItemList.clear();
		prItemList.addAll(tempPRItemList);
		selectedBudgetItem = prItemList.get(0).getBudgetItem();
		selectedBudgetItemIDMain = selectedBudgetItem.getId();
		isPrCopyable = false;
	}
	
	public void prTypeComboBoxSelected(){
		editPR.setPrType(PRType.find(selectedPRType));
	}
	
	public void createPRTypeSelectItemList(){
		SelectItem selectItem;
		prTypeSelectItemList.clear();
		prTypeSelectItemList.add(new SelectItem("-1","เลือกประเภทการแจ้งจัดหา"));
		for (PRType prType : PRType.values()) {
			selectItem = new SelectItem();
			selectItem.setValue(prType.getID());
			selectItem.setLabel(prType.getValue());
			prTypeSelectItemList.add(selectItem);
		}
	}
	
	public void savePR(){
		LogManager log = new LogManager();
		try {
			int prItemSize = 0;
			for (PurchaseRequisitionItem prItem : prItemList) {
				if (prItem.getStatus().equals(Status.NORMAL)){
					prItemSize++;
				}
			}
			if (prItemSize == 0) {
				throw new ControllerException("กรุณาใส่รายการที่ต้องการแจ้งจัดหาอย่างน้อย 1 รายการ");
			}
			if (editPR.getId() == null ){
				// กำหนดเลขที่ใบแจ้ง
				int nextPRNumber = controller.getNextPurchaseRequisitionNo(budgetYear);
				editPR.setPrNumber(nextPRNumber);
				editPR.setBudgetYear(budgetYear);
			}
			editPR.setPurchaseRequisitionItems(prItemList);
			
			if(!editPR.getPrType().equals(PRType.PR_MEDICAL)){
				if (selectedBudgetItem.isControlled() == true) {				
					if (editPR.getSumPrice().compareTo(selectedBudgetItem.getAvailableAmount().add(currentReservedAmount)) == 1) {
						throw new ControllerException("งบประมาณที่คงเหลืออยู่ไม่พอสำหรับการสร้างใบแจ้งจัดหา");
					}
				}
			} else {
				BudgetItem comparedBudgetItem = budController.getBudgetItem(prItemList.get(0).getBudgetItem().getId());
				if(editPR.getSumPrice().compareTo(comparedBudgetItem.getParentBudgetItem().getAvailableAmount().add(currentReservedAmount)) == 1){
					throw new ControllerException("งบประมาณที่คงเหลืออยู่ไม่พอสำหรับการสร้างใบแจ้งจัดหา");
				}
			}
			
			if (editPR.getId() == null) {				
				editPR = controller.savePurchaseRequisition(editPR);
				log.recordCreatePr(editPR.getFormattedPrNumber(), editPR.getTotalPrice());
			} else {
				editPR = controller.savePurchaseRequisition(editPR);
				log.recordUpdatePr(editPR.getFormattedPrNumber(), editPR.getTotalPrice());
			}
			//editPR = controller.getPurchaseRequisition(editPR.getId());
			listPR();
			prTableRowClicked();
			isPrSaved = true;
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		} catch (Exception e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		
	}
	
	public void deletePR(){
		LogManager log = new LogManager();
		try {
			editPR.setStatus(PRStatus.DELETED);
			controller.savePurchaseRequisition(editPR);
			log.recordDeletePr(editPR.getFormattedPrNumber());
			listPR();
			prTableRowClicked();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void newPR(){
		editPR = new PurchaseRequisition();
		editPR.setWarehouseCode(warehouseCode);
		editPR.setPostingDate(CalendarUtils.getDateTimeInstance().getTime());
		editPR.setStatus(PRStatus.OPEN);
		selectedBudgetItemIDMain = Long.valueOf(-1);
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedBudgetItem = null;
		selectedPRType = "-1";
		prItemList.clear();
		fakeItemNo = 0;
		isPrSaved = false;
		isPrCopyable = false;
		currentReservedAmount = new BigDecimal("0.00");
		newPrItem();
	}

	public void printPR(){
		LogManager log = new LogManager();
		try {
			System.out.println("printPR");
			
			JasperReport report = null;
			report = (JasperReport)JRLoader.loadObject(this.getClass().getResource("/welfare/reports/prForm.jasper"));
			//report = (JasperReport)JRLoader.loadObject("D:/prForm.jasper");
			
			ArrayList<PurchasingPRReportData> prReportList = new ArrayList<PurchasingPRReportData>();
			
			for(PurchaseRequisitionItem purchaseRequisitionItem : prItemList) {
				PurchasingPRReportData prReport = new PurchasingPRReportData();
				
				prReport.setItemNo(purchaseRequisitionItem.getItemNumber());
				prReport.setDeliveryDate(purchaseRequisitionItem.getDeliveryDate());				
				prReport.setQty(purchaseRequisitionItem.getQuantity());
				prReport.setUnit(purchaseRequisitionItem.getOrderUnit());
				prReport.setUnitPrice(purchaseRequisitionItem.getUnitPrice());
				
				String material = "";
				if (purchaseRequisitionItem.getMaterial() == null) {
					material = purchaseRequisitionItem.getOtherMaterial();
				} else {
					material = purchaseRequisitionItem.getMaterial().getDescription();
				}
				
				prReport.setMaterial(material);
				prReportList.add(prReport);
			}
			
			BudgetItem item = (BudgetItem) prItemList.get(0).getBudgetItem();
			
			HashMap prReportHashMap = new HashMap();
			prReportHashMap.put("prNo", editPR.getFormattedPrNumber());
			prReportHashMap.put("createdDate", editPR.getPostingDate());
			prReportHashMap.put("text", editPR.getReason());
			prReportHashMap.put("requisitionerName", editPR.getRequisitionerName());
			prReportHashMap.put("inspectorName", editPR.getInspectorName());
			prReportHashMap.put("approverName", editPR.getApproverName());
			prReportHashMap.put("requisitionerPos", editPR.getRequisitionerPos());
			prReportHashMap.put("inspectorPos", editPR.getInspectorPos());
			prReportHashMap.put("approverPos", editPR.getApproverPos());
			prReportHashMap.put("totalPrice", editPR.getTotalPrice());	
			prReportHashMap.put("LOGO", this.getClass().getResource("/welfare/reports/logo.gif"));
			prReportHashMap.put("logo",  this.getClass().getResource("/welfare/reports/aerologo.png"));
			
			if(!editPR.getPrType().equals(PRType.PR_MEDICAL)){
				prReportHashMap.put("budgetItem", item.getCategory());
				prReportHashMap.put("plannedBudget", item.getInitialAmount());
				prReportHashMap.put("reservedBudget", item.getReservedAmount());
				prReportHashMap.put("usedBudget", item.getExpensedAmount());
				prReportHashMap.put("remainBudget", item.getAvailableAmount());
				
			}
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, prReportHashMap, new JRBeanCollectionDataSource(prReportList));
			
			//JasperExportManager.exportReportToPdfFile(jasperPrint,"D:/testPR.pdf");			
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			ReportUtils.displayPdfReport(bytes);			
			
			log.recordPrintPr(editPR.getFormattedPrNumber());
			
		} catch (JRException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}	
	
	public void prItemTableRowClicked(){
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedBudgetItemID = editPRItem.getBudgetItem().getId();				
		if (editPRItem.getMaterial() != null){
		selectedMaterialGroupID = editPRItem.getMaterial().getMaterialGroup().getId();
		selectedMaterialID = editPRItem.getMaterial().getId();
		}
		materialGroupComboBoxSelected();
		isNewPrItem = false;	
	}
		
	public void savePrItem() {
		try {
			// ตรวจสอบว่ามีการเลือก material หรือ ใส่ other material หรือไม่
			if (editPRItem.getMaterial() == null && ( editPRItem.getOtherMaterial()== null || editPRItem.getOtherMaterial().trim().equals("")) ){
				throw new ControllerException("กรุณาเลือกวัสดุที่ต้องการจัดหา");
			}
			// ไม่ให้ใส่ทั้ง material และ othermaterial
			if (editPRItem.getMaterial() != null && (editPRItem.getOtherMaterial() != null && !editPRItem.getOtherMaterial().trim().equals("")) ){
				throw new ControllerException("กรุณาเลือกวัสดุ หรือ วัสดุอื่นๆอย่างใดอย่างหนึ่งเท่านั้น");
			}
			// ตรวจสอบว่ามีการเลือกหมดงบประมาณ หรือไม่
			if (editPRItem.getBudgetItem() == null){
				throw new ControllerException("กรุณาเลือกหมวดงบประมาณ");
			}
			// ตรวจสอบว่ามีรายการซ้ำหรือไม่
			for (PurchaseRequisitionItem prItem : prItemList) {
				System.out.println("editPrITem otherMaterial" + editPRItem.getOtherMaterial());
				System.out.println("prITem otherMaterial" + editPRItem.getOtherMaterial());
				if (editPRItem.getItemNumber() != prItem.getItemNumber()){
					if (editPRItem.getMaterial() != null && prItem.getMaterial() != null && editPRItem.getMaterial().getId().equals(prItem.getMaterial().getId())){
						System.out.println("Same material");
						throw new ControllerException("วัสดุ "+editPRItem.getMaterial()+" มีอยู่แล้วไม่สามารดำเนินการต่อได้");
					} else if (editPRItem.getOtherMaterial() != null && prItem.getOtherMaterial() != null 
							&& !editPRItem.getOtherMaterial().trim().equals("")
							&& !editPRItem.getOtherMaterial().trim().equals("")
							&& editPRItem.getOtherMaterial().equals(prItem.getOtherMaterial()) ) {
						System.out.println("Same othermaterial");
						throw new ControllerException("วัสดุ "+editPRItem.getOtherMaterial()+" มีอยู่แล้วไม่สามารดำเนินการต่อได้");
					}
				}
			}
			if (isNewPrItem){
				prItemList.add(editPRItem);
			}
			System.out.println("numberrrrrr"+editPRItem.getItemNumber());
			newPrItem();			
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deletePrItem() {
		if (editPRItem.getId() == null ){
			// ลบออกจาก prItemList
			int index = 0;
			for (PurchaseRequisitionItem item : prItemList) {
				if (item.getItemNumber() == editPRItem.getItemNumber()){
					prItemList.remove(index);
					break;
				}
				index++;
			}
		} else {
			editPRItem.setStatus(Status.DELETED);
		}
	}
	
	public void newPrItem(){
		editPRItem = new PurchaseRequisitionItem();
		if(!selectedPRType.equals(PRType.PR_MEDICAL.getID())){
			editPRItem.setBudgetItem(selectedBudgetItem);
		}		
		editPRItem.setPurchaseRequisition(editPR);
		editPRItem.setDeliveryDate(CalendarUtils.getDateTimeInstance().getTime());
		editPRItem.setItemNumber(fakeItemNo++);
		editPRItem.setStatus(Status.NORMAL);
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedBudgetItemID = Long.valueOf(-1);
		isNewPrItem = true;
	}
	
	public void budgetItemComboBoxSelected(){
		// find budgetItem 
		try {
			if (!selectedBudgetItemIDMain.equals(Long.valueOf(-1))) {
				selectedBudgetItem = budController.getBudgetItem(selectedBudgetItemIDMain);
				if (selectedBudgetItem.isAvailable() == false) {
					throw new ControllerException("งบประมาณถูกปิดการเบิกใช้");
				}
				if (selectedBudgetItem.isPurchasingBudget() == false) {
					throw new ControllerException("งบประมาณหมวดนี้ไม่สามารถใช้สร้างใบแจ้งจัดหาได้");
				}
				System.out.println("Budget Item : " + selectedBudgetItem.getCategory() );
				editPRItem.setBudgetItem(selectedBudgetItem);
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void prItemBudgetItemComboBoxSelected(){
		// find budgetItem 
		try {
			if (!selectedBudgetItemID.equals(Long.valueOf(-1))) {
				selectedBudgetItem = budController.getBudgetItem(selectedBudgetItemID);
				if (selectedBudgetItem.isAvailable() == false) {
					throw new ControllerException("งบประมาณถูกปิดการเบิกใช้");
				}
				if (selectedBudgetItem.isPurchasingBudget() == false) {
					throw new ControllerException("งบประมาณหมวดนี้ไม่สามารถใช้สร้างใบแจ้งจัดหาได้");
				}
				System.out.println("Budget Item : " + selectedBudgetItem.getCategory() );
				editPRItem.setBudgetItem(selectedBudgetItem);
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
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
	
	public void materialComboBoxSelected(){
		try {
			selectedMaterial = matController.getMaterial(selectedMaterialID);
			editPRItem.setMaterial(selectedMaterial);
			if (selectedMaterial != null ){
				editPRItem.setOrderUnit(selectedMaterial.getOrderUnit());
				if (!selectedPRType.equals(PRType.PR_NOPRICE.getID())){
					editPRItem.setUnitPrice(selectedMaterial.getOrderUnitPrice());
				}
			}
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

	public boolean isPrItemEditable(){
		return isPrEditable() && editPRItem != null && editPRItem.getStatus().equals(Status.NORMAL);
	}
	
	public boolean isPrItemDeletable(){
		return !isNewPrItem;
	}
	
	public boolean isPrItemCreatable(){
			return !isNewPrItem;
	}
	
	public boolean isPrEditable(){
		return budget != null && budget.isAvailable() && editPR != null && editPR.getStatus().equals(PRStatus.OPEN) && !isPrSaved && !selectedPRType.equals("-1");
	}
	
	public boolean isPrDeletable(){
		return isPrEditable() && editPR != null && editPR.getId() != null;
	}
	
	public boolean isPrPrintable(){
		return editPR != null && editPR.getId() != null && editPR.getStatus().equals(PRStatus.OPEN);
	}
	
	public boolean isPrCreatable(){
		return editPR != null && editPR.getId() != null;
	}
	
	public boolean isPriceInputTextDisabled(){
		return selectedPRType.equals(PRType.PR_NOPRICE.getID());
	}
	
	public boolean isPrCopyable(){
		return isPrCopyable;
	}
	
	public boolean isPrMedical(){
		return selectedPRType.equals(PRType.PR_MEDICAL.getID());
	}
}
