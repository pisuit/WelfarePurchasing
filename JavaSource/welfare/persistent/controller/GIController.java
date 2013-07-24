package welfare.persistent.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import welfare.persistent.domain.purchasing.GoodsReceipt;
import welfare.persistent.domain.stock.GoodsIssue;
import welfare.persistent.domain.stock.GoodsIssueItem;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;

public class GIController {
	public ArrayList<GoodsIssue> getGoodsIssues(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<GoodsIssue> grList = session.createQuery(
					"SELECT DISTINCT gi " +
					"FROM GoodsReceipt gi " +
					"WHERE " +
					"	gi.warehouseCode = :pwhcode " +
					"ORDER BY gr.giNumber " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<GoodsIssue>(grList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบเบิก");
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

	public GoodsIssue getGoodsIssue(Long aGoodsReceiptID) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			GoodsIssue gr = (GoodsIssue) session.createQuery(
					"SELECT DISTINCT gi " +
					"FROM GoodsIssue gi " +
					"	left join fetch gi.goodsIssueItems giItem " +
					"WHERE " +
					"	gi.id = :pid " +
					"ORDER BY giItem.itemNumber " )
					.setParameter("pid", aGoodsReceiptID)
					.uniqueResult();
			tx.commit();
			return gr;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบเบิก");
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

	public GoodsIssue saveGoodsIssue(GoodsIssue aGoodsIssue) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		int nextGINumber = getNextGoodsIssueNo();
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if (aGoodsIssue.getId() == null){
				aGoodsIssue.setGiNumber(nextGINumber);
			}			
			session.saveOrUpdate(aGoodsIssue);
			
			for (GoodsIssueItem giItem : aGoodsIssue.getGoodsIssueItems()) {
				session.saveOrUpdate(giItem);
			}
			
			tx.commit();
			return aGoodsIssue;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบเบิก");
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

	public int getNextGoodsIssueNo() throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Integer maxPoNumber = (Integer) session.createQuery(
					"SELECT MAX(gi.giNumber)" +
					"FROM GoodsIssue gi ")
			.uniqueResult();
			
			if (maxPoNumber == null ){
				maxPoNumber = Integer.valueOf(1);
			} else {
				maxPoNumber = Integer.valueOf(maxPoNumber.intValue()+1);
			}
			tx.commit();
			return maxPoNumber;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการกำหนดหมายเลย GI");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<String> getIssuerNames(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT gi.issuerName " +
					"FROM GoodsIsssue gi " +
					"WHERE " +
					"	gi.warehouse = :pwhcode " +
					"ORDER BY gi.issuerName " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<String>(list);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงตำแหน่งผู้แจ้งจัดหา");
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


}
