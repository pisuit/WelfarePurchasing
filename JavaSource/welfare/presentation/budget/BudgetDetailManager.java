/**
 * 
 */
package welfare.presentation.budget;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.richfaces.component.UITree;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import welfare.persistent.controller.BudgetController;
import welfare.persistent.controller.GRController;
import welfare.persistent.customtype.BudgetItemType;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.budget.BudgetExpense;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.budget.BudgetTransfer;
import welfare.persistent.domain.purchasing.GoodsReceipt;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.presentation.common.WarehouseController;
import welfare.reportdata.BudgetItemUsageData;
import welfare.reportdata.BudgetReportData;
import welfare.reportdata.BudgetTransferReportData;
import welfare.security.SecurityUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;
import welfare.utils.ReportUtils;

/**
 * @author Manop
 * 
 */
public class BudgetDetailManager {
	private BudgetController budgetController;
	private GRController grController;
	private Budget editBudget;						// งบประมาณประจำปี

	/* list panel */
	private int listBudgetYear;						// ปีที่ต้องการหางบประมาณ
	private TreeNode<BudgetItem> budgetItemTree;	// Tree สำหรับแสดงรายการหมวดหลักและหมวดย่อย
	/* sub-category panel */
	private ArrayList<BudgetItem> subBudgetItemList = new ArrayList<BudgetItem>();	// รายการงบประมาณหมวดย่อยของหมวดหลักที่ถูกเลือกใน Tree
	private BudgetItem parentBudgetItem;
	private BigDecimal totalInitialAmount;			// จำนวนเงินที่นำไปตั้งเป็นงบประมาณแล้ว
	/* edit panel */
	private BudgetItem editBudgetItem; 				// รายการงบประมาณที่จะถูกแก้ไข
	
	private ArrayList<BudgetItemUsageData> grList = new ArrayList<BudgetItemUsageData>();
	private ArrayList<BudgetItemUsageData> expenseList = new ArrayList<BudgetItemUsageData>();
	private String selectedView = "allitem";
	private Date fromDate = null;
	private Date toDate = null;
	private String welcomeMsg = null;
	private ArrayList<BudgetTransfer> transferList = new ArrayList<BudgetTransfer>();
	private BigDecimal sumTotalGR = new BigDecimal("0.00");
	private BigDecimal sumTotalExpense = new BigDecimal("0.00");
	private BigDecimal sumTotaltransfer = new BigDecimal("0.00");

