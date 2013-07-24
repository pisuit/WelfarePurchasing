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
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.search.DateTerm;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import welfare.persistent.controller.BudgetController;
import welfare.persistent.controller.LogController;
import welfare.persistent.customtype.LogType;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.security.Log;
import welfare.persistent.exception.ControllerException;
import welfare.presentation.common.LogManager;
import welfare.reportdata.BudgetReportData;
import welfare.security.SecurityUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;
import welfare.utils.ReportUtils;

/**
 * @author Manop
 *
 */
public class BudgetMainManager {
	
	/* controller */
	private BudgetController budgetController;
	
	private Budget editBudget;
	/* list panel */
	private ArrayList<BudgetItem> budgetItemList = new ArrayList<BudgetItem>();
	private int listBudgetYear;
	/* create panel */
	private int createBudgetYear;
	private int createCopyFromBudgetYear;
	/* edit panel */
	private BudgetItem editBudgetItem;
	private BigDecimal editBudgetItemInitialAmount = new BigDecimal("0.00");
	private boolean isLockBudgetItem = false;
	private String welcomeMsg = null;
	private BigDecimal sumInitial; 
	private BigDecimal sumReserved; 
	private BigDecimal sumUsed; 
	private BigDecimal sumTransferIn; 
	private BigDecimal sumTransferOut; 
	private BigDecimal sumRemain; 
	
