package welfare.persistent.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import welfare.persistent.domain.purchasing.Vendor;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;

public class VendorController {
	/**
	 * ����ª�����ҹ��ҷ�����
	 * @return ��ª�����ҹ��ҷ�����
	 * @throws ControllerException 
	 */
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
					"FROM Vendor vendor " +
					"WHERE vendor.status = 'NORMAL' " +
					"ORDER by vendor.status, vendor.vendorNumber " )
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

	/**
	 * ����ª�����ҹ��ҷ���ѧ��ҹ����
	 * @return ��ª�����ҹ��ҷ���ѧ��ҹ����
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Vendor> getActiveVendors() throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Vendor> vendorList = session.createQuery(
					"SELECT DISTINCT vendor " +
					"FROM Vendor vendor " +
					"WHERE" +
					"	vendor.status = 'NORMAL' " +
					"ORDER by vendor.status, vendor.vendorNumber " )
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
	
	/**
	 * �ѹ�֡��������ҹ���
	 * @param aVendor ��������ҹ��ҷ���ͧ��úѹ�֡
	 * @return ��������ҹ��ҷ��ѹ�֡����
	 * @throws ControllerException
	 */
	public Vendor saveVendor(Vendor aVendor) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		int nextVendorNumber = nextVendorNumber();
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if (aVendor.getId() == null ){
				aVendor.setVendorNumber(nextVendorNumber);
			}
			session.saveOrUpdate(aVendor);
			
			tx.commit();
			return aVendor;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��úѹ�֡��������ҹ���");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	public Vendor getVendor(Long aID) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Vendor vendor = (Vendor) session.createQuery(
					"SELECT DISTINCT vendor " +
					"FROM Vendor vendor " +
					"WHERE" +
					"	vendor.id = :pid" )
					.setParameter("pid", aID)
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
	 * ���Ţ�����ҹ����ӴѺ�Ѵ�
	 * @return �Ţ�����ҹ����ӴѺ�Ѵ�
	 * @throws ControllerException
	 */
	public int nextVendorNumber() throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Integer maxVendorNumber = (Integer) session.createQuery(
					"SELECT MAX(vendor.vendorNumber) " +
					"FROM Vendor vendor ")
					.uniqueResult();
			
			Integer nextVendorNumber = Integer.valueOf(1);
			if (maxVendorNumber != null ){
				nextVendorNumber = maxVendorNumber.intValue()+1;
			}
			
			tx.commit();
			return nextVendorNumber;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("�Դ�����Դ��Ҵ�����ҧ��á�˹���·����ҹ���");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}


}
