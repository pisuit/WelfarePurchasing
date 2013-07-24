/**
 * 
 */
package welfare.presentation.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import welfare.persistent.controller.BudgetController;
import welfare.persistent.controller.VendorController;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.budget.BudgetExpense;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.purchasing.Vendor;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.security.SecurityUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

/**
 * @author Manop
 *
 */
public class BudgetExpenseManager {
	private BudgetController budgetController;
	private VendorController vendorController;
	private Budget editBudget;						// งบประมาณประจำปี
	
	/* list panel */
	private int listBudgetYear;						// ปีที่ต้องการหางบประมาณ
	private TreeNode<BudgetItem> budgetItemTree;	// Tree สำหรับแสดงรายการหมวดหลักและหมวดย่อย
	private ArrayList<BudgetExpense> expenseList = new ArrayList<BudgetExpense>();
	/* sub-category panel */
	private ArrayList<BudgetItem> subBudgetItemList = new ArrayList<BudgetItem>();	// รายการงบประมาณหมวดย่อยของหมวดหลักที่ถูกเลือกใน Tree
	/* edit panel */
	private BudgetItem editBudgetItem; 				// รายการงบประมาณที่จะถูกแก้ไข
	private BudgetItem tempBudgetItem;
	private BudgetExpense editBudgetExpense;			// ค่าใข้จ่ายที่ต้องการแก้ไข
	private ArrayList<SelectItem> vendorSelectItemList = new ArrayList<SelectItem>();
	private Long selectedVendorID ;
	private Vendor editVendor;
	private BigDecimal oldAmount = new BigDecimal("0.00");
	private Date fromDate = null;
	private Date toDate = null;
	private String selectedView = "today";
	private String welcomeMsg = null;
	
