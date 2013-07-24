package welfare.persistent.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import welfare.persistent.domain.security.Authorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;

public class StockAdminController {
	
	@SuppressWarnings("unchecked")
	public ArrayList<User> getSubSystemUserList(String aMainSystemName, String aSubSytemName) throws ControllerException{
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
					"AND user.mainSystemName = :pmsysname " +
					"AND user.warehouseCode = :pssysname " +
					"ORDER BY user.userType, user.username " )
					.setParameter("pmsysname", aMainSystemName)
					.setParameter("pssysname", aSubSytemName)
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
					"AND user.mainSystemName = 'SYS_MAIN_STOCK' ")
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
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกรายการผู้ใช้");
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
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกรายการผู้ใช้");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
}
