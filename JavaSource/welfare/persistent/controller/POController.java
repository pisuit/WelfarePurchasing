package welfare.persistent.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import welfare.persistent.customtype.POStatus;
import welfare.persistent.customtype.POType;
import welfare.persistent.customtype.PRStatus;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.purchasing.PurchaseOrder;
import welfare.persistent.domain.purchasing.PurchaseOrderItem;
import welfare.persistent.domain.purchasing.PurchaseRequisition;
import welfare.persistent.domain.purchasing.PurchaseRequisitionItem;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;
import welfare.persistent.domain.purchasing.Vendor;
import welfare.utils.CalendarUtils;

public class POController {
	private BudgetController budgetController = new BudgetController();
	/**
	 * ดึงรายการใบ PO ทั้งหมด
	 * @param aWarehouseCode
	 * @return
	 * @throws ControllerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PurchaseOrder> getPurchaseOrders(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<PurchaseOrder> poList = session.createQuery(
					"SELECT DISTINCT po " +
					"FROM PurchaseOrder po " +
					"WHERE " +
					"	po.warehouseCode = :pwhcode " +
					"ORDER BY po.id desc" )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<PurchaseOrder>(poList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบจัดซื้อ");
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
	public ArrayList<PurchaseOrder> getOpenPurchaseOrders(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<PurchaseOrder> poList = session.createQuery(
					"SELECT DISTINCT po " +
					"FROM PurchaseOrder po " +
					"WHERE " +
					"	po.warehouseCode = :pwhcode " +
					"AND po.status in ( 'OPEN', 'GR-PAR') " +
					"ORDER BY po.poNumber " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<PurchaseOrder>(poList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบจัดซื้อ");
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
	
	public PurchaseOrder getPurchaseOrder(Long aPurchaseOrderID) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			PurchaseOrder po = (PurchaseOrder) session.createQuery(
					"SELECT DISTINCT po " +
					"FROM PurchaseOrder po " +
					"	left join fetch po.purchaseOrderItems poItem " +
					"	left join fetch poItem.material mat " +
					"	left join fetch poItem.budgetItem budItem " +
					"	left join fetch po.purchaseRequisition pr " +
					"WHERE " +
					"	po.id = :pid " +
					"ORDER BY poItem.itemNumber " )
					.setParameter("pid", aPurchaseOrderID)
					.uniqueResult();
			tx.commit();
			return po;
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
	
	public PurchaseOrder savePurchaseOrder(PurchaseOrder aPurchaseOrder) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		int nextPONumber = getNextPurchaseOrderNo();
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			if (aPurchaseOrder.getId() == null){
				// ใบใหม่
				aPurchaseOrder.setPoNumber(nextPONumber);
			}
			
			// ถ้าเป็นการสร้างจาก PR
			if (aPurchaseOrder.getPurchaseRequisition() != null ) {
				session.saveOrUpdate(aPurchaseOrder);
				// บันทึกทุกรายการย่อย
				for (PurchaseOrderItem poItem : aPurchaseOrder.getPurchaseOrderItems()) {
					if (poItem.getStatus().equals(Status.DELETED)) {
						session.delete(poItem);
					} else {
						session.saveOrUpdate(poItem);
					}
				}
				// เปลี่ยน status ของ PR ให้เป็น CLOSED
				PurchaseRequisition pr = aPurchaseOrder.getPurchaseRequisition();
				pr.setStatus(PRStatus.CLOSED);
				session.saveOrUpdate(pr);
			} else {
				if (aPurchaseOrder.getPoType().equals(POType.EXCHANGE)) {
					BigDecimal totalPrice = new BigDecimal("0.00");
					BigDecimal grandTotalPrice = new BigDecimal("0.00");
					//ถ้าเป็นการสร้างโดยไม่ผ่าน PR และเป็น exchange									
					for (PurchaseOrderItem poItem : aPurchaseOrder.getPurchaseOrderItems()) {
						if (poItem.getStatus().equals(Status.DELETED)) {
							session.delete(poItem);
						} else {
							totalPrice = poItem.getTotalPrice();
							grandTotalPrice = grandTotalPrice.add(totalPrice);
							session.saveOrUpdate(poItem);
						}
					}
					aPurchaseOrder.setTotalPrice(grandTotalPrice);
					session.saveOrUpdate(aPurchaseOrder);
				} else {
					//session.flush();
				    //session.clear();
					//สร้างโดยไม่ผ่าน PR และเป็น emergency
					BigDecimal reservedAmount = new BigDecimal("0.00");
					BigDecimal minus = new BigDecimal("-1.00");
					BigDecimal grandTotalPrice = new BigDecimal("0.00");
					BigDecimal totalPrice = new BigDecimal("0.00");
					BigDecimal budgetAvailableAmount;
					int itemNo = 1;
					BudgetItem budgetItem;
					BudgetItem closestBudgetItem;
					PurchaseOrderItem item;
					List<PurchaseOrderItem> itemList = aPurchaseOrder.getPurchaseOrderItems();
					for (PurchaseOrderItem poItem : itemList) {
						if (Status.DELETED.equals(poItem.getStatus())){
							// คืนงบประมาณ
							reservedAmount = poItem.getBudgetReservedAmount();
							reservedAmount = reservedAmount.multiply(minus);
							item = (PurchaseOrderItem) session.load(PurchaseOrderItem.class, poItem.getId());
							budgetController.reserveBudget(session, item.getBudgetItem(), reservedAmount);
							
							session.delete(item);
						} else {
							budgetItem =  (BudgetItem) session.load(BudgetItem.class, poItem.getBudgetItem().getId());
							
							totalPrice = poItem.getTotalPrice();
							closestBudgetItem = budgetController.closestControlledBudgetItem(session, poItem.getBudgetItem());
							if (closestBudgetItem != null ){
								// มีการคุมงบประมาณ
								budgetAvailableAmount = closestBudgetItem.getAvailableAmount();
								budgetAvailableAmount = budgetAvailableAmount.add(poItem.getBudgetReservedAmount()).subtract(poItem.getTotalPrice());
								if (budgetAvailableAmount.doubleValue() < 0) {
									DecimalFormat df = new DecimalFormat("#,##0.00");
									throw new ControllerException("จำนวนงบประมาณที่ท่านต้องการใช้เกินงบ = "+ df.format(budgetAvailableAmount.doubleValue()*-1.00)) ;
								}
							}
							if (poItem.getId() == null ){
								// จองงบประมาณ
								reservedAmount = poItem.getTotalPrice();
								budgetController.reserveBudget(session, budgetItem, reservedAmount);
								// รายการใหม่
								poItem.setItemNumber(itemNo++);
								poItem.setBudgetReservedAmount(poItem.getTotalPrice());
								session.saveOrUpdate(poItem);
							} else {
								// จองงบประมาณ
								reservedAmount = totalPrice.subtract(poItem.getBudgetReservedAmount());
								budgetController.reserveBudget(session, budgetItem, reservedAmount);
								// ปรับปรุงรายการเดิม
								poItem.setItemNumber(itemNo++);
								poItem.setBudgetReservedAmount(poItem.getTotalPrice());
								session.saveOrUpdate(poItem);
							}
							grandTotalPrice = grandTotalPrice.add(totalPrice);
						}
						//session.flush();
						//session.clear();
					}
					aPurchaseOrder.setTotalPrice(grandTotalPrice);
					session.saveOrUpdate(aPurchaseOrder);
				}							
			}						
			tx.commit();
			return aPurchaseOrder;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกข้อมูลใบจัดซื้อ");
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

	public PurchaseOrder deletePurchaseOrder(PurchaseOrder aPurchaseOrder) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			aPurchaseOrder =  (PurchaseOrder) session.load(PurchaseOrder.class, aPurchaseOrder.getId());
			if (aPurchaseOrder.totalReceiptQuantity().doubleValue() > 0) {
				throw new ControllerException("ไม่สามารถลบใบสั่งซื้อที่มีการรับของไปแล้ว ให้ใช้การปิดใบรับแทน");
			}
			aPurchaseOrder.setStatus(POStatus.DELETED);
			session.saveOrUpdate(aPurchaseOrder);
			
			if (aPurchaseOrder.getPurchaseRequisition() != null) {
				PurchaseRequisition pr = aPurchaseOrder.getPurchaseRequisition();
				pr.setStatus(PRStatus.OPEN);
				session.saveOrUpdate(pr);
			} else {
				BigDecimal reservedAmount = new BigDecimal("0.00");
				BigDecimal minus = new BigDecimal("-1.00");
				if (aPurchaseOrder.getPoType().equals(POType.PO_EMERGENCY)) {
					for (PurchaseOrderItem poItem : aPurchaseOrder.getPurchaseOrderItems()) {
						reservedAmount = poItem.getBudgetReservedAmount();
						reservedAmount = reservedAmount.multiply(minus);
						budgetController.reserveBudget(session, poItem.getBudgetItem(), reservedAmount);
						session.flush();
						session.clear();
					}
				}
			}
			tx.commit();
			return aPurchaseOrder;
		} catch (ControllerException e) {
			if (tx != null ) {
				tx.rollback();
			}
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกข้อมูลใบจัดซื้อ");
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
	
	public PurchaseOrder closePurchaseOrder(PurchaseOrder aPurchaseOrder) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		int nextPONumber = getNextPurchaseOrderNo();
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			aPurchaseOrder =  (PurchaseOrder) session.load(PurchaseOrder.class, aPurchaseOrder.getId());
			
			if (aPurchaseOrder.totalReceiptQuantity().doubleValue() == 0){
				throw new ControllerException("ไม่สามารถปิดใบสั่งซื้อที่ยังไม่ได้ทำการรับของได้ ให้ใช้วิธีการลบใบสั่งซื้อแทน");
			}
			
			aPurchaseOrder.setStatus(POStatus.CLOSED);
			session.saveOrUpdate(aPurchaseOrder);
			session.flush();
			
			// TODO คืนงบประมาณ
//			BudgetController budgetController = new BudgetController();
//			BudgetItem budgetItem;
//			BigDecimal budgetReturnAmt = new BigDecimal("0.00");
//			BigDecimal expensedAmt = new BigDecimal("0.00");
//			for (PurchaseOrderItem poItem : aPurchaseOrder.getPurchaseOrderItems()) {
//				budgetItem = (BudgetItem) session.load(BudgetItem.class, poItem.getBudgetItem().getId());
//				expensedAmt = poItem.getTotalPrice();
//				budgetReturnAmt = poItem.getBudgetReservedAmount().subtract(expensedAmt);
//				budgetController.reserveBudget(session, budgetItem, budgetReturnAmt);
//				session.flush();
//				session.clear();
//			}
			// TODO คืนงบประมาณ
			BudgetController budgetController = new BudgetController();
			BudgetItem budgetItem;
			BigDecimal minus = new BigDecimal("-1.00");
			for (PurchaseOrderItem poItem : aPurchaseOrder.getPurchaseOrderItems()) {
				budgetItem = (BudgetItem) session.load(BudgetItem.class, poItem.getBudgetItem().getId());
				budgetController.reserveBudget(session, budgetItem, poItem.getRemainQty().multiply(poItem.getUnitPrice().multiply(minus)));
//				budgetController.reserveBudget(session, budgetItem, poItem.getBudgetReservedAmount().multiply(minus));
				session.flush();
				session.clear();
			}
			
			tx.commit();
			return aPurchaseOrder;
		} catch (ControllerException e) {
			if (tx != null ) {
				tx.rollback();
			}
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกข้อมูลใบจัดซื้อ");
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
	
	public int getNextPurchaseOrderNo() throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		int budgetYear;
		Calendar currentCal = CalendarUtils.getDateInstance(CalendarUtils.LOCALE_TH);
		budgetYear = CalendarUtils.toFinancialYear(CalendarUtils.LOCALE_TH, CalendarUtils.LOCALE_TH, currentCal.getTime());
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Integer maxPoNumber = (Integer) session.createQuery(
					"SELECT MAX(po.poNumber)" +
					"FROM PurchaseOrder po " +
					"WHERE po.budgetYear = :pbudgetyear ")
					.setParameter("pbudgetyear", budgetYear)
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
			throw new ControllerException("เกิดความผิดพลาดระหว่างการกำหนดหมายเลย PR");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getBuyerNames(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT po.buyerName " +
					"FROM PurchaseOrder po " +
					"WHERE " +
					"	po.warehouseCode = :pwhcode " +
					"AND po.buyerName IS NOT NULL " +
					"ORDER BY po.buyerName " )
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
	public ArrayList<String> getReceiverNames(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT po.receiverName " +
					"FROM PurchaseOrder po " +
					"WHERE " +
					"	po.warehouseCode = :pwhcode " +
					"AND po.receiverName IS NOT NULL " +
					"ORDER BY po.receiverName " )
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
	public Vendor getVendorForReport(long poId) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Vendor vendor = (Vendor)session.createQuery(
					"SELECT vendor " +
					"FROM PurchaseOrder po " +
					"	inner join po.vendor vendor " +
					"WHERE " +
					"po.id = :ppoid")
					.setParameter("ppoid", poId)
					.uniqueResult();
			
			tx.commit();
			return vendor;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงร้านค้า");
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
	public ArrayList<String> getRequisitionerNames(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT pr.requisitionerName " +
					"FROM PurchaseOrder pr " +
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
					"FROM PurchaseOrder pr " +
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
					"FROM PurchaseOrder pr " +
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
					"FROM PurchaseOrder pr " +
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
					"FROM PurchaseOrder pr " +
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
					"FROM PurchaseOrder pr " +
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

}
