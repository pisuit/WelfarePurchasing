/**
 * 
 */
package welfare.presentation.purchasing;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.map.HashedMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import welfare.utils.ReportUtils;
import welfare.persistent.controller.BudgetController;
import welfare.persistent.controller.GRController;
import welfare.persistent.controller.LogController;
import welfare.persistent.controller.MatController;
import welfare.persistent.controller.POController;
import welfare.persistent.controller.PRController;
import welfare.persistent.controller.SecurityController;
import welfare.persistent.controller.VendorController;
import welfare.persistent.customtype.GRStatus;
import welfare.persistent.customtype.GRType;
import welfare.persistent.customtype.POType;
import welfare.persistent.customtype.Status;
import welfare.persistent.customtype.StorageLocation;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.purchasing.GoodsReceipt;
import welfare.persistent.domain.purchasing.GoodsReceiptItem;
import welfare.persistent.domain.purchasing.PurchaseOrder;
import welfare.persistent.domain.purchasing.PurchaseOrderItem;
import welfare.persistent.domain.purchasing.Vendor;
import welfare.persistent.domain.security.BudgetAuthorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.MaterialGroup;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.presentation.common.WarehouseController;
import welfare.reportdata.PurchasingGRReportData;
import welfare.security.SecurityUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;


/**
 * @author Manop
 *
 */
public class PurchasingGRManager {
	private GRController grController = new GRController();
	private POController poController = new POController();
	private PRController prController =  new PRController();
	private SecurityController secController = new SecurityController();
	private MatController matController = new MatController();
	private BudgetController budController = new BudgetController();
	private VendorController vendorController = new VendorController();
	private WarehouseController warehouseController = new WarehouseController();
	private int budgetYear;
	private Budget budget;
	private String warehouseCode;

	// PO List panel
	private List<GoodsReceipt> grList = new ArrayList<GoodsReceipt>();
	private GoodsReceipt editGR = new GoodsReceipt();
	
	// PO Header panel
	private ArrayList<SelectItem> poSelectItemList = new ArrayList<SelectItem>();
	private Long selectedPoID = Long.valueOf(-1);
	private ArrayList<SelectItem> grTypeSelectItemList = new ArrayList<SelectItem>();
	//private String selectedGRType = GRType.PO.getID();
	private String selectedGRType = "-1";
	private PurchaseOrder editPO;
	private ArrayList<String> recipientNameList = new ArrayList<String>();
	private ArrayList<String> recipientPosList = new ArrayList<String>();
	private ArrayList<String> entryNameList = new ArrayList<String>();
	private ArrayList<String> entryPosList = new ArrayList<String>();
	
	// PO Item panel
	private ArrayList<GoodsReceiptItem> grItemList = new ArrayList<GoodsReceiptItem>(); // รายการย่อยในใบสั่งซื้อ
	private ArrayList<String> orderUnitList = new ArrayList<String>();
	private GoodsReceiptItem editGRItem = new GoodsReceiptItem();
	private ArrayList<SelectItem> budgetItemSelectItemList = new ArrayList<SelectItem>(); // combobox Item สำหรับให้เลือกหมวดงบประมาณ
	private Long selectedBudgetItemID = Long.valueOf(-1); // ID ของหมวดงบประมาณที่ต้องการใช้
	private ArrayList<SelectItem> materialGroupSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialGroupID;
	private ArrayList<SelectItem> materialSelectItemList = new ArrayList<SelectItem>();
	private Long selectedMaterialID;
	private ArrayList<SelectItem> storageLocationSelectItemList = new ArrayList<SelectItem>();
	private String storageLocation = "-1";
	private BudgetItem selectedBudgetItem;
	private MaterialGroup selectedMaterialGroup;
	private Material selectedMaterial;
	private boolean isNewGrItem;
	private int fakeItemNo;
	private boolean isGrSaved = false;
	private boolean isGrItemSaved = true;
	private Vendor vendor;
	private BigDecimal zero = new BigDecimal("0.00");
	private String welcomeMsg = null;
	private boolean isItemAmountOrPriceChanged = false;
	
