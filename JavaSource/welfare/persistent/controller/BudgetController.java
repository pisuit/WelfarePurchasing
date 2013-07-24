package welfare.persistent.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;

import welfare.persistent.customtype.BudgetTransferStatus;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.budget.Budget;
import welfare.persistent.domain.budget.BudgetExpense;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.budget.BudgetTransfer;
import welfare.persistent.domain.purchasing.Vendor;
import welfare.persistent.domain.security.BudgetAuthorization;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;

public class BudgetController {
	public static final DecimalFormat NF = new DecimalFormat("00");
	
	@SuppressWarnings("unchecked")
	public String getNextAccountCodeForFirstLevelBudget(Budget budget) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		String nextAccountCode = "";		
		try {
			String accCode = "";
			int max = 0;
			session = sf.openSession();
			tx = session.beginTransaction();
						
			List<BudgetItem> budgetItemList = session.createQuery(
					"SELECT DISTINCT item " +
					"FROM BudgetItem item " +
					"WHERE item.budgetLevel = 'Level_1' " +
					"AND item.budget = :pbudget " +
					"AND item.status = 'NORMAL'")
					.setParameter("pbudget", budget)
					.list();
			
			for(BudgetItem item : budgetItemList) {
				if(Integer.parseInt(item.getAccountCode().substring(0, 2)) > max) {
					max = Integer.parseInt(item.getAccountCode().substring(0, 2));
				}
			}
			max++;
			accCode = NF.format(Integer.valueOf(max));
			nextAccountCode = accCode+"000000";
			tx.commit();
			return nextAccountCode;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดในการเชื่อมต่อกับฐานข้อมูล");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getNextAccountCodeForSubLevelBudget(BudgetItem parentBudgetItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		String nextAccountCode = "";
		try {
			if (parentBudgetItem.getBudgetLevel().equals("Level_1")) {
				String parentAccCode = parentBudgetItem.getAccountCode().substring(0, 2);
				int max = 0;
				String accCode = "";
				session = sf.openSession();
				tx = session.beginTransaction();
				
				List<BudgetItem> budgetItemList = session.createQuery(
						"SELECT DISTINCT item " +
						"FROM BudgetItem item " +
						"WHERE item.parentBudgetItem = :pparentBudget " +
						"AND item.status = 'NORMAL'")
						.setParameter("pparentBudget", parentBudgetItem)
						.list();
				
				for(BudgetItem item : budgetItemList) {
					if(Integer.parseInt(item.getAccountCode().substring(2, 4)) > max) {
						max = Integer.parseInt(item.getAccountCode().substring(2, 4));
						System.out.println(item.getAccountCode().substring(2, 4));
						System.out.println("max="+max);
					}
				}
				max++;
				System.out.println("parentAccCode="+parentAccCode);
				accCode = NF.format(Integer.valueOf(max));
				nextAccountCode = parentAccCode.toString()+accCode+"0000";
			} else if (parentBudgetItem.getBudgetLevel().equals("Level_2")) {
				String parentAccCode = parentBudgetItem.getAccountCode().substring(0, 4);
				int max = 0;
				String accCode = "";
				session = sf.openSession();
				tx = session.beginTransaction();
				
				List<BudgetItem> budgetItemList = session.createQuery(
						"SELECT DISTINCT item FROM BudgetItem item " +
						"WHERE item.parentBudgetItem = :pparentBudget " +
						"AND item.status = 'NORMAL'")
						.setParameter("pparentBudget", parentBudgetItem)
						.list();
				
				for(BudgetItem item : budgetItemList) {
					if(Integer.parseInt(item.getAccountCode().substring(4, 6)) > max) {
						max = Integer.parseInt(item.getAccountCode().substring(4, 6));
						System.out.println(item.getAccountCode().substring(4, 6));
						System.out.println("max="+max);
					}
				}
				max++;
				System.out.println("parentAccCode="+parentAccCode);
				accCode = NF.format(Integer.valueOf(max));
				nextAccountCode = parentAccCode.toString()+accCode+"00";
			} else if (parentBudgetItem.getBudgetLevel().equals("Level_3")) {
				String parentAccCode = parentBudgetItem.getAccountCode().substring(0, 6);
				int max = 0;
				String accCode = "";
				session = sf.openSession();
				tx = session.beginTransaction();
				
				List<BudgetItem> budgetItemList = session.createQuery(
						"SELECT DISTINCT item FROM BudgetItem item " +
						"WHERE item.parentBudgetItem = :pparentBudget " +
						"AND item.status = 'NORMAL'")
						.setParameter("pparentBudget", parentBudgetItem)
						.list();
				
				for(BudgetItem item : budgetItemList) {
					if(Integer.parseInt(item.getAccountCode().substring(6, 8)) > max) {
						max = Integer.parseInt(item.getAccountCode().substring(6, 8));
						System.out.println(item.getAccountCode().substring(6, 8));
						System.out.println("max="+max);
					}
				}
				max++;
				System.out.println("parentAccCode="+parentAccCode);
				accCode = NF.format(Integer.valueOf(max));
				nextAccountCode = parentAccCode.toString()+accCode;
			}
			tx.commit();
			return nextAccountCode;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดในการเชื่อมต่อกับฐานข้อมูล");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * ดึงงบประมาณประจำปีที่มีทั้งหมาด
	 * @return
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Budget> getBudgets() throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Budget> budgetList = session.createQuery(
					"SELECT DISTINCT bud 	" +
					"FROM Budget 			" +
					"ORDER BY bud.budgetYear" )
					.list();
			
			tx.commit();
			return new ArrayList<Budget>(budgetList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลงบประมาณ(หมวดหลัก)");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	
	/**
	 * ดึงข้อมูลงบประมาณประจำปี
	 * @param aYear ปีงบประมาณที่ต้องการดึงข้อมูล
	 * @return
	 * @throws ControllerException
	 */
	public Budget getBudget(int aYear) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Budget budget = (Budget) session.createQuery(
					"SELECT DISTINCT budget 			" +
					"FROM Budget budget 				" +
					"WHERE budget.budgetYear = :pbyear 	")
					.setParameter("pbyear", Integer.valueOf(aYear))
					.uniqueResult();
			tx.commit();
			return budget;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลงบประมาณ(หมวดหลัก)");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * บันทึกงบประมาณ
	 * @param aBudget งบประมาณที่ต้องการบันทึก
	 * @throws ControllerException
	 */
	public void saveBudget(Budget aBudget) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(aBudget);
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกข้อมูลงบประมาณประจำปี");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * คัดลอกงบประมาณจาก aFromYear ไปที่ aToYear
	 * @param aFromYear
	 * @param aToYear
	 * @throws ControllerException
	 */
	public void copyBudget(int aFromYear, int aToYear) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			// หาปีที่้ต้องการคัดลอก
			Budget fromBudget = (Budget) session.createQuery(
					"FROM Budget 				" +
					"WHERE budgetYear = :pbyear	")
					.setParameter("pbyear", Integer.valueOf(aFromYear))
					.uniqueResult();
			/*
			// หารายการงบประมาณของปีที่ต้องการคัดลอก
			List<BudgetItem> fromBudgetItemList = session.createQuery(					
					"SELECT DISTINCT budgetitem " +
					"FROM BudgetItem budgetitem " +
					"		left join fetch budgetitem.parentBudgetItem " +
					"		left join fetch budgetitem.budget budget " +
					"WHERE " +
					"budget.budgetYear = :pBudgetYear " +
					"AND budgetitem.status = 'NORMAL' " +
					"ORDER BY budget.id, budgetitem.id ")
					.setParameter("pbyear", Integer.valueOf(aFromYear))
					.list();
			*/
			// สร้างงบประมาณประจำปีโดยคัดลอกข้อมูลที่สำคัญเท่านั้น
			Budget toBudget = new Budget();
			toBudget.setBudgetYear(aToYear);
			session.saveOrUpdate(toBudget);
			// สร้างรายการงบประมาณ
			cloneBudgetItem(session, toBudget, null, fromBudget.getMainBudgetItem());
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกข้อมูลงบประมาณประจำปี");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * คัดลอกงบประมาณโดยการทำซ้ำ 
	 * @param session
	 * @param toBudget ปีงบประมาณที่ต้องการสร้าง
	 * @param parentBudgetItem งบหลัก
	 * @param subBugetItemList รายการงบย่อย
	 */
	private void cloneBudgetItem(Session session, Budget toBudget, BudgetItem parentBudgetItem, List<BudgetItem> subBugetItemList) {
		BudgetItem toBudgetItem = null;
		for (BudgetItem budgetItem : subBugetItemList) {
			if(budgetItem.getStatus().equals(Status.NORMAL)){
				toBudgetItem = new BudgetItem();
				toBudgetItem.setAccountCode(budgetItem.getAccountCode());
				toBudgetItem.setCategory(budgetItem.getCategory());
				toBudgetItem.setBudgetType(budgetItem.getBudgetType());
//				toBudgetItem.setInitialAmount(budgetItem.getInitialAmount());
				toBudgetItem.setInitialAmount(new BigDecimal("0.00"));
				toBudgetItem.setControlled(budgetItem.isControlled());
				toBudgetItem.setExpenseEntry(budgetItem.isExpenseEntry());
				toBudgetItem.setMonthlyBudget(budgetItem.isMonthlyBudget());
				toBudgetItem.setNurseryBudget(budgetItem.isNurseryBudget());
				toBudgetItem.setPurchasingBudget(budgetItem.isPurchasingBudget());
				toBudgetItem.setParentBudgetItem(parentBudgetItem);
				toBudgetItem.setBudget(toBudget);
				toBudgetItem.setBudgetLevel(budgetItem.getBudgetLevel());
				session.saveOrUpdate(toBudgetItem);
				System.out.println("Copy budget: "+budgetItem.getAccountCode());
				cloneBudgetItem(session, toBudget, toBudgetItem, budgetItem.getSubBudgetItems());
			}
		}
	}

	/**
	 * ดึงรายการงบประมาณหมวดหลักตามปี
	 * @param aYear ปีงบประมาณ
	 * @param aType ประเภทงบประมาณ M=หมวดหลัก S=หมวดย่อย
	 * @return
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetItem> getMainBudgetItems(int aYear) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetItem> budgetList = session.createQuery(
					"SELECT DISTINCT budgetitem 					" +
					"FROM 											" +
					"	BudgetItem budgetitem 						" +
					"	left join fetch budgetitem.subBudgetItems 	" +
					"	left join fetch budgetitem.parentBudgetItem	" +
					"	left join fetch budgetitem.budget budget 	" +
					"WHERE 											" +
					"	budget.budgetYear = :pbyear 				" +
					"AND budgetitem.budgetType = 'M' 				" +
					"AND budgetitem.status in ('NORMAL','CANCELED') 			" +
					"ORDER BY budgetitem.accountCode ")
					.setParameter("pbyear", Integer.valueOf(aYear))
					.list();
			tx.commit();
			return new ArrayList<BudgetItem>(budgetList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * ดึงข้อมูลรายการงบประมาณหมวดย่อยของหมวดหลัก
	 * @param aParentBudgetItem หมวดหลัก
	 * @return
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetItem> getSubBudgetItems(BudgetItem aParentBudgetItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetItem> budgetList = session.createQuery(
					"SELECT DISTINCT budgetitem 						" +
					"FROM 												" +
					"BudgetItem budgetitem 								" +
					"	left join fetch budgetitem.subBudgetItems 		" +
					"	left join fetch budgetitem.parentBudgetItem 		" +
					"	left join fetch budgetitem.budget budget 		" +
					"WHERE 												" +
					"	budgetitem.parentBudgetItem = :pparentitem 	" +
					"AND budgetitem.budgetType = 'S' 					" +
					"AND budgetitem.status in ('NORMAL','CANCELED') 				" +
					"ORDER BY budgetitem.accountCode ")
					.setParameter("pparentitem", aParentBudgetItem)
					.list();
			tx.commit();
			return new ArrayList<BudgetItem>(budgetList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * ดึงรายการงบประมาณทั้งหมดของปีที้ต้องการ 
	 * @param aBudgetYear ปีงบประมาณที่ต้องการดึงรายการงบประมาณ
	 * @return รายการงบประมาณทั้งหมดพร้อมทั้งงบหมวดหลักและหมวดย่อยของรายการงบประมาณนั้นๆด้วย
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetItem> getAllBugetItems(int aBudgetYear) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetItem> budgetList = session.createQuery(
					"SELECT distinct budItem 							" +
					"FROM BudgetItem budItem 							" +
					"	left join fetch budItem.budget budget 			" +
					"	left join fetch budItem.parentBudgetItem pcate 	" +
					"	left join fetch budItem.subBudgetItems scate 	" +
					"WHERE 												" +
					"		budget.budgetYear = :pbyear 				" +
					"AND	budItem.status = 'NORMAL' 						" +
					"ORDER BY budItem.accountCode	" )
					.setParameter("pbyear", aBudgetYear)
					.list();
			tx.commit();
			return new ArrayList<BudgetItem>(budgetList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	/**
	 * บันทึกข้อมูลรายการงบประมาณ
	 * @param aBudgetItem รายการงบประมาณที่ต้องการบันทึก
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	public void saveBudgetItem(BudgetItem aBudgetItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			// ตรวจสอบว่าจำนวนเงินรวมต้องไม่เกินจำนวนในหมวดใหญ่
			if (aBudgetItem.getParentBudgetItem() != null ){
				// บันทึกลงฐานข้อมูลก่อนเพื่อเอา ID ไปใช้ใน query
				session.saveOrUpdate(aBudgetItem);
				List<BudgetItem>  budgetItemList = session.createQuery(
						"SELECT DISTINCT item FROM BudgetItem item 		" +
						"	left join fetch item.budget 				" +
						"	left join fetch item.subBudgetItems 			" +
						"	left join fetch item.parentBudgetItem parent 	" +
						"WHERE 											" +
						"		parent = :pparent 						" +
						"AND	item != :pitem 							" )
						.setParameter("pparent", aBudgetItem.getParentBudgetItem())
						.setParameter("pitem", aBudgetItem)
						.list();
				BigDecimal sumSubBudgetItemInitialAmount =  new BigDecimal("0.00");
				BigDecimal parentBudgetItemInitialAmount = aBudgetItem.getParentBudgetItem().getInitialAmount();
				BigDecimal parentBudgetItemTransferInAmount = aBudgetItem.getParentBudgetItem().getTransferInAmount();
				BigDecimal parentBudgetItemTransferOutAmount = aBudgetItem.getParentBudgetItem().getTransferOutAmount();
				for (BudgetItem budgetItem : budgetItemList) {
					sumSubBudgetItemInitialAmount = sumSubBudgetItemInitialAmount.add(budgetItem.getInitialAmount());
				}
				sumSubBudgetItemInitialAmount = sumSubBudgetItemInitialAmount.add(aBudgetItem.getInitialAmount());
				System.out.println("sub = "+sumSubBudgetItemInitialAmount);
				System.out.println("parent initial = "+parentBudgetItemInitialAmount);
				System.out.println("parent tran in = "+parentBudgetItemTransferInAmount);
				System.out.println("parent tran out = "+parentBudgetItemTransferOutAmount);
				System.out.println("sum = "+parentBudgetItemInitialAmount.doubleValue() + (parentBudgetItemTransferInAmount.doubleValue() - parentBudgetItemTransferOutAmount.doubleValue()));
				if (sumSubBudgetItemInitialAmount.doubleValue() > parentBudgetItemInitialAmount.doubleValue() + (parentBudgetItemTransferInAmount.doubleValue() - parentBudgetItemTransferOutAmount.doubleValue())) {
					throw new ControllerException("จำนวนเงินรวมภายในหมวดย่อยเกินจำนวนเงินที่ตั้งไว้ในหมวดใหญ่");
				}
			} else {
				session.saveOrUpdate(aBudgetItem);
			}
			
			updateEditableStatus(session, aBudgetItem);
			
			tx.commit();
		} catch (ControllerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null ) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกรายการบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	public void saveEditorBudgetItem(BudgetItem budgetitem){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(budgetitem);
			tx.commit();
		}  catch (Exception e) {
			e.printStackTrace();
			if (tx != null ) {
				tx.rollback();
			}
		}
	}
	
	
	private void updateEditableStatus(Session session, BudgetItem aBudgetItem) {
		System.out.println(session.isOpen());
		if (aBudgetItem == null ){
			return;
		}
		List<BudgetItem> subItems =  aBudgetItem.getSubBudgetItems();
		for (BudgetItem budgetItem : subItems) {
			budgetItem.setEditable(aBudgetItem.isEditable());
			session.saveOrUpdate(budgetItem);
			updateEditableStatus(session, budgetItem);
		}
	}

	/**
	 * ดึงรายการงบประมาณที่มี ID ตรงกับที่ต้องการ
	 * @param aBudgetItemId ID ของรายการงบประมาณ
	 * @return รายการงบประมาณที่เจอพร้อมทั้งหมวดหลักและหมวดย่อยของรายการงบประมาณนั้นด้วย  ถ้าไม่เจอจะเป็นค่า null
	 * @throws ControllerException
	 */
	public BudgetItem getBudgetItem(Long aBudgetItemId) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			BudgetItem budgetItem = (BudgetItem) session.createQuery(
					"SELECT DISTINCT item FROM BudgetItem item 	" +
					"	left join fetch item.budget 			" +
					"	left join fetch item.subBudgetItems 		" +
					"	left join fetch item.parentBudgetItem 	" +
					"WHERE item.id = :pid 						" )
					.setParameter("pid", aBudgetItemId)
					.uniqueResult();
			tx.commit();
			return budgetItem;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลรายการงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * บันทึกใบโอนงบประมาณ ถ้าเป็นใบโอนใหม่จะทำการกำหนดเลขที่ใบโอนให้อัตโนมัติ
	 * @param aBudgetTransfer ใบโอนงบประมาณที่ต้องการบันทึก
	 * @return ใบโอนงบประมาณที่ถูกบันทึกเรียบร้อย
	 * @throws ControllerException
	 */
	public BudgetTransfer saveBudgetTransfer(BudgetTransfer aBudgetTransfer, boolean flag) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if (aBudgetTransfer.getId() == null ){
				Object maxNumber = session.createQuery(
						"SELECT MAX(t.transferNumber) 		" +
						"FROM BudgetTransfer t 				" +
						"	left join t.fromBudgetItem b 		" +
						"WHERE 								" +
						"	b.budget.budgetYear = :pbyear	" )
						.setParameter("pbyear", aBudgetTransfer.getFromBudgetItem().getBudget().getBudgetYear())
						.uniqueResult();
				int nextNumber;
				if (maxNumber == null ){
					nextNumber = 1;
				}else {
					nextNumber = ((Integer)maxNumber).intValue();
					nextNumber++;
				}
				aBudgetTransfer.setTransferNumber(nextNumber);
			}
			
			session.saveOrUpdate(aBudgetTransfer);
					
			BudgetItem budgetItem = getBudgetItem(aBudgetTransfer.getOldFromBudgetItem().getId());
			if (aBudgetTransfer.getStatus().equals(BudgetTransferStatus.DISCARDED)) {
				BigDecimal minus = new BigDecimal("-1.00");
				reserveBudget(session, budgetItem, aBudgetTransfer.getRequestAmount().multiply(minus));
			} 
			if (flag == true) {
				System.out.println("is new");
				System.out.println("request"+aBudgetTransfer.getRequestAmount());
				reserveBudget(session, budgetItem, aBudgetTransfer.getRequestAmount());
			} else {
				System.out.println("is old");
				if (aBudgetTransfer.getOldAmount().compareTo(aBudgetTransfer.getRequestAmount()) == -1) {
					reserveBudget(session, budgetItem, aBudgetTransfer.getRequestAmount().subtract(aBudgetTransfer.getOldAmount()));
					
					aBudgetTransfer.setOldAmount(aBudgetTransfer.getRequestAmount());
					session.saveOrUpdate(aBudgetTransfer);
				} else if (aBudgetTransfer.getOldAmount().compareTo(aBudgetTransfer.getRequestAmount()) == 1) {
					reserveBudget(session, budgetItem, aBudgetTransfer.getRequestAmount().subtract(aBudgetTransfer.getOldAmount()));
					
					aBudgetTransfer.setOldAmount(aBudgetTransfer.getRequestAmount());
					session.saveOrUpdate(aBudgetTransfer);
				}
			}									
			tx.commit();
			return aBudgetTransfer;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกรายการขอโอนงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	/**
	 * ดึงรายการขอโอนงบประมาณประจำปี
	 * @param aBudgetYear ปีงบประมาณที่ต้องการ
	 * @return รายการขอโอนงบประมาณพร้อมทั้งหมวดงบประมาณที่ขอโอนเข้าและโอนออก
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetTransfer> getBudgetTransfers(int aBudgetYear) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetTransfer> transferList = session.createQuery(
					"SELECT DISTINCT bt 						" +
					"FROM BudgetTransfer bt 					" +
					"	left join fetch bt.fromBudgetItem fcate	" +
					"	left join fetch bt.toBudgetItem tcate 	" +
					"	left join fetch bt.oldFromBudgetItem ofcate	" +
					"	left join fetch bt.oldToBudgetItem otcate 	" +
					"	left join fetch fcate.budget bud1 		" +
					"	left join fetch tcate.budget bud2 		" +
					"	left join fetch ofcate.budget bud3 		" +
					"	left join fetch otcate.budget bud4 		" +
					"WHERE 										" +
					"	bud1.budgetYear = :pbyear 				" +
					"ORDER BY bt.transferNumber desc			" )
					.setParameter("pbyear", aBudgetYear)
					.list();
			
			tx.commit();
			return new ArrayList<BudgetTransfer>(transferList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงรายการขอโอนงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	public BudgetTransfer approveBudgetTransfer(BudgetTransfer aBudgetTransfer) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		BigDecimal minus = new BigDecimal("-1.00");
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
		
			aBudgetTransfer.setApproveDate(CalendarUtils.getDateTimeInstance().getTime());
			aBudgetTransfer.setStatus(BudgetTransferStatus.APPROVED);
			session.saveOrUpdate(aBudgetTransfer);
			
			BudgetItem transferInBudgetItem = aBudgetTransfer.getToBudgetItem(); // หมวดงบประมาณที่ต้องการโอนเข้า
			BudgetItem transferOutBudgetItem = aBudgetTransfer.getFromBudgetItem(); // หมวดงบประมาณที่ต้องการโอนออก
			BudgetItem oldTransferOutBudgetItem = aBudgetTransfer.getOldFromBudgetItem();
			BudgetItem closestControlledBudgetItem; // หมวดที่มีการควบคุมงบประมาณ
			BigDecimal transferOutAvailableAmount ; // จำนวนเงินในหมวดที่ยังสามารถใช้ได้อยู่
			BigDecimal transferAmount; // จำนวนเงินที่ต้องการโอน
			
			// ตรวจสอบว่าสามารถโอนงบออกได้หรือไม่
			closestControlledBudgetItem = closestControlledBudgetItem(session, transferOutBudgetItem);
			transferAmount = aBudgetTransfer.getApproveAmount();
			if (closestControlledBudgetItem == null ){
				// ไม่มีการควบคุมงบประมาณ
				transferOutAvailableAmount = BigDecimal.valueOf(Double.MAX_VALUE);
			} else {
				// มีการควบคุมงบประมาณ
				transferOutAvailableAmount = closestControlledBudgetItem.getAvailableAmount();
				if (transferAmount.compareTo(transferOutAvailableAmount) == 1 ) {
					// จำนวนเงินที่ขอโอนออกมากกว่าจำนวนเงินที่สามารถใช้ได้
					StringBuffer buff = new StringBuffer();
					buff.append("ไม่สามารถทำรายการได้เนื่องจากมีการจำกัดวงเงินงบประมาณที่หมวด ").append(closestControlledBudgetItem).append("\n");
					buff.append("จำนวนเงินที่สามารถโอนได้ = ").append(transferOutAvailableAmount).append("\n");
					buff.append("จำนวนเงินที่ต้องการโอน = ").append(transferAmount);
					throw new ControllerException(buff.toString());			
				}
			}
			// ปรับยอดการโอนเข้าและโอนออกของหมวดงบประมาณ
			//transferOutBudgetItem.transferOut(transferAmount);
			//transferInBudgetItem.transferIn(transferAmount);
			// ปรับปรุงยอดการใช้จ่ายงบประมาณสำหรับหมวดหลัก
			//expenseBudget(session, transferOutBudgetItem.getParentBudgetItem(), transferAmount);
			BudgetItem budgetItem = (BudgetItem) session.load(BudgetItem.class, transferOutBudgetItem.getId());
			BudgetItem oldFromBudgetItem = (BudgetItem) session.load(BudgetItem.class, oldTransferOutBudgetItem.getId());
			
			//expenseBudget(session, budgetItem, transferAmount);
			// บันทึกงบประมาณ
			//session.saveOrUpdate(transferOutBudgetItem);
			//session.saveOrUpdate(transferInBudgetItem);
			BudgetItem transferOut = (BudgetItem) session.load(BudgetItem.class, transferOutBudgetItem.getId());
//			transferOut.transferOut(transferAmount);
			BudgetItem transferIn = (BudgetItem) session.load(BudgetItem.class, transferInBudgetItem.getId());
//			transferIn.transferIn(transferAmount);
			
			transferInBudget(session, transferIn, transferAmount);
			transferOutBudget(session, transferOut, transferAmount);
//			session.saveOrUpdate(transferOut);
//			session.saveOrUpdate(transferIn);
			
			if (transferOutBudgetItem.getId().equals(oldTransferOutBudgetItem)) {
				reserveBudget(session, budgetItem, aBudgetTransfer.getRequestAmount().multiply(minus));
			} else {
				reserveBudget(session, oldFromBudgetItem, aBudgetTransfer.getRequestAmount().multiply(minus));
			}
					
			tx.commit();
			return aBudgetTransfer;
		} catch (ControllerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกรายการขอโอนงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (tx != null && tx.isActive()) {
//				tx.rollback();
//			}
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	
	
	public boolean isSubBudgetItem(BudgetItem aParentBudgetItem, BudgetItem aSubBudgetItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			BudgetItem budgetItem = (BudgetItem) session.createQuery(
					"SELECT DISTINCT item 	" +
					"FROM 					" +
					"	BudgetItem item 	" +
					"WHERE 					" +
					"	item.id = :pid 		" )
					.setParameter("pid", aParentBudgetItem.getId())
					.uniqueResult();
			tx.commit();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลรายการงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetExpense> getBudgetExpenses(BudgetItem aBudgetItem,Date date) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetExpense> budgetExpenseList = session.createQuery(
				"SELECT DISTINCT budEx 							" +
				"FROM BudgetExpense budEx 						" +
				"	left join fetch budEx.budgetItem budItem	" +
				"	left join fetch budEx.vendor 				" +
				"WHERE 											" +
				"		budItem = :pbudItem 					" +
				"AND budEx.postingDate = :pdate					" +
				"ORDER BY budEx.id desc 						" )
				.setParameter("pbudItem", aBudgetItem)
				.setParameter("pdate", date)
				.list();
			tx.commit();
			return new ArrayList<BudgetExpense>(budgetExpenseList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลค่าใช้จ่าย");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetExpense> getAllBudgetExpenses(BudgetItem aBudgetItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetExpense> budgetExpenseList = session.createQuery(
				"SELECT DISTINCT budEx 							" +
				"FROM BudgetExpense budEx 						" +
				"	left join fetch budEx.budgetItem budItem	" +
				"	left join fetch budEx.vendor 				" +
				"WHERE 											" +
				"		budItem = :pbudItem 					" +
				"ORDER BY budEx.id desc 						" )
				.setParameter("pbudItem", aBudgetItem)
				.list();
			tx.commit();
			return new ArrayList<BudgetExpense>(budgetExpenseList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลค่าใช้จ่าย");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetExpense> getBudgetExpenseByDate(BudgetItem aBudgetItem, Date fromDate, Date toDate) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetExpense> budgetExpenseList = session.createQuery(
				"SELECT DISTINCT budEx 							" +
				"FROM BudgetExpense budEx 						" +
				"	left join fetch budEx.budgetItem budItem	" +
				"	left join fetch budEx.vendor 				" +
				"WHERE 											" +
				"		budItem = :pbudItem 					" +
				"AND (budEx.postingDate > :pfromDate			" +
				"OR budEx.postingDate = :pfromDate)				" +
				"AND (budEx.postingDate < :ptoDate				" +
				"OR budEx.postingDate = :ptoDate)				" +
				"ORDER BY budEx.id desc 						" )
				.setParameter("pbudItem", aBudgetItem)
				.setParameter("pfromDate", fromDate)
				.setParameter("ptoDate", toDate)
				.list();
			tx.commit();
			return new ArrayList<BudgetExpense>(budgetExpenseList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลค่าใช้จ่าย");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
		
	public BudgetExpense saveBudgetExpenses(BudgetExpense aBudgetExpense, BigDecimal oldPrice) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;	
		boolean isNewBudgetExpense = false;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			if(aBudgetExpense.getId() == null) isNewBudgetExpense = true;
			
			session.saveOrUpdate(aBudgetExpense);
			
			// ตรวจสอบว่าสามารถใช้งบประมาณได้หรือไม่
			BudgetItem expensedBudgetItem = aBudgetExpense.getBudgetItem();
			BudgetItem closestControlledBudgetItem; // หมวดที่มีการควบคุมงบประมาณ
			BigDecimal availableAmount; // จำนวนเงินที่สามารถใช้ได้
			BigDecimal expensedAmount; // จำนวนเงินที่ต้องการใช้

			closestControlledBudgetItem = closestControlledBudgetItem(session, expensedBudgetItem);
			expensedAmount = aBudgetExpense.getAmount();
			if (closestControlledBudgetItem != null ){
				availableAmount = closestControlledBudgetItem.getAvailableAmount();
				if (expensedAmount.compareTo(availableAmount) == 1 ) {
					// จำนวนเงินที่ขอโอนออกมากกว่าจำนวนเงินที่สามารถใช้ได้
					StringBuffer buff = new StringBuffer();
					buff.append("ไม่สามารถทำรายการได้เนื่องจากมีการจำกัดวงเงินงบประมาณที่หมวด ").append(closestControlledBudgetItem).append("\n");
					buff.append("จำนวนเงินที่สามารถใช้ได้ = ").append(availableAmount).append("\n");
					buff.append("จำนวนเงินที่ต้องการใช้ = ").append(expensedAmount);
					throw new ControllerException(buff.toString());			
				}
			}
			// ปรับยอดการใช้งบประมาณ
			if (isNewBudgetExpense == true) {
				BudgetItem item = (BudgetItem) session.load(BudgetItem.class, expensedBudgetItem.getId());
				expenseBudget(session, item, expensedAmount);
			} else {
				BudgetItem item = (BudgetItem) session.load(BudgetItem.class, expensedBudgetItem.getId());
				BigDecimal totalPrice = new BigDecimal("0.00");
				
				if(oldPrice.compareTo(aBudgetExpense.getAmount()) == 0) {
					System.out.println("Did nothing");
				} else if(oldPrice.compareTo(aBudgetExpense.getAmount()) == 1) {
					totalPrice = aBudgetExpense.getAmount().subtract(oldPrice);
					expenseBudget(session, item, totalPrice);
				} else {
					totalPrice = aBudgetExpense.getAmount().subtract(oldPrice);
					expenseBudget(session, item, totalPrice);
				}
			}
			
			tx.commit();
			return aBudgetExpense;
		} catch (ControllerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกค่าใช้จ่าย");
		} finally {
			session.clear();
			session.close();
//			if (tx != null && tx.isActive()) {
//				tx.rollback();
//			}
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	public void deleteBudgetExpense(BudgetExpense aBudgetExpense) throws ControllerException {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;	
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			BigDecimal minus = new BigDecimal("-1.00");
			BigDecimal totalPrice = new BigDecimal("0.00");
			BudgetItem budgetItem = aBudgetExpense.getBudgetItem();
			
			totalPrice = aBudgetExpense.getAmount().multiply(minus);
			expenseBudget(session, budgetItem, totalPrice);
			
			session.delete(aBudgetExpense);
			
			tx.commit();		
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการลบข้อมูลค่าใช้จ่าย");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	public void deleteBudgetItem(BudgetItem aBudgetItem) throws ControllerException {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;	
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
						
			session.delete(aBudgetItem);
			
			tx.commit();		
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการลบข้อมูลค่าใช้จ่าย");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	public void deleteItem() throws ControllerException {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;	
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
					
			List<BudgetItem> list = session.createQuery(
					"SELECT DISTINCT item " +
					"FROM BudgetItem item " +
					"WHERE item.status = 'DELETED'")
					.list();
			for(BudgetItem budgetItem : list){
				session.delete(budgetItem);
			}
					
			tx.commit();		
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการลบข้อมูลค่าใช้จ่าย");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Vendor> getVendors() throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Vendor> vendorList = session.createQuery(
				"SELECT DISTINCT vendor " +
				"FROM 					" +
				"	Vendor vendor 		" )
				.list();
			tx.commit();
			return new ArrayList<Vendor>(vendorList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลร้านค้า");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	public Vendor getVendor(Long aVendorId) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Vendor vendor = (Vendor) session.createQuery(
					"SELECT DISTINCT vendor " +
					"FROM 					" +
					"	Vendor vendor 		" +
					"WHERE vendor.id = :pid " )
					.setParameter("pid", aVendorId)
					.uniqueResult();
			tx.commit();
			return vendor;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลร้านค้า");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * ปรับปรุงยอดการใช้จ่ายงบประมาณ
	 * @param session
	 * @param aBudgetItem หมวดงบประมาณที่ต้องการปรับปรุงยอดการใช้งบประมาณ
	 * @param aExpensedAmount จำนวนเงินที่มีการใช้งบประมาณ
	 */
	public void expenseBudget(Session session, BudgetItem aBudgetItem, BigDecimal aExpensedAmount){
		if (aBudgetItem == null) {
			return;
		}
		aBudgetItem.expense(aExpensedAmount);
		session.saveOrUpdate(aBudgetItem);
		expenseBudget(session, aBudgetItem.getParentBudgetItem(), aExpensedAmount);
	}

	/**
	 * ปรับปรุงยอดการจองงบประมาณ
	 * @param session
	 * @param aBudgetItem หมวดงบประมาณที่ต้องการปรับปรุงยอดการจอง
	 * @param aReservedAmount จำนวนเงินที่มีการจองงบประมาณ
	 */
	public void reserveBudget(Session session, BudgetItem aBudgetItem, BigDecimal aReservedAmount){
		if (aBudgetItem == null) {
			return;
		}
		System.out.println("amount in reserve = "+aReservedAmount);
		aBudgetItem.reserve(aReservedAmount);
		session.saveOrUpdate(aBudgetItem);
		reserveBudget(session, aBudgetItem.getParentBudgetItem(), aReservedAmount);
	}
	
	public void transferInBudget(Session session, BudgetItem aBudgetItem, BigDecimal transferAmount){
		if (aBudgetItem == null) {
			return;
		}
		System.out.println("budgetitem in = "+aBudgetItem.getCategory());
		System.out.println("amount in transfer = "+transferAmount);
		aBudgetItem.transferIn(transferAmount);
		session.saveOrUpdate(aBudgetItem);
		transferInBudget(session, aBudgetItem.getParentBudgetItem(), transferAmount);
	}
	
	public void transferOutBudget(Session session, BudgetItem aBudgetItem, BigDecimal transferAmount){
		if (aBudgetItem == null) {
			return;
		}
		System.out.println("budgetitem out = "+aBudgetItem.getCategory());
		System.out.println("amount out transfer = "+transferAmount);
		aBudgetItem.transferOut(transferAmount);
		session.saveOrUpdate(aBudgetItem);
		transferOutBudget(session, aBudgetItem.getParentBudgetItem(), transferAmount);
	}
	
	
	/**
	 * หาหมวดงบประมาณหมวดที่มีการควบคุมงบประมาณ
	 * @param session
	 * @param aBudgetItem
	 * @return 1.หมวดงบประมาณที่มีการควบคุม   2.null ถ้าหากไม่เจอหมวดงบประมาณที่มีการควบคุมงบประมาณ
	 */
	public BudgetItem closestControlledBudgetItem( Session session, BudgetItem aBudgetItem) {
		if (aBudgetItem != null ) {
			aBudgetItem = (BudgetItem) session.load(BudgetItem.class, aBudgetItem.getId());
			if (aBudgetItem.isControlled()) {
				return aBudgetItem;
			} else {
				return closestControlledBudgetItem(session, aBudgetItem.getParentBudgetItem());
			}
		} else {
			// สิ้นสุดสายงบประมาณแล้วแต่ไม่เจอหมวดที่มีการควบคุม
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetAuthorization> getBudgetAuthorizationsForCurrentUser(Long userID) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetAuthorization> budgetAuthorizations = session.createQuery(
					"SELECT budgetAuth " +
					"FROM BudgetAuthorization budgetAuth " +
					"	left join fetch budgetAuth.user user " +
					"WHERE " +
					"	user.id = :puserid " +
					"ORDER BY budgetAuth.budgetAuth")
					.setParameter("puserid", userID)
					.list();
			tx.commit();
			return new ArrayList<BudgetAuthorization>(budgetAuthorizations);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public BudgetItem getAuthorizedBudgetItemForCurrentUser(int budgetYear, String accountCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			BudgetItem budgetItems = (BudgetItem) session.createQuery(
					"SELECT DISTINCT budgetitem 					" +
					"FROM 											" +
					"	BudgetItem budgetitem 						" +
					"	left join fetch budgetitem.subBudgetItems 	" +
					"	left join fetch budgetitem.parentBudgetItem	" +
					"	left join fetch budgetitem.budget budget 	" +
					"WHERE 											" +
					"	budget.budgetYear = :pbyear 				" +
					"AND budgetitem.accountCode = :paccountcode 	" +					
					"AND budgetitem.status in ('NORMAL','CANCELED') " +
					"ORDER BY budgetitem.accountCode 				")
					.setParameter("pbyear", budgetYear)
					.setParameter("paccountcode", accountCode)
					.uniqueResult();
			tx.commit();
			return budgetItems;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetItem> getSubBudgetItemsForAuthorizedUser(BudgetItem aParentBudgetItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetItem> budgetList = session.createQuery(
					"SELECT DISTINCT budgetitem 						" +
					"FROM 												" +
					"BudgetItem budgetitem 								" +
					"	left join fetch budgetitem.subBudgetItems 		" +
					"	left join fetch budgetitem.parentBudgetItem 		" +
					"	left join fetch budgetitem.budget budget 		" +
					"WHERE 												" +
					"	budgetitem.parentBudgetItem = :pparentitem 	" +
					"AND budgetitem.status in ('NORMAL','CANCELED') 				" +
					"ORDER BY budgetitem.accountCode ")
					.setParameter("pparentitem", aParentBudgetItem)
					.list();
			tx.commit();
			return new ArrayList<BudgetItem>(budgetList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลงบประมาณ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetTransfer> getBudgetTransferForBudgetItem(BudgetItem budgetItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetTransfer> budgetTransferList = session.createQuery(
					"SELECT DISTINCT budgetTransfer " +
					"FROM BudgetTransfer budgetTransfer " +
					"	left join fetch budgetTransfer.fromBudgetItem fcate	" +
					"	left join fetch budgetTransfer.toBudgetItem tcate 	" +
					"WHERE (budgetTransfer.fromBudgetItem = :pbudgetitem " +
					"OR budgetTransfer.toBudgetItem = :pbudgetitem) " +
					"AND budgetTransfer.status = 'APPROVED' " +
					"ORDER BY budgetTransfer.approveDate desc")
					.setParameter("pbudgetitem", budgetItem)
					.list();
			tx.commit();
			return new ArrayList<BudgetTransfer>(budgetTransferList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลการโอนงบประมาณ");
		} finally {
			session.clear();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetTransfer> getBudgetTransferForBudgetItemByDate(BudgetItem budgetItem,Date fromDate,Date toDate) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetTransfer> budgetTransferList = session.createQuery(
					"SELECT DISTINCT budgetTransfer " +
					"FROM BudgetTransfer budgetTransfer " +
					"	left join fetch budgetTransfer.fromBudgetItem fcate	" +
					"	left join fetch budgetTransfer.toBudgetItem tcate 	" +
					"WHERE (budgetTransfer.fromBudgetItem = :pbudgetitem " +
					"OR budgetTransfer.toBudgetItem = :pbudgetitem) " +
					"AND budgetTransfer.status = 'APPROVED' " +
					"AND budgetTransfer.approveDate >= :pfromdate " +
					"AND budgetTransfer.approveDate <= :ptodate " +
					"ORDER BY budgetTransfer.approveDate desc")
					.setParameter("pbudgetitem", budgetItem)
					.setParameter("pfromdate", fromDate)
					.setParameter("ptodate", toDate)
					.list();
			tx.commit();
			return new ArrayList<BudgetTransfer>(budgetTransferList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลการโอนงบประมาณ");
		} finally {
			session.clear();
			session.close();
		}
	}
}
