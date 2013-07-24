package welfare.persistent.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import welfare.persistent.customtype.LogType;
import welfare.persistent.domain.security.Log;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;

public class LogController {
	
	public void logRecord(Log aLog) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(aLog);
			
			tx.commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึก Log");
		} finally {
			session.clear();
			session.close();
		}
	}
		
//	@SuppressWarnings("unchecked")
//	public ArrayList<Log> getLogList() throws ControllerException{
//		SessionFactory sf = HibernateUtil.getSessionFactory();
//		Session session = null;
//		Transaction tx = null;		
//		try {
//			session = sf.openSession();
//			tx = session.beginTransaction();
//			
//			List<Log> logList = session.createQuery(
//					"SELECT log " +
//					"FROM Log log " +
//					"left join fetch log.user user " +
//					"WHERE TRUNC(log.dateTime) >= TRUNC(add_months(sysdate,-1)) " +
//					"ORDER BY log.dateTime desc" )
//					.list();
//			tx.commit();
//			return new ArrayList<Log>(logList);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			if (tx != null) {
//				tx.rollback();
//			}
//			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูล Log");
//		} finally {
//			session.clear();
//			session.close();
//		}
//	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Log> getLogList() throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;		
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Log> logList = session.createQuery(
					"SELECT log " +
					"FROM Log log " +
					"left join fetch log.user user " +
					"ORDER BY log.dateTime desc" )
					.list();
			tx.commit();
			return new ArrayList<Log>(logList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูล Log");
		} finally {
			session.clear();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Log> searchLog(String userName, Date fromDate, Date toDate, LogType logType) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;	
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Log> logList = session.createQuery(
					"SELECT log " +
					"FROM Log log " +
					"left join fetch log.user user " +
					"WHERE (user.username = :pusername OR :pusername IS NULL) " +
					"AND (log.logType = :plogtype OR :plogtype IS NULL) " +
					"AND (TRUNC(log.dateTime) >= :pfromdate OR :pfromdate IS NULL) " +
					"AND (TRUNC(log.dateTime) <= :ptodate OR :ptodate IS NULL) ")
					.setParameter("pusername", userName)
					.setParameter("plogtype", logType)
					.setParameter("pfromdate", fromDate)
					.setParameter("ptodate", toDate)
					.list();
			
			tx.commit();
			return new ArrayList<Log>(logList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูล Log");
		} finally {
			session.clear();
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Log> getSearchedUser(String username) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Log> logList = session.createQuery(
					"SELECT log " +
					"FROM Log log " +
					"left join fetch log.user user " +
					"WHERE user.username = :pusername " +
					"ORDER BY log.dateTime desc" )
					.setParameter("pusername", username)
					.list();
			tx.commit();
			return new ArrayList<Log>(logList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูล Log");
		} finally {
			session.clear();
			session.close();
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<Log> getSearchedType(LogType logtype) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Log> logList = session.createQuery(
					"SELECT log " +
					"FROM Log log " +
					"left join fetch log.user user " +
					"WHERE log.logType = :plogtype " +
					"ORDER BY log.dateTime desc" )
					.setParameter("plogtype", logtype)
					.list();
			tx.commit();
			return new ArrayList<Log>(logList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูล Log");
		} finally {
			session.clear();
			session.close();
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<Log> getSearchedTime(Timestamp fromDateTime, Timestamp toDateTime) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Log> logList = session.createQuery(
					"SELECT log " +
					"FROM Log log " +
					"left join fetch log.user user " +
					"WHERE (log.dateTime > :pfromdatetime " +
					"OR log.dateTime = :pfromdatetime) " +
					"AND (log.dateTime < :ptodatetime " +
					"OR log.dateTime = :ptodatetime) " +
					"ORDER BY log.dateTime desc" )
					.setParameter("pfromdatetime", fromDateTime)
					.setParameter("ptodatetime", toDateTime)
					.list();
			tx.commit();
			return new ArrayList<Log>(logList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูล Log");
		} finally {
			session.clear();
			session.close();
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<Log> deleteOldLog(Timestamp date) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Log> logDeletedList = session.createQuery(
					"SELECT log " +
					"FROM Log log " +
					"WHERE log.dateTime < :pdate")
					.setParameter("pdate", date)
					.list();
			
			for (Log log : logDeletedList){
				session.delete(log);
			}
			
			tx.commit();
			
			List<Log> logList = getLogList();
			
			return new ArrayList<Log>(logList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูล Log");
		} finally {
			session.clear();
			session.close();
		}
	}
}
