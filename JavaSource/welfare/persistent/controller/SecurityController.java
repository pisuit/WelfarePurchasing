package welfare.persistent.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.lowagie.tools.concat_pdf;

import welfare.persistent.domain.hr.Employee;
import welfare.persistent.domain.security.Authorization;
import welfare.persistent.domain.security.User;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;
import welfare.utils.Constants;

public class SecurityController {
	
	@SuppressWarnings("unchecked")
	public Authorization[] getAuthorizations(String aUsername, String aMainSystemName, String aSubSystemName) throws ControllerException {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Authorization> list = session.createQuery(
					"SELECT DISTINCT auth " +
					"FROM Authorization auth " +
					"	left join fetch auth.user user " +
					"WHERE " +
					"	user.username = :pusername " +
					"AND user.mainSystemName = :pmsystemname " +
					"AND user.subSystemName = :pssystemname " +
					"AND user.status = 'NORMAL' ")
					.setParameter("pusername", aUsername)
					.setParameter("pmsystemname", aMainSystemName)
					.setParameter("pssystemname", aSubSystemName)
					.list();
			
			tx.commit();
//			if (aUsername.equals("pisuityo")) {
//				Authorization[] authorizations = list.toArray(new Authorization[list.size()+1]);
//				Authorization authorization = new Authorization();
//				authorization.setSystemRole(Constants.ROLE_LOG);
//				authorizations[authorizations.length-1] = authorization;
//				return authorizations;
//			} else {
				Authorization[] authorizations = list.toArray(new Authorization[list.size()]);				
				return authorizations;
//			}
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
	
	public User getUser(String aUsername, String aMainSystemName, String aSubSystemName) throws ControllerException {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			User user = (User) session.createQuery(
					"SELECT DISTINCT user " +
					"FROM User user left join fetch user.authorizations " +
					"WHERE 	" +
					"	user.username = :pusername " +
					"AND user.mainSystemName = :pmsystemname " +
					"AND user.subSystemName = :pssystemname " +
					"AND user.status = 'NORMAL' ")
					.setParameter("pusername", aUsername)
					.setParameter("pmsystemname", aMainSystemName)
					.setParameter("pssystemname", aSubSystemName)
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
	public ArrayList<Authorization> getAuthorization(String role) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Authorization> auth = session.createQuery(
					"SELECT DISTINCT auth " +
					"FROM Authorization auth " +
					"WHERE auth.systemRole = :prole")
					.setParameter("prole", role)
					.list();
			tx.commit();
			return new ArrayList<Authorization>(auth);
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
}