	public PurchasingGRManager() {
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
		createPOSelectItemList();
		createStorageLocationSelectItemList();
		createGRTypeSelectItemList();
		createMaterialGroupSelectItemList();
		createBudgetItemSelectItemList();
		getSuggestionList();
		listGR();
		newGR();
	}
	
	public void refreshData(){
		createPOSelectItemList();
		getSuggestionList();
		listGR();
		newGR();
	}
	
	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public GoodsReceipt getEditGR() {
		return editGR;
	}

	public void setEditGR(GoodsReceipt editPO) {
		this.editGR = editPO;
	}

	public Long getSelectedPoID() {
		return selectedPoID;
	}

	public void setSelectedPoID(Long selectedPrID) {
		this.selectedPoID = selectedPrID;
	}

	public String getSelectedGRType() {
		return selectedGRType;
	}

	public void setSelectedGRType(String selectedPOType) {
		this.selectedGRType = selectedPOType;
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

	public String getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}

	public ArrayList<SelectItem> getStorageLocationSelectItemList() {
		return storageLocationSelectItemList;
	}

	public List<GoodsReceipt> getGrList() {
		return grList;
	}

	public ArrayList<SelectItem> getPoSelectItemList() {
		return poSelectItemList;
	}

	public ArrayList<SelectItem> getGrTypeSelectItemList() {
		return grTypeSelectItemList;
	}

	public ArrayList<String> getRecipientNameList() {
		return recipientNameList;
	}

	public ArrayList<String> getRecipientPosList() {
		return recipientPosList;
	}

	public ArrayList<String> getEntryNameList() {
		return entryNameList;
	}

	public ArrayList<String> getEntryPosList() {
		return entryPosList;
	}


	public ArrayList<GoodsReceiptItem> getGrItemList() {
		return grItemList;
	}

	public GoodsReceiptItem getEditGRItem() {
		return editGRItem;
	}

	public PurchaseOrder getEditPO() {
		return editPO;
	}

