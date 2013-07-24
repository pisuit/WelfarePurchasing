package welfare.persistent.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import welfare.persistent.customtype.LotStatus;
import welfare.persistent.customtype.StorageLocation;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.MaterialLot;
import welfare.persistent.domain.stock.StockMovement;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;

public class LotController {
	
	@SuppressWarnings("unchecked")
	public BigDecimal getAvailableQuantity(String aWarehouseCode, StorageLocation aStorageLocation, Material aMaterial) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<MaterialLot> movementList = session.createQuery(
					"SELECT DISTINCT matLot " +
					"FROM MaterialLot matLot " +
					"WHERE " +
					"	matLot.warehouse = :pwhcode " +
					"AND matLot.storageLocation = :pstorage " +
					"AND matLot.material = :pmaterial " +
					"AND matLot.status = 'OPEN' " +
					"ORDER BY matLot.createdDate desc " )
					.setParameter("pwhcode", aWarehouseCode)
					.setParameter("pstorage", aStorageLocation)
					.setParameter("pmaterial", aMaterial)
					.list();
			
			BigDecimal availableQty = new BigDecimal("0.00");
			for (MaterialLot materialLot : movementList) {
				if (materialLot.getStatus().equals(LotStatus.OPEN)){
					availableQty = availableQty.add(materialLot.getAvailableQty());
				}
			}
			
			tx.commit();
			return availableQty;
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
