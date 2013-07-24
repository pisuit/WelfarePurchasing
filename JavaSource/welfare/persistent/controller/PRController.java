package welfare.persistent.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.lowagie.text.Watermark;

import welfare.persistent.customtype.PRStatus;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.purchasing.PurchaseRequisition;
import welfare.persistent.domain.purchasing.PurchaseRequisitionItem;
import welfare.persistent.domain.stock.MaterialGroup;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;

public class PRController {
	
	private BudgetController budgetController = new BudgetController();
	
	@SuppressWarnings("unchecked")
	public ArrayList<PurchaseRequisition> getPurchaseRequisitions(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<PurchaseRequisition> prList = session.createQuery(
					"SELECT DISTINCT pr " +
					"FROM PurchaseRequisition pr " +
					"WHERE " +
					"	pr.warehouseCode = :pwhcode " +
					"ORDER BY pr.id desc" )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<PurchaseRequisition>(prList);
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
	public ArrayList<PurchaseRequisition> getOpenPurchaseRequisitions(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<PurchaseRequisition> prList = session.createQuery(
					"SELECT DISTINCT pr " +
					"FROM PurchaseRequisition pr " +
					"WHERE " +
					"	pr.warehouseCode = :pwhcode " +
					"AND pr.status = 'OPEN' " +
					"ORDER BY pr.prNumber " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			System.out.println("Open PR List : "+prList.size());
			tx.commit();
			return new ArrayList<PurchaseRequisition>(prList);
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
	
	public PurchaseRequisition getPurchaseRequisition(Long aPurchaseRequisitionID) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			PurchaseRequisition pr = (PurchaseRequisition) session.createQuery(
					"SELECT DISTINCT pr " +
					"FROM PurchaseRequisition pr " +
					"	left join fetch pr.purchaseRequisitionItems prItem " +
					"	left join fetch prItem.material mat " +
					"	left join fetch prItem.budgetItem budItem " +
					"WHERE " +
					"	pr.id = :pid " +
					"ORDER BY prItem.itemNumber " )
					.setParameter("pid", aPurchaseRequisitionID)
					.uniqueResult();
			tx.commit();
			return pr;
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
	
	public PurchaseRequisition savePurchaseRequisition(PurchaseRequisition aPurchaseRequisition) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			// TODO ถ้าเป็นสถานะ CLOSED ให้ทำอย่างไร
			
			session.saveOrUpdate(aPurchaseRequisition);
			
			if (PRStatus.DELETED.equals(aPurchaseRequisition.getStatus())){
				session.flush();
				session.clear(); // ลบ object ออกจาก session ก่อนทำการแก้ไข budget item 
				// คืนงบประมาณทั้งหมด
				BigDecimal reservedAmount = new BigDecimal("0.00");
				BigDecimal minus = new BigDecimal("-1.00");
				PurchaseRequisition pr = (PurchaseRequisition) session.createQuery(
						"SELECT DISTINCT pr " +
						"FROM PurchaseRequisition pr " +
						"	left join fetch pr.purchaseRequisitionItems prItem " +
						"	left join fetch prItem.material mat " +
						"	left join fetch prItem.budgetItem budItem " +
						"WHERE " +
						"	pr.id = :pid " +
						"ORDER BY prItem.itemNumber " )
						.setParameter("pid", aPurchaseRequisition.getId())
						.uniqueResult();
				for (PurchaseRequisitionItem prItem : pr.getPurchaseRequisitionItems()) {
					// คืนงบประมาณ
					reservedAmount = prItem.getBudgetReservedAmount();
					reservedAmount = reservedAmount.multiply(minus);
					System.out.println("cash return = "+reservedAmount);
					budgetController.reserveBudget(session, prItem.getBudgetItem(), reservedAmount);
					session.flush();
					session.clear();
				}
				aPurchaseRequisition = pr;
			} else {
				BigDecimal reservedAmount = new BigDecimal("0.00");
				BigDecimal minus = new BigDecimal("-1.00");
				BigDecimal grandTotalPrice = new BigDecimal("0.00");
				BigDecimal totalPrice = new BigDecimal("0.00");
				BigDecimal budgetAvailableAmount;
				List<PurchaseRequisitionItem> itemList = aPurchaseRequisition.getPurchaseRequisitionItems();
				int itemNo = 1;
				BudgetItem closestBudgetItem;
				BudgetItem budgetItem;
				for (PurchaseRequisitionItem prItem : itemList) {
					//if (PurchaseRequisitionItem.STATUS_DELETED.equals(prItem.getStatus())){
					if (Status.DELETED.equals(prItem.getStatus())){
						// คืนงบประมาณ
						reservedAmount = prItem.getBudgetReservedAmount();
						reservedAmount = reservedAmount.multiply(minus);
						budgetController.reserveBudget(session, prItem.getBudgetItem(), reservedAmount);
						
						session.delete(prItem);
					} else {
						// ตรวจสอบจำนวนคงเหลือของงบประมาณ
						budgetItem =  (BudgetItem) session.load(BudgetItem.class, prItem.getBudgetItem().getId());
						
						totalPrice = prItem.getTotalPrice();
						closestBudgetItem = budgetController.closestControlledBudgetItem(session, prItem.getBudgetItem());
						if (closestBudgetItem != null ){
							// มีการคุมงบประมาณ
							budgetAvailableAmount = closestBudgetItem.getAvailableAmount();
							budgetAvailableAmount = budgetAvailableAmount.add(prItem.getBudgetReservedAmount()).subtract(prItem.getTotalPrice());
							if (budgetAvailableAmount.doubleValue() < 0) {
								DecimalFormat df = new DecimalFormat("#,##0.00");
								throw new ControllerException("จำนวนงบประมาณที่ท่านต้องการใช้ เกินงบ = "+ df.format(budgetAvailableAmount.doubleValue()*-1.00)+" ("+budgetItem.getCategory()+")") ;
							}
						}
						if (prItem.getId() == null ){
							// จองงบประมาณ
							reservedAmount = prItem.getTotalPrice();
							budgetController.reserveBudget(session, budgetItem, reservedAmount);
							// รายการใหม่
							prItem.setItemNumber(itemNo++);
							prItem.setBudgetReservedAmount(prItem.getTotalPrice());
							session.saveOrUpdate(prItem);
						} else {
							// จองงบประมาณ
							reservedAmount = totalPrice.subtract(prItem.getBudgetReservedAmount());
							budgetController.reserveBudget(session, budgetItem, reservedAmount);
							// ปรับปรุงรายการเดิม
							prItem.setItemNumber(itemNo++);
							prItem.setBudgetReservedAmount(prItem.getTotalPrice());
							session.saveOrUpdate(prItem);
						}
						// รวมจำนวนเงินแต่ละรายการเพื่อไปปรับปรุงใบแจ้ง
						grandTotalPrice = grandTotalPrice.add(totalPrice);
					}
					session.flush();
					session.clear();
				}
				aPurchaseRequisition.setTotalPrice(grandTotalPrice);
				session.saveOrUpdate(aPurchaseRequisition);
			}
						
			tx.commit();
			return aPurchaseRequisition;
		} catch (ControllerException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกข้อมูลใบแจ้ง");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getRequisitionerNames(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT pr.requisitionerName " +
					"FROM PurchaseRequisition pr " +
					"WHERE " +
					"	pr.warehouseCode = :pwhcode " +
					"AND pr.requisitionerName IS NOT NULL " +
					"ORDER BY pr.requisitionerName " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<String>(list);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงรายชื่อผู้แจ้งจัดหา");
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
	public ArrayList<String> getRequisitionerPos(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT pr.requisitionerPos " +
					"FROM PurchaseRequisition pr " +
					"WHERE " +
					"	pr.warehouseCode = :pwhcode " +
					"AND pr.requisitionerPos IS NOT NULL " +
					"ORDER BY pr.requisitionerPos " )
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
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getInspectorNames(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT pr.inspectorName " +
					"FROM PurchaseRequisition pr " +
					"WHERE " +
					"	pr.warehouseCode = :pwhcode " +
					"AND pr.inspectorName IS NOT NULL " +
					"ORDER BY pr.inspectorName " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<String>(list);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงรายชื่อผู้แจ้งจัดหา");
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
	public ArrayList<String> getInspectorPos(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT pr.inspectorPos " +
					"FROM PurchaseRequisition pr " +
					"WHERE " +
					"	pr.warehouseCode = :pwhcode " +
					"AND pr.inspectorPos IS NOT NULL " +
					"ORDER BY pr.inspectorPos " )
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
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getApproverNames(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT pr.approverName " +
					"FROM PurchaseRequisition pr " +
					"WHERE " +
					"	pr.warehouseCode = :pwhcode " +
					"AND pr.approverName IS NOT NULL " +
					"ORDER BY pr.approverName " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<String>(list);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงรายชื่อผู้แจ้งจัดหา");
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
	public ArrayList<String> getApproverPos(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT pr.approverPos " +
					"FROM PurchaseRequisition pr " +
					"WHERE " +
					"	pr.warehouseCode = :pwhcode " +
					"AND pr.approverPos IS NOT NULL " +
					"ORDER BY pr.approverPos " )
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

	/**
	 * บันทีกรายการในใบแจ้งจัดหา
	 * @param aPurchaseRequisitionItem รายการที่ต้องการบันทึก
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	public void savePurchaseRequisitionItem(PurchaseRequisitionItem aPurchaseRequisitionItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if (aPurchaseRequisitionItem.getStatus().equals(PurchaseRequisitionItem.STATUS_DELETED)){
				session.delete(aPurchaseRequisitionItem);
			} else {
				session.saveOrUpdate(aPurchaseRequisitionItem);
			}
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกข้อมูลใบแจ้ง");
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
	public void savePurchaseRequisitionItem(ArrayList<PurchaseRequisitionItem> aPurchaseRequisitionItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			int itemNo = 1;
			for (PurchaseRequisitionItem item : aPurchaseRequisitionItem) {
				if (item.getStatus().equals(PurchaseRequisitionItem.STATUS_DELETED)){
					session.delete(item);
				} else {
					item.setItemNumber(itemNo++);
					session.saveOrUpdate(item);
				}
			}
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกข้อมูลใบแจ้ง");
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
	public ArrayList<PurchaseRequisitionItem> getPurchaseRequisitionItems(PurchaseRequisition aPurchaseRequisition) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<PurchaseRequisitionItem> list = session.createQuery(
					"SELECT DISTINCT prItem " +
					"FROM PurchaseRequisitionItem prItem " +
					"	left join fetch prItem.purchaseRequisition pr " +
					"	left join fetch prItem.material mat " +
					"	left join fetch prItem.budgetItem budItem " +
					"WHERE " +
					"	pr = :ppr " +
					"ORDER BY prItem.itemNumber " )
					.setParameter("ppr", aPurchaseRequisition)
					.list();
			tx.commit();
			return new ArrayList<PurchaseRequisitionItem>(list);
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบแจ้งจัดหา");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	
	public int getNextPurchaseRequisitionNo(int budgetYear) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Integer maxPrNumber = (Integer) session.createQuery(
					"SELECT MAX(pr.prNumber)" +
					"FROM PurchaseRequisition pr " +
					"WHERE pr.budgetYear = :pbudgetyear ")
					.setParameter("pbudgetyear", budgetYear)
					.uniqueResult();
			
			if (maxPrNumber == null ){
				maxPrNumber = Integer.valueOf(1);
			} else {
				maxPrNumber = Integer.valueOf(maxPrNumber.intValue()+1);
			}
			tx.commit();
			return maxPrNumber;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการกำหนดหมายเลย PR");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
}
