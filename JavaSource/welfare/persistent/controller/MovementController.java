package welfare.persistent.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import welfare.persistent.customtype.StorageLocation;
import welfare.persistent.domain.purchasing.PurchaseRequisition;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.StockMovement;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;

public class MovementController {
	@SuppressWarnings("unchecked")
	public ArrayList<StockMovement> getStockMovements(String aWarehouseCode, StorageLocation aStorageLocation, Material aMaterial) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<StockMovement> movementList = session.createQuery(
					"SELECT DISTINCT mm " +
					"FROM StockMovement mm " +
					"	left join fetch mm.material mat " +
					"	left join fetch mat.materialGroup matGroup " +
					"WHERE " +
					"	mm.warehouse = :pwhcode " +
					"AND mm.storageLocation = :pstorage " +
					"AND mm.material = :pmaterial " +
					"ORDER BY mm.movementDate desc " )
					.setParameter("pwhcode", aWarehouseCode)
					.setParameter("pstorage", aStorageLocation)
					.setParameter("pmaterial", aMaterial)
					.list();
			tx.commit();
			return new ArrayList<StockMovement>(movementList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบแจ้งจัดหา");
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

	@SuppressWarnings("unchecked")
	public ArrayList<StockMovement> getStockMovements(String aWarehouseCode, StorageLocation aStorageLocation, Material aMaterial, Date aStartDate, Date aEndDate ) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<StockMovement> movementList = session.createQuery(
					"SELECT DISTINCT mm " +
					"FROM StockMovement mm " +
					"	left join fetch mm.material mat " +
					"	left join fetch mat.materialGroup matGroup " +
					"WHERE " +
					"	mm.warehouse = :pwhcode " +
					"AND mm.storageLocation = :pstorage " +
					"AND mm.material = :pmaterial " +
					"AND mm.movementDate between :pstdate and :pendate " +
					"ORDER BY mm.movementDate desc " )
					.setParameter("pwhcode", aWarehouseCode)
					.setParameter("pstorage", aStorageLocation)
					.setParameter("pmaterial", aMaterial)
					.setParameter("pstdate", aStartDate)
					.setParameter("pendate", aEndDate)
					.list();
			tx.commit();
			return new ArrayList<StockMovement>(movementList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบแจ้งจัดหา");
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