	public BudgetExpenseManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		budgetController = new BudgetController();
		vendorController = new VendorController();
		Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
		budgetController = new BudgetController();
		listBudgetYear = CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime());
		// create tree
		listBudgetItem();
		buildVendorSelectItemList();
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
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

	public String getSelectedView() {
		return selectedView;
	}

	public void setSelectedView(String selectedView) {
		this.selectedView = selectedView;
	}

	public ArrayList<BudgetExpense> getExpenseList() {
		return expenseList;
	}

	public void setExpenseList(ArrayList<BudgetExpense> expenseList) {
		this.expenseList = expenseList;
	}

	public TreeNode<BudgetItem> getBudgetItemTree() {
		return budgetItemTree;
	}

	public void setBudgetItemTree(TreeNode<BudgetItem> budgetItemTree) {
		this.budgetItemTree = budgetItemTree;
	}

	public ArrayList<BudgetItem> getSubBudgetItemList() {
		return subBudgetItemList;
	}

	public Budget getEditBudget() {
		return editBudget;
	}

	public BudgetItem getEditBudgetItem() {
		return editBudgetItem;
	}

	public BudgetExpense getEditBudgetExpense() {
		return editBudgetExpense;
	}

	public void setEditBudgetExpense(BudgetExpense editBudgetExpense) {
		this.editBudgetExpense = editBudgetExpense;
	}

	public ArrayList<SelectItem> getVendorSelectItemList() {
		return vendorSelectItemList;
	}

	public Long getSelectedVendorID() {
		return selectedVendorID;
	}

	public void setSelectedVendorID(Long selectedVendorID) {
		this.selectedVendorID = selectedVendorID;
	}

	/**
	 * แสดงรายการงบประมาณประจำปีตามปีที่้ต้องการ ถูกเรียกโดยปุ่มค้นหา
	 */
	public void listBudgetItem() {
		try {
			editBudget = budgetController.getBudget(listBudgetYear);
			/*if (editBudget == null) {
				if(subBudgetItemList != null) {
					subBudgetItemList.clear();
				}*/
				//subBudgetItemList = new ArrayList<BudgetItem>();
			if (editBudget == null) {
				if (expenseList != null) {
					expenseList.clear();
				}
				expenseList = new ArrayList<BudgetExpense>();
				editBudgetItem = null;
			}
			buildBudgetItemTree();
			newExpense();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void buildVendorSelectItemList() {
		try {
			editBudget = budgetController.getBudget(listBudgetYear);
			vendorSelectItemList.clear();
			ArrayList<Vendor> vendorList = vendorController.getActiveVendors();
			SelectItem selectItem;
			for (Vendor budgetItem : vendorList) {
				selectItem = new SelectItem();
				selectItem.setValue(budgetItem.getId());
				selectItem.setLabel(budgetItem.getName());
				vendorSelectItemList.add(selectItem);
			}
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
				if (budgetItem.isExpenseEntry()){
					budgetItems = budgetController.getSubBudgetItems(budgetItem);
					nodeItem.setData(budgetItem);
					rootNode.addChild(budgetItem.getId(), nodeItem);
					buildTree(nodeItem, budgetItems);
				}
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
		return rootNode;
	}

	/*public void budgetItemTreeNodeSelection(NodeSelectedEvent event) {
		try {
			System.out.println("Node is selected");
			HtmlTree tree = (HtmlTree) event.getComponent();
			editBudgetItem = (BudgetItem) tree.getRowData();
			subBudgetItemList = budgetController.getSubBudgetItems(editBudgetItem);
			newExpense();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}*/

	public void budgetItemTreeNodeSelection(NodeSelectedEvent event) {
		try {
			System.out.println("===========================================================");
			System.out.println(selectedView);
			System.out.println(fromDate);
			System.out.println(toDate);
		if (selectedView.equals("selectday")) {
			if (fromDate == null || toDate == null) throw new ControllerException("กรุณาระบุวันที่");
		}
			System.out.println("Node is selected");
			HtmlTree tree = (HtmlTree) event.getComponent();
			editBudgetItem = (BudgetItem) tree.getRowData();
			tempBudgetItem = editBudgetItem;
			//subBudgetItemList = budgetController.getSubBudgetItems(editBudgetItem);
			//expenseList = budgetController.getBudgetExpenses(editBudgetItem);
			if (selectedView.equals("today")){				
				listBudgetExpense();
			} else {
				listBudgetExpenseByDate();
			}
			//listBudgetExpense();
			newExpense();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void listBudgetExpense(){
		try {
			if (expenseList.size() != 0) expenseList.clear();
			Date date = CalendarUtils.getDateTimeInstance().getTime();
			expenseList = budgetController.getBudgetExpenses(editBudgetItem,date);
			editBudgetItem = budgetController.getBudgetItem(tempBudgetItem.getId());		
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	private void listBudgetExpenseByDate() {
		try {
			if (expenseList.size() != 0) expenseList.clear();
			expenseList = budgetController.getBudgetExpenseByDate(editBudgetItem, fromDate, toDate);
			editBudgetItem = budgetController.getBudgetItem(tempBudgetItem.getId());	
			} catch (ControllerException e) {
				FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
				e.printStackTrace();
			}
	}
	
	public void budgetExpenseTableRowClicked() {
		try {
			if (editBudgetExpense.getVendor() != null) {
				selectedVendorID = editBudgetExpense.getVendor().getId();
				editVendor = budgetController.getVendor(selectedVendorID);
			}				
			oldAmount = editBudgetExpense.getAmount();

		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
			
	}
	
	public void saveExpense(){
		LogManager log = new LogManager();
		try {
			// ยังไม่แน่นอน
			/*if (editBudgetItem.getSubBudgetItems().size() > 0 ) {
				throw new ControllerException("ไม่สามารถบันทึกค่าใช้จ่ายในหมวดงบประมาณที่มีหมวดย่อยได้");
			}*/
			if (editBudgetItem.isAvailable() == false) {
				throw new ControllerException("งบประมาณถูกปิดการเบิกใช้");
			}
			if (editBudgetItem.isExpenseEntry() != true) {
				throw new ControllerException("ไม่สามารถบันทึกค่าใช้จ่ายในงบประมาณหมวดนี้ได้");
			}
//			if (editVendor.getId() == null || editBudgetExpense.getAmount().equals(new BigDecimal("0.00")) || editBudgetExpense.getDetail() == "" || editBudgetExpense.getInvoiceNumber() == "") {
//				throw new ControllerException("กรุณาใส่ข้อมูลให้ครบถ้วน");
//			}
			if (editBudgetExpense.getAmount().equals(new BigDecimal("0.00")) || editBudgetExpense.getDetail() == "" ) {
				throw new ControllerException("กรุณาใส่รายละเอียดและจำนวนเงิน");
			}
			if (editBudgetItem.isControlled() == true) {
				if (editBudgetItem.getAvailableAmount().compareTo(editBudgetExpense.getAmount()) == -1) {
					throw new ControllerException("งบประมาณที่คงเหลืออยู่ไม่พอสำหรับการบันทึกค่าใช้จ่าย");
				}
			}
			if (editBudgetExpense.getId() == null) {
				editBudgetExpense.setBudgetItem(editBudgetItem);				
				editBudgetExpense.setPostingDate(CalendarUtils.getDateTimeInstance().getTime());
			}
			if (editVendor.getId() != null) {
			editBudgetExpense.setVendor(editVendor);
			}
			if (editBudgetExpense.getId() != null) {
				budgetController.saveBudgetExpenses(editBudgetExpense,oldAmount);
				log.recordUpdateBudgetExpense(editBudgetExpense.getBudgetItem().getAccountCode(), editBudgetExpense.getDetail());
			} else {
				budgetController.saveBudgetExpenses(editBudgetExpense,oldAmount);
				log.recordBudgetExpense(editBudgetExpense.getBudgetItem().getAccountCode(), editBudgetExpense.getDetail());
			}
			editBudgetExpense = new BudgetExpense();
			selectedVendorID = null;
			listBudgetExpense();
			listBudgetItem();
			newExpense();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void deleteExpense() {
		LogManager log = new LogManager();
		try {
			log.recordDeleteBudgetExpense(editBudgetExpense.getBudgetItem().getAccountCode(), editBudgetExpense.getInvoiceNumber());
			budgetController.deleteBudgetExpense(editBudgetExpense);
			editBudgetExpense = new BudgetExpense();
			selectedVendorID = null;
			listBudgetExpense();
			listBudgetItem();
			newExpense();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	} 
	
	public void newExpense(){
		editBudgetExpense = new BudgetExpense();
		editVendor = new Vendor();
		editBudgetExpense.setPostingDate(CalendarUtils.getDateTimeInstance().getTime());
		selectedVendorID = null;
	}
	
	public void vendorComboboxSelected(){
		try {
			editVendor = budgetController.getVendor(selectedVendorID);
		} catch (ControllerException e) {
			e.printStackTrace();
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
		}
	}
		
	public boolean isEditable(){
		return editBudget != null && editBudget.isAvailable() && editBudgetItem != null && editBudgetItem.isAvailable();
	}
	
	public boolean isAvailable(){
		return editBudget != null && editBudget.isAvailable() && editBudgetItem.isAvailable();
	}
	
	public boolean isNewable(){
		return editBudgetExpense.getId() != null;
	}
	
	public boolean isDateRendered(){
		if (selectedView.equals("selectday")){
			return false;
		} else return true;
	}

}
