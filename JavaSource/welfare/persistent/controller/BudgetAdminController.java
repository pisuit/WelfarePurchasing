package welfare.persistent.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.security.Authorization;
import welfare.persistent.domain.security.BudgetAuthorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;

public class BudgetAdminController {

	@SuppressWarnings("unchecked")
	public ArrayList<User> getMainSystemUserList(String aMainSystemName, String aSubSystemName) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<User> userList = session.createQuery(
					"SELECT distinct user " +
					"FROM " +
					"User user " +
					"	left join fetch user.authorizations auth " +
					"WHERE " +
					"	user.status = 'NORMAL' " +
					"AND user.mainSystemName = :pmsystemname " +
					"AND user.subSystemName = :pssystemname " +
					"ORDER BY user.userType, user.username " )
					.setParameter("pmsystemname", aMainSystemName)
					.setParameter("pssystemname", aSubSystemName)
					.list();
			tx.commit();			
			return new ArrayList<User>(userList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลผู้ใช้งาน");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	public void saveUser(User aUser, ArrayList<Authorization> aAuthorizationList) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(aUser);
			for (Authorization authorization : aUser.getAuthorizations()) {
				session.delete(authorization);
			}
			Authorization authorization;
			for (Authorization newAuthorization: aAuthorizationList) {
				session.save(newAuthorization);
			}
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
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

	public void deleteUser(User aUser) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(aUser);
			
			for (Authorization authorization : aUser.getAuthorizations()) {
				session.delete(authorization);
			}
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
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
	public User checkDuplicateUser (String aUsername) throws ControllerException {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			User user = (User)session.createQuery(
					"SELECT DISTINCT user " +
					"FROM User user " +
					"WHERE " +
					"user.username = :pusername " +
					"AND user.status = 'NORMAL' " +
					"AND user.mainSystemName = 'SYS_MAIN_BUDGET'")
					.setParameter("pusername", aUsername)
					.uniqueResult();
			tx.commit();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูล");
		} finally {
			session.clear();
			session.close();
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<BudgetItem> getBudgetItemList(int budgetYear) throws ControllerException {
		SessionFactory sf  = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<BudgetItem> budgetList = session.createQuery(
					"SELECT DISTINCT budgetItem " +
					"FROM BudgetItem budgetItem " +
					"left join fetch budgetItem.budget budget " +
					"WHERE " +
					"budgetItem.status = 'NORMAL' " +
					"AND budget.budgetYear = :pbudgetyear " +
					"ORDER BY budgetItem.accountCode")
					.setParameter("pbudgetyear", budgetYear)
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
	public ArrayList<User> getPurchasingUserList(String aMainSystemName) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<User> userList = session.createQuery(
					"SELECT distinct user " +
					"FROM " +
					"User user " +
					"	left join fetch user.budgetAuthorizations budgetAuth " +
					"WHERE " +
					"	user.status = 'NORMAL' " +
					"AND user.mainSystemName = :pmsystemname " +
					"ORDER BY user.username,user.subSystemName " )
					.setParameter("pmsystemname", aMainSystemName)
					.list();
			tx.commit();
			return new ArrayList<User>(userList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลผู้ใช้งาน");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	public void saveBudgetAuthorization(User aUser, ArrayList<BudgetAuthorization> aBudgetAuthorizationList) throws ControllerException {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			for (BudgetAuthorization budgetAuthorization : aUser.getBudgetAuthorizations()) {
				session.delete(budgetAuthorization);
				System.out.println("Delete "+budgetAuthorization.getBudgetAuth());
			}
			BudgetAuthorization budgetAuthorization;
			for (BudgetAuthorization newBudgetAuthorization: aBudgetAuthorizationList) {
				session.save(newBudgetAuthorization);
				System.out.println("Save "+newBudgetAuthorization.getBudgetAuth());
			}		
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
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
}