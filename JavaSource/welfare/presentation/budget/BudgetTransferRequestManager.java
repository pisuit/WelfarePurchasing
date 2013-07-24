/**
 * 
 */
package welfare.presentation.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.criterion.Projection;
import org.richfaces.component.html.HtmlTabPanel;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import welfare.persistent.controller.BudgetController;
import welfare.persistent.customtype.BudgetTransferStatus;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.budget.BudgetTransfer;
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
public class BudgetTransferRequestManager {
	private BudgetController budgetController;
	private Budget editBudget;						// ปีที่ต้องการโอน
	private BudgetTransfer editBudgetTransfer;			// ใบโอนงบประมาณ
	/* list panel */
	private int listBudgetYear;						// ปีที่ต้องการหางบประมาณ
	private ArrayList<BudgetTransfer> budgetTransferList = new ArrayList<BudgetTransfer>();	// รายการใบโอนงบประมาณ
	private TreeNode<BudgetItem> fromBudgetItemTree;	// Tree สำหรับแสดงรายการหมวดหลักและหมวดย่อย
	private TreeNode<BudgetItem> toBudgetItemTree;	// Tree สำหรับแสดงรายการหมวดหลักและหมวดย่อย
	/* sub-category panel */
	private ArrayList<BudgetItem> fromBudgetItemSubBudgetItemList = new ArrayList<BudgetItem>();	// รายการงบประมาณหมวดย่อยของหมวดหลักที่ถูกเลือกใน Tree
	private ArrayList<BudgetItem> toBudgetItemSubBudgetItemList = new ArrayList<BudgetItem>();	// รายการงบประมาณหมวดย่อยของหมวดหลักที่ถูกเลือกใน Tree
	/* edit panel */
	private BudgetItem editFromBudgetItem; 				// รายการงบประมาณที่จะถูกแก้ไข
	private BudgetItem editToBudgetItem; 				// รายการงบประมาณที่จะถูกแก้ไข
	private boolean isSaved = false;
	private String welcomeMsg = null;
	