	public BudgetMainManager() {
		SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
		welcomeMsg = securityUser.getFullName();
		Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
		budgetController = new BudgetController();
		listBudgetYear = CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime());
		createBudgetYear = listBudgetYear;
		createCopyFromBudgetYear = createBudgetYear-1;
		editBudgetItem = new BudgetItem();
		listMainBudget();
	}
	
	public BigDecimal getSumInitial() {
		return sumInitial;
	}

	public BigDecimal getSumReserved() {
		return sumReserved;
	}

	public BigDecimal getSumUsed() {
		return sumUsed;
	}

	public BigDecimal getSumTransferIn() {
		return sumTransferIn;
	}

	public BigDecimal getSumTransferOut() {
		return sumTransferOut;
	}

	public BigDecimal getSumRemain() {
		return sumRemain;
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}
	
	public void setBudgetList(ArrayList<BudgetItem> budgetList) {
		this.budgetItemList = budgetList;
	}

	public Budget getEditBudget() {
		return editBudget;
	}

	public void setEditBudget(Budget editBudget) {
		this.editBudget = editBudget;
	}

	public ArrayList<BudgetItem> getBudgetItemList() {
		return budgetItemList;
	}
	
	public int getListBudgetYear() {
		return listBudgetYear;
	}

	public void setListBudgetYear(int listBudgetYear) {
		this.listBudgetYear = listBudgetYear;
	}

	public int getCreateBudgetYear() {
		return createBudgetYear;
	}

	public void setCreateBudgetYear(int createBudgetYear) {
		this.createBudgetYear = createBudgetYear;
	}

	public int getCreateCopyFromBudgetYear() {
		return createCopyFromBudgetYear;
	}

	public void setCreateCopyFromBudgetYear(int createCopyFromBudgetYear) {
		this.createCopyFromBudgetYear = createCopyFromBudgetYear;
	}

	public BudgetItem getEditBudgetItem() {
		return editBudgetItem;
	}

	public void setEditBudgetItem(BudgetItem editBudgetItem) {
		this.editBudgetItem = editBudgetItem;
	}

	public BigDecimal getEditBudgetItemInitialAmount() {
		return editBudgetItemInitialAmount;
	}

	public void setEditBudgetItemInitialAmount(
			BigDecimal editBudgetItemInitialAmount) {
		this.editBudgetItemInitialAmount = editBudgetItemInitialAmount;
	}

	/**
	 * �ʴ���¡�ç�����ҳ��Ǵ��ѡ����շ���ͧ���
	 */
	public void listMainBudget(){		
		try {
			editBudget = budgetController.getBudget(listBudgetYear);
			if (editBudget != null){
				budgetItemList = budgetController.getMainBudgetItems(editBudget.getBudgetYear());
				sumTotalValue();
			} else {
				budgetItemList.clear();
			}
			editBudgetItem = new BudgetItem();
			editBudgetItem.setAccountCode(getNextAccountCode());
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * �ѹ�֡������ҳ
	 */
	public void saveBudget(){
		try {
			if (editBudget != null) {
				if (editBudget.isAvailable()){
					editBudget.setEditable(false);
				}
				budgetController.saveBudget(editBudget);
				listMainBudget();						
			}
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * �Դ/�Դ ��������¡�ç�����ҳ
	 */
	public void lockBudget(){
		LogManager log = new LogManager();
		if (editBudget != null){
			editBudget.setEditable(!editBudget.isEditable());
			log.recordLockBudget(createBudgetYear,editBudget.isEditable());
			saveBudget();
		}
	}
	
	public void useBudget(){
		LogManager log = new LogManager();
		if (editBudget != null) {
			editBudget.setAvailable(!editBudget.isAvailable());
			log.recordUseBudget(createBudgetYear,editBudget.isAvailable());
			saveBudget();
		}
	}
	
	public void tableMainBudgetRowClicked(){
		
	}
	
	public void printReport() {
		LogManager log = new LogManager();
			try {
				JasperReport report = null;
				Date printDate = CalendarUtils.getDateTimeInstance().getTime();
				report = (JasperReport)JRLoader.loadObject(this.getClass().getResource("/welfare/reports/budgetReport.jasper"));
				
				ArrayList<BudgetReportData> budgetList = new ArrayList<BudgetReportData>();
				BigDecimal sumInitial = new BigDecimal("0.00");
				BigDecimal sumReserve = new BigDecimal("0.00");
				BigDecimal sumExpense = new BigDecimal("0.00");
				BigDecimal sumTransferIn = new BigDecimal("0.00");
				BigDecimal sumTransferOut = new BigDecimal("0.00");
				BigDecimal sumAvailable = new BigDecimal("0.00");
				
				 for (BudgetItem budgetItem : budgetItemList) {
					 BudgetReportData data = new BudgetReportData();
					 
					 sumInitial = sumInitial.add(budgetItem.getInitialAmount());
					 sumReserve = sumReserve.add(budgetItem.getReservedAmount());
					 sumExpense = sumExpense.add(budgetItem.getExpensedAmount());
					 sumTransferIn = sumTransferIn.add(budgetItem.getTransferInAmount());
					 sumTransferOut = sumTransferOut.add(budgetItem.getTransferOutAmount());
					 sumAvailable = sumAvailable.add(budgetItem.getAvailableAmount());
					 
					 data.setAccountCode(budgetItem.getAccountCode());
					 data.setAvailableAmount(budgetItem.getAvailableAmount());
					 data.setCategory(budgetItem.getCategory());
					 data.setExpensedAmount(budgetItem.getExpensedAmount());
					 data.setInitialAmount(budgetItem.getInitialAmount());
					 data.setReservedAmount(budgetItem.getReservedAmount());
					 data.setTransferInAmount(budgetItem.getTransferInAmount());
					 data.setTransferOutAmount(budgetItem.getTransferOutAmount());
					 
					 budgetList.add(data);
				 }
				 
				 HashMap budgetReportHashMap = new HashMap();
				 budgetReportHashMap.put("budgetYear", editBudget.getBudgetYear());
				 budgetReportHashMap.put("sumInitial", sumInitial);
				 budgetReportHashMap.put("sumReserve", sumReserve);
				 budgetReportHashMap.put("sumExpense", sumExpense);
				 budgetReportHashMap.put("sumTransferIn", sumTransferIn);
				 budgetReportHashMap.put("sumTransferOut", sumTransferOut);
				 budgetReportHashMap.put("sumAvailable", sumAvailable);
				 budgetReportHashMap.put("printDate", printDate);
				 budgetReportHashMap.put("logo", this.getClass().getResource("/welfare/reports/aerologo.png"));
				 System.out.println(printDate.getTime());
				 JasperPrint jasperPrint = JasperFillManager.fillReport(report, budgetReportHashMap, new JRBeanCollectionDataSource(budgetList));
				 
				 byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
				ReportUtils.displayPdfReport(bytes);
				log.printBudgetReport(editBudget.getBudgetYear());
				 
			} catch (JRException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
		
	private void sumTotalValue() {
		sumInitial = new BigDecimal("0.00");
		sumReserved = new BigDecimal("0.00");
		sumUsed = new BigDecimal("0.00");
		sumTransferIn = new BigDecimal("0.00");
		sumTransferOut = new BigDecimal("0.00");
		sumRemain = new BigDecimal("0.00");
		
		for (BudgetItem item : budgetItemList) {
			sumInitial = sumInitial.add(item.getInitialAmount());
			sumRemain = sumRemain.add(item.getAvailableAmount());
			sumReserved = sumReserved.add(item.getReservedAmount());
			sumTransferIn = sumTransferIn.add(item.getTransferInAmount());
			sumTransferOut = sumTransferOut.add(item.getTransferOutAmount());
			sumUsed = sumUsed.add(item.getExpensedAmount());
		}
	}
		
	/**
	 * ���ҧ������ҳ��Шӻ�
	 */
	public void createBudget(){
		LogManager log = new LogManager();
		try {
			
			// ��Ǩ�ͺ����է�����ҳ��Шӻ������������
			
			if (isBudgetExists(createBudgetYear)) {
				throw new ControllerException("�է�����ҳ��Шӻ� "+createBudgetYear+"��������");
			}
			
			Budget budget = new Budget();
			budget.setBudgetYear(createBudgetYear);
			
			budgetController.saveBudget(budget);
			log.recordCreateBudget(createBudgetYear);
			
			listMainBudget();			
			listBudgetYear = createBudgetYear;
			
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	/**
	 * ��Ǩ�ͺ����է�����ҳ��Шӻ������������ ��㹡óշ���ͧ������ҧ������ҳ���� copy ������ҳ
	 * @param aBudgetYear �շ���ͧ��õ�Ǩ�ͺ
	 * @return true ����է�����ҳ���� , false ��������
	 */
	private boolean isBudgetExists(int aBudgetYear){
		try {
			Budget budget = budgetController.getBudget(aBudgetYear);
			if (budget == null){
				return false;
			}
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * ���ҧ�����ҳ��Шӻ��¡�äѴ�͡�����Ũҡ�է�����ҳ����ͧ���
	 */
	public void copyBudget(){
		LogManager log = new LogManager();
		try {
			// ��Ǩ�ͺ��һշ���ͧ��äѴ�͡ �Ѻ �շ���ͧ������ҧ���������ǡѹ
			if (createBudgetYear == createCopyFromBudgetYear){
				throw new ControllerException("��س��кػշ���ͧ��äѴ�͡�Ѻ�շ���ͧ������ҧ����������͹�ѹ");
			}
			// ��Ǩ��һյ�ͧ��äѴ�͡���������������
			if (!isBudgetExists(createCopyFromBudgetYear)){
				throw new ControllerException("����է�����ҳ��Шӻ� "+createCopyFromBudgetYear+" ����ͧ��äѴ�͡ ");
			}
			// ��Ǩ��һյ�ͧ��äѴ�͡��������������
			if (isBudgetExists(createBudgetYear)){
				throw new ControllerException("�է�����ҳ��Шӻ� "+createBudgetYear+" �������� ");
			}
			budgetController.copyBudget(createCopyFromBudgetYear, createBudgetYear);
			log.recordCreateBudget(createBudgetYear);
			listBudgetYear = createBudgetYear;
			listMainBudget();			
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * �ѹ�֡��¡�ç�����ҳ
	 */
	public void saveBudgetItem(){
		boolean isNewBudgetItem = false;
		LogManager log = new LogManager();
		try {			
			if (editBudget != null ){
				if (editBudgetItem.getBudget() == null){
					editBudgetItem.setBudget(editBudget);
					isNewBudgetItem = true;
				}
				editBudgetItem.setBudgetLevel(Constants.LEVEL_1);
				budgetController.saveBudgetItem(editBudgetItem);
				if (!editBudgetItem.getStatus().equals(Status.DELETED) && isLockBudgetItem == false) {
					log.recordCreateBudgetItem(editBudgetItem.getAccountCode(),editBudgetItem.getInitialAmount(),isNewBudgetItem);
				}
				editBudgetItem = new BudgetItem();
				listMainBudget();				
				isNewBudgetItem = false;
				isLockBudgetItem = false;
			}			
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
	/**
	 * ź��¡�ç�����ҳ�¡�á�˹� STATUS = 'X'
	 */
	public void deleteBudgetItem(){
		LogManager log = new LogManager();
		try {			
			// ��Ǩ�ͺ����ա���駺����ҳ������ѧ ���������������ź
			if (!editBudgetItem.isDeletable()) {
				throw new ControllerException("�������öź��¡�ç�����ҳ�����ͧ�ҡ�ա���駺����ҳ�����");
			}
			editBudgetItem.setStatus(Status.DELETED);
			log.recordDeleteBudgetItem(editBudgetItem.getAccountCode());
			saveBudgetItem();
		} catch (ControllerException e) {
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		}
	}
			
	/**
	 * ���ҧ��¡�ç�����ҳ��������Ѻ��������
	 */
	public void newBudgetItem(){
		editBudgetItem = new BudgetItem();
		editBudgetItem.setAccountCode(getNextAccountCode());
	}
	
	private String getNextAccountCode() {
		String nextAccountCode = "";
		try {			
			nextAccountCode = budgetController.getNextAccountCodeForFirstLevelBudget(editBudget);						
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		System.out.println(nextAccountCode);
		return nextAccountCode;		
	}
	
	/**
	 * �Դ/�Դ ���ӡ�������¡�ç�����ҳ
	 */
	public void lockBudgetItem(){
		LogManager log = new LogManager();
		editBudgetItem.setEditable(!editBudgetItem.isEditable());
		log.recordLockBudgetItem(editBudgetItem.getAccountCode(), editBudgetItem.isEditable());
		isLockBudgetItem = true;
		saveBudgetItem();
	}
	
	/**
	 * ��Ǩ�ͺ�����¡�ç�����ҳ����ö������������
	 * @return
	 */
	public boolean isEditable(){
		return editBudget != null && editBudget.isEditable() && !editBudget.isAvailable();
	}
	
	public boolean isPrintable(){
		return editBudget != null;
	}
	
}
