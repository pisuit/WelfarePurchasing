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
			throw new ControllerException("�Դ�����Դ��Ҵ㹡���������͡Ѻ�ҹ������");
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
			throw new ControllerException("�Դ�����Դ��Ҵ㹡���������͡Ѻ�ҹ������");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * �֧������ҳ��Шӻշ���շ����Ҵ
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����ŧ�����ҳ(��Ǵ��ѡ)");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	
	/**
	 * �֧�����ŧ�����ҳ��Шӻ�
	 * @param aYear �է�����ҳ����ͧ��ô֧������
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����ŧ�����ҳ(��Ǵ��ѡ)");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * �ѹ�֡������ҳ
	 * @param aBudget ������ҳ����ͧ��úѹ�֡
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��úѹ�֡�����ŧ�����ҳ��Шӻ�");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * �Ѵ�͡������ҳ�ҡ aFromYear 价�� aToYear
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
			
			// �һշ����ͧ��äѴ�͡
			Budget fromBudget = (Budget) session.createQuery(
					"FROM Budget 				" +
					"WHERE budgetYear = :pbyear	")
					.setParameter("pbyear", Integer.valueOf(aFromYear))
					.uniqueResult();
			/*
			// ����¡�ç�����ҳ�ͧ�շ���ͧ��äѴ�͡
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
			// ���ҧ������ҳ��Шӻ��¤Ѵ�͡�����ŷ���Ӥѭ��ҹ��
			Budget toBudget = new Budget();
			toBudget.setBudgetYear(aToYear);
			session.saveOrUpdate(toBudget);
			// ���ҧ��¡�ç�����ҳ
			cloneBudgetItem(session, toBudget, null, fromBudget.getMainBudgetItem());
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��úѹ�֡�����ŧ�����ҳ��Шӻ�");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * �Ѵ�͡������ҳ�¡�÷ӫ�� 
	 * @param session
	 * @param toBudget �է�����ҳ����ͧ������ҧ
	 * @param parentBudgetItem ����ѡ
	 * @param subBugetItemList ��¡�ç�����
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
	 * �֧��¡�ç�����ҳ��Ǵ��ѡ�����
	 * @param aYear �է�����ҳ
	 * @param aType ������������ҳ M=��Ǵ��ѡ S=��Ǵ����
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����ŧ�����ҳ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * �֧��������¡�ç�����ҳ��Ǵ���¢ͧ��Ǵ��ѡ
	 * @param aParentBudgetItem ��Ǵ��ѡ
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����ŧ�����ҳ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * �֧��¡�ç�����ҳ�������ͧ�շ���ͧ��� 
	 * @param aBudgetYear �է�����ҳ����ͧ��ô֧��¡�ç�����ҳ
	 * @return ��¡�ç�����ҳ�������������駧���Ǵ��ѡ�����Ǵ���¢ͧ��¡�ç�����ҳ�������
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����ŧ�����ҳ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	/**
	 * �ѹ�֡��������¡�ç�����ҳ
	 * @param aBudgetItem ��¡�ç�����ҳ����ͧ��úѹ�֡
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
			
			// ��Ǩ�ͺ��Ҩӹǹ�Թ�����ͧ����Թ�ӹǹ���Ǵ�˭�
			if (aBudgetItem.getParentBudgetItem() != null ){
				// �ѹ�֡ŧ�ҹ�����š�͹������� ID ���� query
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
					throw new ControllerException("�ӹǹ�Թ���������Ǵ�����Թ�ӹǹ�Թ�����������Ǵ�˭�");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��úѹ�֡��¡�ú����ҳ");
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
	 * �֧��¡�ç�����ҳ����� ID �ç�Ѻ����ͧ���
	 * @param aBudgetItemId ID �ͧ��¡�ç�����ҳ
	 * @return ��¡�ç�����ҳ����;���������Ǵ��ѡ�����Ǵ���¢ͧ��¡�ç�����ҳ��鹴���  �������ͨ��繤�� null
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧��������¡�ç�����ҳ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * �ѹ�֡��͹������ҳ �������͹����зӡ�á�˹��Ţ�����͹����ѵ��ѵ�
	 * @param aBudgetTransfer ��͹������ҳ����ͧ��úѹ�֡
	 * @return ��͹������ҳ���١�ѹ�֡���º����
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��úѹ�֡��¡�â��͹������ҳ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	/**
	 * �֧��¡�â��͹������ҳ��Шӻ�
	 * @param aBudgetYear �է�����ҳ����ͧ���
	 * @return ��¡�â��͹������ҳ����������Ǵ������ҳ�����͹�������͹�͡
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧��¡�â��͹������ҳ");
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
			
			BudgetItem transferInBudgetItem = aBudgetTransfer.getToBudgetItem(); // ��Ǵ������ҳ����ͧ����͹���
			BudgetItem transferOutBudgetItem = aBudgetTransfer.getFromBudgetItem(); // ��Ǵ������ҳ����ͧ����͹�͡
			BudgetItem oldTransferOutBudgetItem = aBudgetTransfer.getOldFromBudgetItem();
			BudgetItem closestControlledBudgetItem; // ��Ǵ����ա�äǺ���������ҳ
			BigDecimal transferOutAvailableAmount ; // �ӹǹ�Թ���Ǵ����ѧ����ö��������
			BigDecimal transferAmount; // �ӹǹ�Թ����ͧ����͹
			
			// ��Ǩ�ͺ�������ö�͹���͡���������
			closestControlledBudgetItem = closestControlledBudgetItem(session, transferOutBudgetItem);
			transferAmount = aBudgetTransfer.getApproveAmount();
			if (closestControlledBudgetItem == null ){
				// ����ա�äǺ���������ҳ
				transferOutAvailableAmount = BigDecimal.valueOf(Double.MAX_VALUE);
			} else {
				// �ա�äǺ���������ҳ
				transferOutAvailableAmount = closestControlledBudgetItem.getAvailableAmount();
				if (transferAmount.compareTo(transferOutAvailableAmount) == 1 ) {
					// �ӹǹ�Թ�����͹�͡�ҡ���Ҩӹǹ�Թ�������ö����
					StringBuffer buff = new StringBuffer();
					buff.append("�������ö����¡�������ͧ�ҡ�ա�èӡѴǧ�Թ������ҳ�����Ǵ ").append(closestControlledBudgetItem).append("\n");
					buff.append("�ӹǹ�Թ�������ö�͹�� = ").append(transferOutAvailableAmount).append("\n");
					buff.append("�ӹǹ�Թ����ͧ����͹ = ").append(transferAmount);
					throw new ControllerException(buff.toString());			
				}
			}
			// ��Ѻ�ʹ����͹�������͹�͡�ͧ��Ǵ������ҳ
			//transferOutBudgetItem.transferOut(transferAmount);
			//transferInBudgetItem.transferIn(transferAmount);
			// ��Ѻ��ا�ʹ�������§�����ҳ����Ѻ��Ǵ��ѡ
			//expenseBudget(session, transferOutBudgetItem.getParentBudgetItem(), transferAmount);
			BudgetItem budgetItem = (BudgetItem) session.load(BudgetItem.class, transferOutBudgetItem.getId());
			BudgetItem oldFromBudgetItem = (BudgetItem) session.load(BudgetItem.class, oldTransferOutBudgetItem.getId());
			
			//expenseBudget(session, budgetItem, transferAmount);
			// �ѹ�֡������ҳ
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��úѹ�֡��¡�â��͹������ҳ");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧��������¡�ç�����ҳ");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����Ť�������");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����Ť�������");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����Ť�������");
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
			
			// ��Ǩ�ͺ�������ö�駺����ҳ���������
			BudgetItem expensedBudgetItem = aBudgetExpense.getBudgetItem();
			BudgetItem closestControlledBudgetItem; // ��Ǵ����ա�äǺ���������ҳ
			BigDecimal availableAmount; // �ӹǹ�Թ�������ö����
			BigDecimal expensedAmount; // �ӹǹ�Թ����ͧ�����

			closestControlledBudgetItem = closestControlledBudgetItem(session, expensedBudgetItem);
			expensedAmount = aBudgetExpense.getAmount();
			if (closestControlledBudgetItem != null ){
				availableAmount = closestControlledBudgetItem.getAvailableAmount();
				if (expensedAmount.compareTo(availableAmount) == 1 ) {
					// �ӹǹ�Թ�����͹�͡�ҡ���Ҩӹǹ�Թ�������ö����
					StringBuffer buff = new StringBuffer();
					buff.append("�������ö����¡�������ͧ�ҡ�ա�èӡѴǧ�Թ������ҳ�����Ǵ ").append(closestControlledBudgetItem).append("\n");
					buff.append("�ӹǹ�Թ�������ö���� = ").append(availableAmount).append("\n");
					buff.append("�ӹǹ�Թ����ͧ����� = ").append(expensedAmount);
					throw new ControllerException(buff.toString());			
				}
			}
			// ��Ѻ�ʹ����駺����ҳ
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��úѹ�֡��������");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ���ź�����Ť�������");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ���ź�����Ť�������");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ���ź�����Ť�������");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧��������ҹ���");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧��������ҹ���");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	/**
	 * ��Ѻ��ا�ʹ�������§�����ҳ
	 * @param session
	 * @param aBudgetItem ��Ǵ������ҳ����ͧ��û�Ѻ��ا�ʹ����駺����ҳ
	 * @param aExpensedAmount �ӹǹ�Թ����ա���駺����ҳ
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
	 * ��Ѻ��ا�ʹ��èͧ������ҳ
	 * @param session
	 * @param aBudgetItem ��Ǵ������ҳ����ͧ��û�Ѻ��ا�ʹ��èͧ
	 * @param aReservedAmount �ӹǹ�Թ����ա�èͧ������ҳ
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
	 * ����Ǵ������ҳ��Ǵ����ա�äǺ���������ҳ
	 * @param session
	 * @param aBudgetItem
	 * @return 1.��Ǵ������ҳ����ա�äǺ���   2.null ����ҡ�������Ǵ������ҳ����ա�äǺ���������ҳ
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
			// ����ش��§�����ҳ�������������Ǵ����ա�äǺ���
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����ŧ�����ҳ");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����ŧ�����ҳ");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����ŧ�����ҳ");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����š���͹������ҳ");
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
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��ô֧�����š���͹������ҳ");
		} finally {
			session.clear();
			session.close();
		}
	}
}