	public BudgetDetailManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		grController = new GRController();
		budgetController = new BudgetController();
		Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
		listBudgetYear = CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime());
		// create tree
		listBudgetItem();
	}

	public ArrayList<BudgetTransfer> getTransferList() {
		return transferList;
	}

	public void setTransferList(ArrayList<BudgetTransfer> transferList) {
		this.transferList = transferList;
	}

	public BigDecimal getSumTotalGR() {
		return sumTotalGR;
	}

	public void setSumTotalGR(BigDecimal sumTotalGR) {
		this.sumTotalGR = sumTotalGR;
	}

	public BigDecimal getSumTotalExpense() {
		return sumTotalExpense;
	}

	public void setSumTotalExpense(BigDecimal sumTotalExpense) {
		this.sumTotalExpense = sumTotalExpense;
	}

	public BigDecimal getSumTotaltransfer() {
		return sumTotaltransfer;
	}

	public void setSumTotaltransfer(BigDecimal sumTotaltransfer) {
		this.sumTotaltransfer = sumTotaltransfer;
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public String getSelectedView() {
		return selectedView;
	}


	public void setSelectedView(String selectedView) {
		this.selectedView = selectedView;
	}


	public Date getFromDate() {
		return fromDate;
	}


	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}


	public Date getToDate() {
		return toDate;
	}


	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public ArrayList<BudgetItemUsageData> getGrList() {
		return grList;
	}


	public void setGrList(ArrayList<BudgetItemUsageData> grList) {
		this.grList = grList;
	}


	public ArrayList<BudgetItemUsageData> getExpenseList() {
		return expenseList;
	}


	public void setExpenseList(ArrayList<BudgetItemUsageData> expenseList) {
		this.expenseList = expenseList;
	}


	public int getListBudgetYear() {
		return listBudgetYear;
	}

	public void setListBudgetYear(int listBudgetYear) {
		this.listBudgetYear = listBudgetYear;
	}

	public Budget getEditBudget() {
		return editBudget;
	}

	public void setEditBudget(Budget editBudget) {
		this.editBudget = editBudget;
	}

	public TreeNode<BudgetItem> getBudgetItemTree() {
		return budgetItemTree;
	}
	
	public ArrayList<BudgetItem> getSubBudgetItemList(){
		if (subBudgetItemList == null) {
			subBudgetItemList = new ArrayList<BudgetItem>();
		}
		return subBudgetItemList;
	}
	
	public void setEditBudgetItem(BudgetItem editBudgetItem) {
		this.editBudgetItem = editBudgetItem;
	}

	public BudgetItem getEditBudgetItem() {
		return editBudgetItem;
	}

	public BudgetItem getParentBudgetItem() {
		return parentBudgetItem;
	}

	public BigDecimal getTotalInitialAmount() {
		return totalInitialAmount;
	}

	public void setTotalInitialAmount(BigDecimal totalInitialAmount) {
		this.totalInitialAmount = totalInitialAmount;
	}

	/**
	 * แสดงรายการงบประมาณประจำปีตามปีที่้ต้องการ ถูกเรียกโดยปุ่มค้นหา
	 */
	public void listBudgetItem() {
		try {
			editBudget = budgetController.getBudget(listBudgetYear);
			if (editBudget == null) {
				if(subBudgetItemList != null) {
					subBudgetItemList.clear();
				}
				subBudgetItemList = new ArrayList<BudgetItem>();
				parentBudgetItem = null;
				editBudgetItem = null;
			}
			buildBudgetItemTree();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void buildBudgetItemTree(){
		try {
			ArrayList<BudgetItem> budgetItems = budgetController.getMainBudgetItems(listBudgetYear);
			budgetItemTree = new TreeNodeImpl<BudgetItem>();
			budgetItemTree = buildTree(budgetItemTree, budgetItems);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private TreeNode<BudgetItem> buildTree(TreeNode<BudgetItem> rootNode, List<BudgetItem> budgetItemList) {
		TreeNodeImpl<BudgetItem> nodeItem;
		BudgetItem budgetItem;
		ArrayList<BudgetItem> budgetItems;
		try {
			for (int node = 0; node < budgetItemList.size(); node++) {
				nodeItem = new TreeNodeImpl<BudgetItem>();
				budgetItem = budgetItemList.get(node);
				//System.out.println(budgetItem);
				budgetItems = budgetController.getSubBudgetItems(budgetItem);
				nodeItem.setData(budgetItem);
				rootNode.addChild(budgetItem.getId(), nodeItem);
				buildTree(nodeItem, budgetItems);
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		return rootNode;
	}

	public void budgetItemTreeNodeSelection(NodeSelectedEvent event) {
		try {
			System.out.println("Node is selected");
			HtmlTree tree = (HtmlTree) event.getComponent();
			parentBudgetItem = (BudgetItem) tree.getRowData();
			editBudgetItem = parentBudgetItem;
			subBudgetItemList = budgetController.getSubBudgetItems(parentBudgetItem);
			sumTotalInitialAmount();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void sumTotalInitialAmount(){
		// หาจำนวนเงินที่ไปตั้งงบประมาณแล้ว
		totalInitialAmount = new BigDecimal("0.00");
		for (BudgetItem budgetItem : subBudgetItemList) {
			totalInitialAmount = totalInitialAmount.add(budgetItem.getInitialAmount());
		}
	}
	
	public boolean isEditable(){
		return editBudget != null && editBudget.isEditable() && editBudgetItem != null && editBudgetItem.isEditable();
	}

	public void saveBudgetItem(){
		System.out.println("Save item ");
		LogManager log = new LogManager();
		try {			
			if (editBudget != null){
				budgetController.saveBudgetItem(editBudgetItem);
				log.recordUpdateBudgetItem(editBudgetItem.getAccountCode());
				subBudgetItemList = budgetController.getSubBudgetItems(parentBudgetItem);
				buildBudgetItemTree();
			}			
		} catch (ControllerException e) {
			buildBudgetItemTree();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deleteBudgetItem(){
		LogManager log = new LogManager();
		try {
			if (editBudget != null){
				if((!editBudgetItem.getExpensedAmount().equals(new BigDecimal(0)) || (!editBudgetItem.getReservedAmount().equals(new BigDecimal(0))))){
					throw new ControllerException("ไม่สามารถลบหมวดงบประมาณที่มีการใช้งานแล้วได้");
				}
				ArrayList<BudgetItem> subItem = budgetController.getSubBudgetItems(editBudgetItem);
				if(subItem.size() != 0){
					throw new ControllerException("ไม่สามารถลบหมวดงบประมาณที่มีหมวดย่อยได้");
				}
				budgetController.deleteBudgetItem(editBudgetItem);
				log.recordDeleteBudgetItem(editBudgetItem.getAccountCode());
				newBudgetItem();
				buildBudgetItemTree();
			}
		} catch (ControllerException e) {
			buildBudgetItemTree();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deleteItem(){
		try {
			budgetController.deleteItem();
		}  catch (ControllerException e) {
			buildBudgetItemTree();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void saveSubBudgetItem(){
		System.out.println("Save sub-item ");
		LogManager log = new LogManager();
		try {
			if (editBudget != null){
				editBudgetItem.setBudget(editBudget);
				if (editBudgetItem.getParentBudgetItem().getBudgetLevel().equals(Constants.LEVEL_1)){
					editBudgetItem.setBudgetLevel(Constants.LEVEL_2);
				} else if (editBudgetItem.getParentBudgetItem().getBudgetLevel().equals(Constants.LEVEL_2)){
					editBudgetItem.setBudgetLevel(Constants.LEVEL_3);
				} else if (editBudgetItem.getParentBudgetItem().getBudgetLevel().equals(Constants.LEVEL_3)){
					editBudgetItem.setBudgetLevel(Constants.LEVEL_4);
				} else if (editBudgetItem.getParentBudgetItem().getBudgetLevel().equals(Constants.LEVEL_4)){
					throw new ControllerException("ไม่สามารถสร้างงบประมาณหมวดย่อยได้เกิน 4 ระดับ");
				}
				
				budgetController.saveBudgetItem(editBudgetItem);
				log.recordCreateSubBudgetItem(editBudgetItem.getAccountCode());
				subBudgetItemList = budgetController.getSubBudgetItems(parentBudgetItem);
				newBudgetItem();
				buildBudgetItemTree();
				sumTotalInitialAmount();
			}
		} catch (ControllerException e) {
			buildBudgetItemTree();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		
	}

	public void newBudgetItem(){
		if (parentBudgetItem != null){
			System.out.println(parentBudgetItem);
			editBudgetItem = new BudgetItem();
			editBudgetItem.setBudgetType(BudgetItemType.S);
			editBudgetItem.setParentBudgetItem(parentBudgetItem);
			editBudgetItem.setAccountCode(getNextAccountCode());
		}
	}
	
	private String getNextAccountCode() {
		String nextAccountCode = "";
		try {			
			nextAccountCode = budgetController.getNextAccountCodeForSubLevelBudget(editBudgetItem.getParentBudgetItem());						
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		System.out.println(nextAccountCode);
		return nextAccountCode;		
	}
	
	public void listExpenseHistory(){
		BigDecimal minus = new BigDecimal("-1.00");
		try {
			if (expenseList.size() != 0) expenseList.clear();
			if (grList.size() != 0) grList.clear();
			if (transferList.size() != 0) transferList.clear();
			sumTotalExpense = new BigDecimal("0.00");
			sumTotalGR = new BigDecimal("0.00");
			sumTotaltransfer = new BigDecimal("0.00");
			
			ArrayList<BudgetExpense> budgetExpense = budgetController.getAllBudgetExpenses(parentBudgetItem);
			BudgetItemUsageData expenseUsageData;
			for (BudgetExpense item : budgetExpense){
				expenseUsageData = new BudgetItemUsageData();
				expenseUsageData.setDate(item.getPostingDate());
				expenseUsageData.setTotalPrice(item.getAmount());
				expenseUsageData.setInvoiceNumber(item.getInvoiceNumber());
				expenseUsageData.setDetail(item.getDetail());
				sumTotalExpense = sumTotalExpense.add(item.getAmount());
				expenseList.add(expenseUsageData);
			}
			
			ArrayList<GoodsReceipt> goodReceipts = grController.getGoodReceiptsLinkedWithBudgetItem(parentBudgetItem);
			BudgetItemUsageData grUsageData;
			for (GoodsReceipt item : goodReceipts){
				grUsageData = new BudgetItemUsageData();
				grUsageData.setDate(item.getPostingDate());
				grUsageData.setTotalPrice(item.getTotalPrice());
				grUsageData.setNumber(item.getGrNumber());
				sumTotalGR = sumTotalGR.add(item.getTotalPrice());
				grList.add(grUsageData);
			}
			
			ArrayList<BudgetTransfer> budgetTransferList = budgetController.getBudgetTransferForBudgetItem(parentBudgetItem);
			BudgetTransfer transfer;
			for (BudgetTransfer budgetTransfer : budgetTransferList){
				transfer = new BudgetTransfer();
				transfer.setApproveDate(budgetTransfer.getApproveDate());
				transfer.setFromBudgetItem(budgetTransfer.getFromBudgetItem());
				transfer.setToBudgetItem(budgetTransfer.getToBudgetItem());
				transfer.setTransferNumber(budgetTransfer.getTransferNumber());
				if(budgetTransfer.getFromBudgetItem().getAccountCode().equals(parentBudgetItem.getAccountCode())) {
					transfer.setApproveAmount(budgetTransfer.getApproveAmount().multiply(minus));
				} else {
					transfer.setApproveAmount(budgetTransfer.getApproveAmount());
				}
				transferList.add(transfer);
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void listExpenseHistoryByDate(){
		BigDecimal minus = new BigDecimal("-1.00");
		try {
			if (expenseList.size() != 0) expenseList.clear();
			if (grList.size() != 0) grList.clear(); 
			if (transferList.size() != 0) transferList.clear();
			sumTotalExpense = new BigDecimal("0.00");
			sumTotalGR = new BigDecimal("0.00");
			sumTotaltransfer = new BigDecimal("0.00");
			
			ArrayList<BudgetExpense> budgetExpense = budgetController.getBudgetExpenseByDate(parentBudgetItem,fromDate,toDate);
			BudgetItemUsageData expenseUsageData;
			for (BudgetExpense item : budgetExpense){
				expenseUsageData = new BudgetItemUsageData();
				expenseUsageData.setDate(item.getPostingDate());
				expenseUsageData.setTotalPrice(item.getAmount());
				expenseUsageData.setInvoiceNumber(item.getInvoiceNumber());
				expenseUsageData.setDetail(item.getDetail());
				sumTotalExpense = sumTotalExpense.add(item.getAmount());
				expenseList.add(expenseUsageData);
			}
			
			ArrayList<GoodsReceipt> goodReceipts = grController.getGoodReceiptsLinkedWithBudgetItemByDate(parentBudgetItem,fromDate,toDate);
			BudgetItemUsageData grUsageData;
			for (GoodsReceipt item : goodReceipts){
				grUsageData = new BudgetItemUsageData();
				grUsageData.setDate(item.getPostingDate());
				grUsageData.setTotalPrice(item.getTotalPrice());
				grUsageData.setNumber(item.getGrNumber());
				sumTotalGR = sumTotalGR.add(item.getTotalPrice());
				grList.add(grUsageData);
			}
			
			ArrayList<BudgetTransfer> budgetTransferList = budgetController.getBudgetTransferForBudgetItemByDate(parentBudgetItem,fromDate,toDate);
			BudgetTransfer transfer;
			for (BudgetTransfer budgetTransfer : budgetTransferList){
				transfer = new BudgetTransfer();
				transfer.setApproveDate(budgetTransfer.getApproveDate());
				transfer.setFromBudgetItem(budgetTransfer.getFromBudgetItem());
				transfer.setToBudgetItem(budgetTransfer.getToBudgetItem());
				transfer.setTransferNumber(budgetTransfer.getTransferNumber());
				if(budgetTransfer.getFromBudgetItem().getAccountCode().equals(parentBudgetItem.getAccountCode())) {
					transfer.setApproveAmount(budgetTransfer.getApproveAmount().multiply(minus));
				} else {
					transfer.setApproveAmount(budgetTransfer.getApproveAmount());
				}
				transferList.add(transfer);
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void printBudgetTransferReport(){
		BigDecimal minus = new BigDecimal("-1.00");
		LogManager log = new LogManager();
		try {
			JasperReport report = null;
			Date printDate = CalendarUtils.getDateTimeInstance().getTime();
			report = (JasperReport)JRLoader.loadObject(this.getClass().getResource("/welfare/reports/budgetTransferReport.jasper"));
			
			ArrayList<BudgetTransferReportData> budgetTransferList = new ArrayList<BudgetTransferReportData>();
			BigDecimal sumTransferIn = new BigDecimal("0.00");
			BigDecimal sumTransferOut = new BigDecimal("0.00");
			BigDecimal sumTotalAmount = new BigDecimal("0.00");
			
			for(BudgetTransfer transfer : transferList){
				BudgetTransferReportData data = new BudgetTransferReportData();
				if (transfer.getFromBudgetItem().getAccountCode().equals(parentBudgetItem.getAccountCode())){
					sumTransferOut = sumTransferOut.add(transfer.getApproveAmount().multiply(minus));
					sumTotalAmount = sumTotalAmount.add(transfer.getApproveAmount());
					data.setTotalAmount(transfer.getApproveAmount());
					data.setTransferOutAmount(transfer.getApproveAmount().multiply(minus));
				} else {
					sumTransferIn = sumTransferIn.add(transfer.getApproveAmount());
					sumTotalAmount = sumTotalAmount.add(transfer.getApproveAmount());
					data.setTotalAmount(transfer.getApproveAmount());
					data.setTransferInAmount(transfer.getApproveAmount());
				}
				
				data.setApproveDate(transfer.getApproveDate());
				data.setCategoryFrom(transfer.getFromBudgetItem().getCategory());
				data.setCategoryTo(transfer.getToBudgetItem().getCategory());
				data.setTransferNumber(transfer.getTransferNumber());
				
				budgetTransferList.add(data);
			}
			HashMap budgetTransferReportHashMap = new HashMap();
			budgetTransferReportHashMap.put("fromDate", fromDate);
			budgetTransferReportHashMap.put("toDate", toDate);
			budgetTransferReportHashMap.put("budgetItem", parentBudgetItem.getCategory());
			budgetTransferReportHashMap.put("budgetYear", listBudgetYear);
			budgetTransferReportHashMap.put("printDate", printDate);		
			budgetTransferReportHashMap.put("sumTotalAmount", sumTotalAmount);
			budgetTransferReportHashMap.put("sumTransferIn", sumTransferIn);
			budgetTransferReportHashMap.put("sumTransferOut", sumTransferOut);
			budgetTransferReportHashMap.put("logo", this.getClass().getResource("/welfare/reports/aerologo.png"));
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, budgetTransferReportHashMap, new JRBeanCollectionDataSource(budgetTransferList));
			 
			 byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			 ReportUtils.displayPdfReport(bytes);
			 log.recordPrintBudgetTransferReport(parentBudgetItem.getCategory());
		} catch (JRException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean isExpenseListable(){
		return parentBudgetItem != null;
	}
	
	public void listHistory() {
		try {
			System.out.println(fromDate);
			System.out.println(toDate);
			System.out.println(selectedView);
			if (selectedView.equals("allitem")) {
				listExpenseHistory();
			} else {
				if (fromDate == null || toDate == null) throw new ControllerException("กรุณาระบุวันที่");
				listExpenseHistoryByDate();
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
}