	public BudgetTransferRequestManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		budgetController = new BudgetController();
		Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
		budgetController = new BudgetController();
		listBudgetYear = CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime());
		// create tree
		listBudgetTransfer();
		listBudgetItem();
		newTransfer();
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public Budget getEditBudget() {
		return editBudget;
	}

	public BudgetTransfer getEditBudgetTransfer() {
		return editBudgetTransfer;
	}

	public void setEditBudgetTransfer(BudgetTransfer editBudgetTransfer) {
		this.editBudgetTransfer = editBudgetTransfer;
	}

	public int getListBudgetYear() {
		return listBudgetYear;
	}

	public void setListBudgetYear(int listBudgetYear) {
		this.listBudgetYear = listBudgetYear;
	}

	public ArrayList<BudgetTransfer> getBudgetTransferList() {
		return budgetTransferList;
	}

	public TreeNode<BudgetItem> getFromBudgetItemTree() {
		return fromBudgetItemTree;
	}

	public TreeNode<BudgetItem> getToBudgetItemTree() {
		return toBudgetItemTree;
	}

	public ArrayList<BudgetItem> getFromBudgetItemSubBudgetItemList() {
		return fromBudgetItemSubBudgetItemList;
	}

	public ArrayList<BudgetItem> getToBudgetItemSubBudgetItemList() {
		return toBudgetItemSubBudgetItemList;
	}

	public BudgetItem getEditFromBudgetItem() {
		return editFromBudgetItem;
	}

	public BudgetItem getEditToBudgetItem() {
		return editToBudgetItem;
	}
	
	public void listBudgetTransfer(){
		try {
			budgetTransferList.clear();
			budgetTransferList = budgetController.getBudgetTransfers(listBudgetYear);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * แสดงรายการงบประมาณประจำปีตามปีที่้ต้องการ ถูกเรียกโดยปุ่มค้นหา
	 */
	public void listBudgetItem() {
		try {
			editBudget = budgetController.getBudget(listBudgetYear);
			if (editBudget == null) {
				if(fromBudgetItemSubBudgetItemList != null) {
					fromBudgetItemSubBudgetItemList.clear();
				}
				if(toBudgetItemSubBudgetItemList != null) {
					toBudgetItemSubBudgetItemList.clear();
				}
				fromBudgetItemSubBudgetItemList = new ArrayList<BudgetItem>();
				toBudgetItemSubBudgetItemList = new ArrayList<BudgetItem>();
				editFromBudgetItem = null;
				editToBudgetItem = null;
			}
			buildBudgetItemTree();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * สร้าง Tree สำหรับแสดงในหน้าจอ
	 */
	private void buildBudgetItemTree(){
		try {
			ArrayList<BudgetItem> fromBudgetItems = budgetController.getMainBudgetItems(listBudgetYear);
			ArrayList<BudgetItem> toBudgetItems = budgetController.getMainBudgetItems(listBudgetYear);
			fromBudgetItemTree = new TreeNodeImpl<BudgetItem>();
			fromBudgetItemTree = buildTree(fromBudgetItemTree, fromBudgetItems);
			toBudgetItemTree = new TreeNodeImpl<BudgetItem>();
			toBudgetItemTree = buildTree(toBudgetItemTree, toBudgetItems);
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

	public void fromBudgetItemTreeNodeSelection(NodeSelectedEvent event) {
		try {
			System.out.println("form is selected");
			HtmlTree tree = (HtmlTree) event.getComponent();
			editFromBudgetItem = (BudgetItem) tree.getRowData();
			//editFromBudgetItem = budgetController.getBudgetItem(editFromBudgetItem.getId());
			fromBudgetItemSubBudgetItemList = budgetController.getSubBudgetItems(editFromBudgetItem);
			System.out.println("From : "+editFromBudgetItem);
			System.out.println("To : "+editToBudgetItem);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void toBudgetItemTreeNodeSelection(NodeSelectedEvent event) {
		try {
			System.out.println("to is selected");
			HtmlTree tree = (HtmlTree) event.getComponent();
			editToBudgetItem = (BudgetItem) tree.getRowData();
			//editToBudgetItem = budgetController.getBudgetItem(editToBudgetItem.getId());
			toBudgetItemSubBudgetItemList = budgetController.getSubBudgetItems(editToBudgetItem);
			System.out.println("From : "+editFromBudgetItem);
			System.out.println("To : "+editToBudgetItem);
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void newTransfer(){
		editBudgetTransfer = new BudgetTransfer();
		editFromBudgetItem = new BudgetItem();
		editToBudgetItem = new BudgetItem();		
		editBudgetTransfer.setRequestDate(CalendarUtils.getDateTimeInstance().getTime());
		isSaved = false;
	}
	
	public void saveTransfer(){
		LogManager log = new LogManager();
		try {
			// อนุญาตให้มีการโอนไปที่หมวดย่อยสุดเท่านั้น
			/*if (editFromBudgetItem.getSubBudgetItems().size() > 0 || editToBudgetItem.getSubBudgetItems().size() > 0 ){
				throw new ControllerException("อนุญาตให้โอนงบเข้าไปและออกจากหมวดงบประมาณย่อยที่สุดเท่านั้น");
			}*/
			// อนุญาตให้มีการโอนภายใต้หมวดหลักเดียวกันเท่านั้น
			/*
			boolean isTransferable = false;
			if (editFromBudgetItem.getParentCategory() == null && editToBudgetItem.getParentCategory() == null ){
				isTransferable = true;
			} else {
				if (editFromBudgetItem.getParentCategory() != null && editToBudgetItem.getParentCategory() != null ) {
					if (editFromBudgetItem.getParentCategory().getId().equals(editToBudgetItem.getParentCategory().getId())){
						isTransferable = true;
					} else {
						isTransferable = false;
					}
				} else {
					isTransferable = false;
				}
			}
			if (!isTransferable) {
				throw new ControllerException("อนุญาตให้โอนงบภายใต้หมวดหลักเดียวกันเท่านั้น");
			}
			*/
			editBudgetTransfer.setFromBudgetItem(editFromBudgetItem);
			editBudgetTransfer.setOldFromBudgetItem(editFromBudgetItem);
			editBudgetTransfer.setToBudgetItem(editToBudgetItem);
			editBudgetTransfer.setOldToBudgetItem(editToBudgetItem);
			editBudgetTransfer.setApproveAmount(editBudgetTransfer.getRequestAmount());			
			if (editBudgetTransfer.getId() == null) {
				editBudgetTransfer.setOldAmount(editBudgetTransfer.getRequestAmount());
				editBudgetTransfer = budgetController.saveBudgetTransfer(editBudgetTransfer,true);
				log.recordCreateTransfer(editBudgetTransfer.getFormattedTransferNumber());
			} else {
				editBudgetTransfer = budgetController.saveBudgetTransfer(editBudgetTransfer,false);
				log.recordUpdateTransfer(editBudgetTransfer.getFormattedTransferNumber());
			}
			listBudgetTransfer();
			listBudgetItem();
			isSaved = true;
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public void transferRequestTableRowClicked() {
		
		editFromBudgetItem = editBudgetTransfer.getFromBudgetItem();
		editToBudgetItem = editBudgetTransfer.getToBudgetItem();
		System.out.println("invoked");
	}
	
	public void printTransfer(){
		// พิมพ์ใบขอโอนแล้วสร้างใบขอโอนใหม่
		newTransfer();
	}

	public boolean isAvailable(){
		return editBudget != null && editBudget.isAvailable();		
	}
	
	public boolean isEditable() {
		return isAvailable() && editBudgetTransfer.getStatus() == BudgetTransferStatus.REQUESTING && !isSaved();
	}
	
	public boolean isCreatable() {
		return isAvailable() && editBudgetTransfer != null && editBudgetTransfer.getId() != null;
	}
	
	public boolean isPrintable(){
		return isAvailable() && editBudgetTransfer != null && editBudgetTransfer.getId() != null && editBudgetTransfer.getTransferNumber() > 0 ;
	}
	
	public boolean isSaved(){
		return isSaved;
	}

}