	public void setEditGRItem(GoodsReceiptItem editPOItem) {
		this.editGRItem = editPOItem;
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
	
	private void listGR(){
		try {
			grList.clear();
			grList = grController.getGoodsReceipts(warehouseCode);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void grTableRowClicked(){
		try {
			editGR = grController.getGoodsReceipt(editGR.getId());
			selectedGRType = editGR.getGrType().getID();
			grItemList.clear();
			//grItemList.addAll(editGR.getGoodsReceiptItems());
			ArrayList<GoodsReceiptItem> grList = new ArrayList<GoodsReceiptItem>();
			grList.addAll(editGR.getGoodsReceiptItems());
			
			for (GoodsReceiptItem grItem : grList){
				GoodsReceiptItem item = new GoodsReceiptItem();
				item.setBudgetExpensedAmount(grItem.getBudgetExpensedAmount());
				item.setBudgetItem(grItem.getBudgetItem());
				item.setGoodsReceipt(grItem.getGoodsReceipt());
				item.setId(grItem.getId());
				item.setItemNumber(grItem.getItemNumber());
				item.setMaterial(grItem.getMaterial());
				item.setOldReceivedQty(grItem.getReceivedQty());
				item.setOrderQty(grItem.getOrderQty());
				item.setOrderUnitPrice(grItem.getOrderUnitPrice());
				item.setOtherMaterial(grItem.getOtherMaterial());
				item.setReceivedQty(grItem.getReceivedQty());
				item.setReceiveUnit(grItem.getReceiveUnit());
				item.setStatus(grItem.getStatus());
				item.setStockMovements(grItem.getStockMovements());
				item.setStorageLocation(grItem.getStorageLocation());
				item.setUnitPrice(grItem.getUnitPrice());
				item.setTotalprice(grItem.getTotalprice());
				item.setAvgPrice(grItem.getAvgPrice());
				item.setNetPrice(grItem.getNetPrice());
				item.setDiscountAmount(grItem.getDiscountAmount());
				grItemList.add(item);
			}
			fakeItemNo = grItemList.size()+1;
			System.out.println(editGR.getTotalPrice());
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void poComboBoxSelected(){
		try {
			/*
			if(selectedPOType == null || selectedPOType.equalsIgnoreCase("-1")) {
				selectedPrID = Long.valueOf(-1);
				throw new ControllerException("กรุณาเลือกประเภทการจัดซื้อ/จ้าง");
			}
			*/
			//newGR();
			editPO = poController.getPurchaseOrder(selectedPoID);
			if (editPO == null ) return;
			// สร้าง PO โดย copy ข้อมูลมาจาก PR
			editGR.setPurchaseOrder(editPO);
			editGR.setReason(editPO.getReason());
			editGR.setRecipientName(editPO.getReceiverName());
			GoodsReceiptItem grItem;
			grItemList.clear();
			for (PurchaseOrderItem poItem : editPO.getPurchaseOrderItems()) {
				if (poItem.getRemainQty().doubleValue() > 0){
					grItem = new GoodsReceiptItem();
					grItem.setGoodsReceipt(editGR);
					grItem.setItemNumber(poItem.getItemNumber());
					grItem.setMaterial(poItem.getMaterial());
					grItem.setOtherMaterial(poItem.getOtherMaterial());
					grItem.setBudgetItem(poItem.getBudgetItem());
					grItem.setReceivedQty(poItem.getRemainQty());
					grItem.setOrderQty(poItem.getRemainQty());
					grItem.setOrderUnitPrice(poItem.getUnitPrice());
					grItem.setReceiveUnit(poItem.getOrderUnit());
					grItem.setUnitPrice(poItem.getUnitPrice());
					grItem.setTotalprice(poItem.getRemainQty().multiply(poItem.getUnitPrice()));
					grItem.setNetPrice(poItem.getRemainQty().multiply(poItem.getUnitPrice()));
					grItem.setAvgPrice(poItem.getUnitPrice());
					grItemList.add(grItem);
				}
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void grTypeComboBoxSelected(){
		editGR.setGrType(GRType.find(selectedGRType));
	}
	
	public void saveGR(){
		LogManager log = new LogManager();
		try {
			// ตรวจว่าต้องมีการกำหนด StorageLocation
			/*for (GoodsReceiptItem grItem : grItemList) {
				if (grItem.getStorageLocation()== null ){
					throw new ControllerException("กรุณาระบุสถานที่จัดเก็บให้กับทุกรายการที่ต้องการรับเข้าคลัง");
				}
			}*/
			if (isItemAmountOrPriceChanged == true && !editGR.getTotalDiscountAmount().equals(zero)) {
				throw new ControllerException("กรุณากดปุ่มกระจายส่วนลดอีกครั้ง");
			}
			editGR.setGoodsReceiptItems(grItemList);
			if (editGR.getId() == null) {
				editGR.setBudgetYear(budgetYear);
				editGR = grController.saveGoodsReceipt(editGR);
				log.recordCreateGr(editGR.getFormattedGrNumber(), editGR.getTotalPrice());
			} else {
				editGR = grController.saveGoodsReceipt(editGR);
				log.recordUpdateGr(editGR.getFormattedGrNumber(), editGR.getTotalPrice());
			}
			createPOSelectItemList();
			listGR();
			grTableRowClicked();
			isGrSaved = true;
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deleteGR(){
		LogManager log = new LogManager();
		try {
			if (editGR.getId() == null ){
				throw new ControllerException("ไม่สามารถลบใบรับที่ยังไม่ได้บันทึกได้");
			}
			log.recordDeleteGr(editGR.getFormattedGrNumber());
			editGR =  grController.deleteGoodsReceipt(editGR);
			createPOSelectItemList();
			listGR();
			grTableRowClicked();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void closeGR(){
		LogManager log = new LogManager();
		try {
			if (editGR.getId() == null ){
				throw new ControllerException("ไม่สามารถปิดใบรับที่ยังไม่ได้บันทึกได้");
			}
			log.recordCloseGr(editGR.getFormattedGrNumber());
			editGR =  grController.closeGoodsReceipt(editGR);
			createPOSelectItemList();
			listGR();
			grTableRowClicked();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void distributeDiscount() {
		System.out.println("distributed discount");
		BigDecimal sumTotalPrice = new BigDecimal("0.00");
		BigDecimal maxTotalPrice = new BigDecimal("0.00");
		BigDecimal sumOfTotalDiscount = new BigDecimal("0.00");
		BigDecimal difference = new BigDecimal("0.00");
		int itemNumberOfmaxTotalPrice = 0;
		GoodsReceiptItem grItem;
		ArrayList<GoodsReceiptItem> tempGrItemList = new ArrayList<GoodsReceiptItem>();
		
		tempGrItemList.addAll(grItemList);
		grItemList.clear();
		
		for (GoodsReceiptItem item : tempGrItemList) {
			if (maxTotalPrice.compareTo(item.getTotalprice()) == -1) {
				maxTotalPrice = item.getTotalprice();
				itemNumberOfmaxTotalPrice = item.getItemNumber();
			}
			sumTotalPrice = sumTotalPrice.add(item.getTotalprice());
		}
		
		for (GoodsReceiptItem item : tempGrItemList) {
			BigDecimal discountAmount = new BigDecimal("0.00");
			BigDecimal netPrice = new BigDecimal("0.00");
			BigDecimal avgprice = new BigDecimal("0.00");
			
			discountAmount = (item.getTotalprice().divide(sumTotalPrice, 2, RoundingMode.HALF_UP)).multiply(editGR.getTotalDiscountAmount());
			netPrice = item.getTotalprice().subtract(discountAmount);
			avgprice = netPrice.divide(item.getReceivedQty(), 2, RoundingMode.HALF_UP);
			sumOfTotalDiscount = sumOfTotalDiscount.add(discountAmount);
									
			grItem = new GoodsReceiptItem();
			grItem.setBudgetItem(item.getBudgetItem());
			grItem.setGoodsReceipt(item.getGoodsReceipt());
			grItem.setId(item.getId());
			grItem.setItemNumber(item.getItemNumber());
			grItem.setMaterial(item.getMaterial());
			grItem.setOldReceivedQty(item.getOldReceivedQty());
			grItem.setOrderQty(item.getOrderQty());
			grItem.setOrderUnitPrice(item.getOrderUnitPrice());
			grItem.setOtherMaterial(item.getOtherMaterial());
			grItem.setReceivedQty(item.getReceivedQty());
			grItem.setReceiveUnit(item.getReceiveUnit());
			grItem.setStatus(item.getStatus());
			grItem.setStockMovements(item.getStockMovements());
			grItem.setStorageLocation(item.getStorageLocation());
			grItem.setUnitPrice(item.getUnitPrice());
			grItem.setTotalprice(item.getTotalprice());
			grItem.setDiscountAmount(discountAmount);
			grItem.setNetPrice(netPrice);
			grItem.setAvgPrice(avgprice);
			grItemList.add(grItem);
		}
		
		difference = editGR.getTotalDiscountAmount().subtract(sumOfTotalDiscount);
		if (difference.compareTo(zero) != 0) {
			recalculate(difference,itemNumberOfmaxTotalPrice);
		}
		isItemAmountOrPriceChanged = false;
	}
	
	private void recalculate(BigDecimal difference, int itemNumber) {
		ArrayList<GoodsReceiptItem> tempGrItemList = new ArrayList<GoodsReceiptItem>();
		GoodsReceiptItem grItem;
		
		tempGrItemList.addAll(grItemList);
		grItemList.clear();
		
		for (GoodsReceiptItem item : tempGrItemList) {
			grItem = new GoodsReceiptItem();
			grItem.setBudgetItem(item.getBudgetItem());
			grItem.setGoodsReceipt(item.getGoodsReceipt());
			grItem.setId(item.getId());
			grItem.setItemNumber(item.getItemNumber());
			grItem.setMaterial(item.getMaterial());
			grItem.setOldReceivedQty(item.getOldReceivedQty());
			grItem.setOrderQty(item.getOrderQty());
			grItem.setOrderUnitPrice(item.getOrderUnitPrice());
			grItem.setOtherMaterial(item.getOtherMaterial());
			grItem.setReceivedQty(item.getReceivedQty());
			grItem.setReceiveUnit(item.getReceiveUnit());
			grItem.setStatus(item.getStatus());
			grItem.setStockMovements(item.getStockMovements());
			grItem.setStorageLocation(item.getStorageLocation());
			grItem.setUnitPrice(item.getUnitPrice());
			grItem.setTotalprice(item.getTotalprice());
			if (item.getItemNumber() == itemNumber) {
				grItem.setDiscountAmount(item.getDiscountAmount().add(difference));
				grItem.setNetPrice(item.getNetPrice().subtract(difference));
				grItem.setAvgPrice((item.getNetPrice().subtract(difference).divide(item.getReceivedQty(), 2, RoundingMode.HALF_UP)));
			} else {
				grItem.setDiscountAmount(item.getDiscountAmount());
				grItem.setNetPrice(item.getNetPrice());
				grItem.setAvgPrice(item.getAvgPrice());
			}
			grItemList.add(grItem);
		}
	}
	
	public void printGR(){
		LogManager log = new LogManager();
		try {
			JasperReport report = null;
			report = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/welfare/reports/grForm.jasper"));
			//report = (JasperReport)JRLoader.loadObject("D:/grForm.jasper");
			
			ArrayList<PurchasingGRReportData> grReportList = new ArrayList<PurchasingGRReportData>();
						
			for (GoodsReceiptItem goodsReceiptItem : grItemList) {
				PurchasingGRReportData grReport = new PurchasingGRReportData();
				
				grReport.setItemNo(goodsReceiptItem.getItemNumber());
				grReport.setOrderQty(goodsReceiptItem.getOrderQty());
				grReport.setReceivedQty(goodsReceiptItem.getReceivedQty());
				grReport.setUnit(goodsReceiptItem.getReceiveUnit());
				grReport.setUnitPrice(goodsReceiptItem.getUnitPrice());
				
				String material = "";
				if (goodsReceiptItem.getMaterial() == null) {
					material = goodsReceiptItem.getOtherMaterial();
				} else {
					material = goodsReceiptItem.getMaterial().getDescription();
				}				
				grReport.setMaterial(material);
				grReportList.add(grReport);
			}
						
			vendor = poController.getVendorForReport(editGR.getPurchaseOrder().getId());
			String text = "";
			if (editGR.getStatus().toString().equals("GR-FULL")) {
				text = "รับเต็มจำนวน";
			} if (editGR.getStatus().toString().equals("GR-PAR")) {
				text = "รับไม่เต็มจำนวน";
			}
			
			HashMap grReportHashMap = new HashMap();
			grReportHashMap.put("grNo", editGR.getFormattedGrNumber());
			grReportHashMap.put("receivedDate", editGR.getReceivedDate());
			grReportHashMap.put("poNo", editGR.getPurchaseOrder().getFormattedPoNumber());
			grReportHashMap.put("status", editGR.getStatus().toString());
			grReportHashMap.put("invoiceNo", editGR.getInvoiceNumber());			
			if(vendor != null){
				grReportHashMap.put("vendor", vendor.getName());
			} else {
				grReportHashMap.put("vendor", editGR.getPurchaseOrder().getOtherVendor());
			}
			grReportHashMap.put("text", text);
			grReportHashMap.put("recipientName", editGR.getRecipientName());
			grReportHashMap.put("entryName", editGR.getEntryName());
			grReportHashMap.put("recipientPos", editGR.getRecipientPos());
			grReportHashMap.put("entryPos", editGR.getEntryPos());
			grReportHashMap.put("totalPrice", editGR.getTotalPrice());
			grReportHashMap.put("totalDiscount", editGR.getTotalDiscountAmount());
			grReportHashMap.put("LOGO", this.getClass().getResource("/welfare/reports/logo.gif"));
			grReportHashMap.put("logo", this.getClass().getResource("/welfare/reports/aerologo.png"));
					
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, grReportHashMap, new JRBeanCollectionDataSource(grReportList));
			
			//JasperExportManager.exportReportToPdfFile(jasperPrint,"D:/testGR.pdf");			
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			ReportUtils.displayPdfReport(bytes);
			log.recordPrintGr(editGR.getFormattedGrNumber());
			
		} catch (JRException e) {
			// TODO: handle exception
			e.printStackTrace();
			e.getMessage();
		}  catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void newGR(){
		editGR = new GoodsReceipt();
		editGR.setPostingDate(CalendarUtils.getDateTimeInstance().getTime());
		editGR.setWarehouseCode(warehouseCode);
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedPoID = Long.valueOf(-1);
		storageLocation = "-1";
		//selectedGRType = GRType.PO.getID();
		selectedGRType = "-1";
		grItemList.clear();
		fakeItemNo = 0;
		newGRItem();
		isGrSaved = false;
		selectedBudgetItem = null;
		selectedBudgetItemID = Long.valueOf(-1);
	}
	
	public void grItemTableRowClicked(){
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedMaterialGroup = null;
		selectedMaterial = null;
		selectedBudgetItem = editGRItem.getBudgetItem();
		if (editGRItem.getMaterial() != null) {
			selectedMaterialGroupID = editGRItem.getMaterial().getMaterialGroup().getId();
			selectedMaterialID = editGRItem.getMaterial().getId();
			selectedMaterialGroup = editGRItem.getMaterial().getMaterialGroup();
			selectedMaterial = editGRItem.getMaterial();
		}
		createMaterialSelectItem();
		editGRItem.setOldReceivedQty(editGRItem.getReceivedQty());
		editGRItem.setMaterial(selectedMaterial);				
		/*if (selectedMaterial != null ){
			editGRItem.setReceiveUnit(selectedMaterial.getOrderUnit());
			editGRItem.setUnitPrice(selectedMaterial.getOrderUnitPrice());
		}*/
		isNewGrItem = false;
		isGrItemSaved = false;
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
				editGRItem.setBudgetItem(selectedBudgetItem);
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void saveGRItem(){
			try {
				/*editGRItem.setMaterial(selectedMaterial);				
				if (selectedMaterial != null ){
					editGRItem.setReceiveUnit(selectedMaterial.getOrderUnit());
					editGRItem.setUnitPrice(selectedMaterial.getOrderUnitPrice());					
				}*/	
				if (editGRItem.getBudgetItem() == null) {
					throw new ControllerException("กรุณาเลือกหมวดงบประมาณ");
				}
				if (selectedGRType.equalsIgnoreCase(GRType.PO.getID())) {
					if (editGRItem.getReceivedQty().compareTo(editGRItem.getOrderQty()) == 1) {
						editGRItem.setReceivedQty(editGRItem.getOrderQty());
						throw new ControllerException("ไม่สามารถรับของเกินจำนวนที่สั่งซื้อได้");
					}
					if (!editGRItem.getOrderUnitPrice().equals(zero)){
						if (editGRItem.getUnitPrice().compareTo(editGRItem.getOrderUnitPrice()) == 1) {
							editGRItem.setUnitPrice(editGRItem.getOrderUnitPrice());
							throw new ControllerException("ไม่สามารถใส่ราคาต่อหน่วยเกินราคาที่สั่งซื้อได้");
						}
					}
				}
				// ตรวจสอบว่ามีการเลือก material หรือ ใส่ other material หรือไม่
				if (editGRItem.getMaterial() == null && ( editGRItem.getOtherMaterial()== null || editGRItem.getOtherMaterial().trim().equals("")) ){
					throw new ControllerException("กรุณาเลือกวัสดุที่ต้องการรับ");
				}
				// ไม่ให้ใส่ทั้ง material และ othermaterial
				if (editGRItem.getMaterial() != null && (editGRItem.getOtherMaterial() != null && !editGRItem.getOtherMaterial().trim().equals("")) ){
					throw new ControllerException("กรุณาเลือกวัสดุ หรือ วัสดุอื่นๆอย่างใดอย่างหนึ่งเท่านั้น");
				}
				// ตรวจสอบว่ามีรายการซ้ำหรือไม่
				for (GoodsReceiptItem grItem : grItemList) {
					System.out.println("editPrITem otherMaterial" + editGRItem.getOtherMaterial());
					System.out.println("prITem otherMaterial" + editGRItem.getOtherMaterial());
					if (editGRItem.getItemNumber() != grItem.getItemNumber()){
						if (editGRItem.getMaterial() != null && grItem.getMaterial() != null && editGRItem.getMaterial().getId().equals(grItem.getMaterial().getId())){
							System.out.println("Same material");
							throw new ControllerException("วัสดุ "+editGRItem.getMaterial()+" มีอยู่แล้วไม่สามารดำเนินการต่อได้");
						} else if (editGRItem.getOtherMaterial() != null && grItem.getOtherMaterial() != null 
								&& !editGRItem.getOtherMaterial().trim().equals("")
								&& !editGRItem.getOtherMaterial().trim().equals("")
								&& editGRItem.getOtherMaterial().equals(grItem.getOtherMaterial()) ) {
							System.out.println("Same othermaterial");
							throw new ControllerException("วัสดุ "+editGRItem.getOtherMaterial()+" มีอยู่แล้วไม่สามารดำเนินการต่อได้");
						}
					}
				}
				/*if (storageLocation.equals("-1")){
					throw new ControllerException("กรุณาเลือกสถานที่จัดเก็บ");
				}*/
				editGRItem.setStorageLocation(StorageLocation.find(storageLocation));
				editGRItem.setTotalprice(editGRItem.getReceivedQty().multiply(editGRItem.getUnitPrice()));
				
				if (editGRItem.getDiscountAmount().compareTo(zero) != 0) {
					editGRItem.setNetPrice(editGRItem.getTotalprice().subtract(editGRItem.getDiscountAmount()));
					editGRItem.setAvgPrice(editGRItem.getNetPrice().divide(editGRItem.getReceivedQty(), 2 ,RoundingMode.HALF_UP));
				} else {
					editGRItem.setNetPrice(editGRItem.getTotalprice());
					editGRItem.setAvgPrice(editGRItem.getUnitPrice());
				}
				if (isNewGrItem) {
					grItemList.add(editGRItem);
				}
				if ((editGRItem.getOrderQty().compareTo(editGRItem.getReceivedQty()) != 0) || (editGRItem.getOrderUnitPrice().compareTo(editGRItem.getUnitPrice()) != 0)) {
					isItemAmountOrPriceChanged = true;
					System.out.println("true");
				} else {
					isItemAmountOrPriceChanged = false;
					System.out.println("false");
				}
				newGRItem();
			} catch (ControllerException e) {
				e.printStackTrace();
				FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			}
		/*if (isNewGrItem) {
			grItemList.add(editGRItem);
		}*/
	}
	
	public void deleteGRItem(){
		if (editGRItem.getId() == null){
			// remove from list
			int index = 0;
			for (GoodsReceiptItem grItem : grItemList) {
				if (grItem.getItemNumber() == editGRItem.getItemNumber()){
					grItemList.remove(index);
					break;
				}
				index++;
			}
		} else {
			editGRItem.setStatus(Status.DELETED);
		}
	}
	
	public void newGRItem(){
		editGRItem = new GoodsReceiptItem();
		if (!selectedBudgetItemID.equals(Long.valueOf(-1))) {
			editGRItem.setBudgetItem(selectedBudgetItem);
		}
		editGRItem.setGoodsReceipt(editGR);
		editGRItem.setItemNumber(fakeItemNo++);
		editGRItem.setStatus(Status.NORMAL);
		selectedMaterialGroupID = Long.valueOf(-1);
		selectedMaterialID = Long.valueOf(-1);
		selectedBudgetItemID = Long.valueOf(-1);
		selectedMaterial = null;
		selectedMaterialGroup = null;
		isNewGrItem = true;
		isGrItemSaved = true;
	}
	
	public void materialGroupComboBoxSelected(){
		try {
			selectedMaterialGroup = matController.getMaterialGroup(selectedMaterialGroupID);
			//editGRItem.setOtherMaterial("");
			createMaterialSelectItem();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void materialComboBoxSelected(){
		try {
			selectedMaterial = matController.getMaterial(selectedMaterialID);
			editGRItem.setMaterial(selectedMaterial);
			if (selectedMaterial != null ){
				editGRItem.setReceiveUnit(selectedMaterial.getOrderUnit());
				editGRItem.setUnitPrice(selectedMaterial.getOrderUnitPrice());
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void createGRTypeSelectItemList(){
		SelectItem selectItem;
		grTypeSelectItemList.clear();
		grTypeSelectItemList.add(new SelectItem("-1","เลือกประเภทการรับวัสดุ"));
		for (GRType grType : GRType.values()) {
			selectItem = new SelectItem();
			selectItem.setValue(grType.getID());
			selectItem.setLabel(grType.getValue());
			grTypeSelectItemList.add(selectItem);
		}
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
	
	private void createPOSelectItemList(){
		try {
			poSelectItemList.clear();
			ArrayList<PurchaseOrder> openPrList = poController.getOpenPurchaseOrders(warehouseCode);
			poSelectItemList.add(new SelectItem(Long.valueOf(-1),"เลือกใบสั่งซื้อ"));
			SelectItem selectItem;
			for (PurchaseOrder po : openPrList) {
				selectItem = new SelectItem();
				selectItem.setValue(po.getId());
				selectItem.setLabel(po.getFormattedPoNumber());
				poSelectItemList.add(selectItem);
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
			recipientNameList.clear();
			recipientNameList = grController.getRecipientNames(warehouseCode);
			recipientPosList.clear();
			recipientPosList = grController.getRecipientPOs(warehouseCode);
			entryNameList.clear();
			entryNameList = grController.getEntryNames(warehouseCode);
			entryPosList.clear();
			entryPosList = grController.getEntryPos(warehouseCode);
			// หน่วยซื้อ
			orderUnitList = matController.getOrderUnits(warehouseCode);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public boolean isGrItemNewable(){
		//return isGrEditable() && editGR.getPurchaseOrder() != null;
		return isGrEditable() && !isGrItemSaved && (isGrNoPo() || !selectedGRType.equals("-1"));
	}
	
	public boolean isGrItemEditable(){
		return isGrEditable() && editGRItem != null && editGRItem.getStatus().equals(Status.NORMAL) && (isGrNoPo() || !selectedGRType.equals("-1"));
	}
	
	public boolean isGrItemDeletable(){
		//return isGrItemEditable() && !isNewGrItem && editGR.getPurchaseOrder() != null;
		return isGrEditable() && !isGrItemSaved && (isGrNoPo() || !selectedGRType.equals("-1"));
	}
	
	public boolean isGrEditable(){
		return budget != null && budget.isAvailable() && editGR != null && editGR.isEditable() && !selectedGRType.equals("-1");
	}
	
	public boolean isGrDeletable(){
		return isGrEditable() && editGR.getId() != null;
	}
	
	public boolean isGrPrintable(){
		return editGR != null && editGR.getId() != null && !(editGR.getStatus().equals(GRStatus.CLOSED) || editGR.getStatus().equals(GRStatus.DELETED));
	}
	
	public boolean isGrClosable(){
		return isGrEditable() && editGR.getId() != null;
	}
	
	public boolean isGrSavable(){
		return isGrEditable() && editGR != null && !isGrSaved;
	}
	
	public boolean isGrCreatable(){
		return editGR != null && editGR.getId() != null;
	}
	
	public boolean isItemDiscountable(){
		
		return editGR.getTotalDiscountAmount().compareTo(zero) == 0;
	}
	
	public boolean isGrNoPo() {
		return selectedGRType.equalsIgnoreCase(GRType.NOPO.getID());
	}
	
}
