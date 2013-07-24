package welfare.persistent.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.dev.BiffViewer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import sun.text.normalizer.CharTrie.FriendAgent;

import welfare.persistent.customtype.GRStatus;
import welfare.persistent.customtype.LotStatus;
import welfare.persistent.customtype.MovementStatus;
import welfare.persistent.customtype.MovementType;
import welfare.persistent.customtype.POStatus;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.budget.BudgetItem;
import welfare.persistent.domain.purchasing.GoodsReceipt;
import welfare.persistent.domain.purchasing.GoodsReceiptItem;
import welfare.persistent.domain.purchasing.PurchaseOrder;
import welfare.persistent.domain.purchasing.PurchaseOrderItem;
import welfare.persistent.domain.stock.MaterialLot;
import welfare.persistent.domain.stock.StockMovement;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;
import welfare.utils.CalendarUtils;

public class GRController {
	
	@SuppressWarnings("unchecked")
	public ArrayList<GoodsReceipt> getGoodsReceipts(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<GoodsReceipt> grList = session.createQuery(
					"SELECT DISTINCT gr " +
					"FROM GoodsReceipt gr " +
					"	left join fetch gr.purchaseOrder po " +
					"WHERE " +
					"	gr.warehouseCode = :pwhcode " +
					"ORDER BY gr.id desc" )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<GoodsReceipt>(grList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบรับ");
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
	public ArrayList<GoodsReceipt> getGoodReceiptsLinkedWithBudgetItem(BudgetItem budgetItem) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<GoodsReceipt> grList = session.createQuery(
					"SELECT DISTINCT gr " +
					"FROM GoodsReceipt gr " +
					"	left join fetch gr.goodsReceiptItems grItem " +
					"	left join fetch grItem.budgetItem budgetItem " +
					"WHERE budgetItem = :pbudgetItem " +
					"AND gr.status in ('GR-FULL','GR-PAR','CLOSED')	" +
					"ORDER BY gr.grNumber desc ")
					.setParameter("pbudgetItem", budgetItem)
					.list();
			tx.commit();
			return new ArrayList<GoodsReceipt>(grList);					
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบรับ");
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
	public ArrayList<GoodsReceipt> getGoodReceiptsLinkedWithBudgetItemByDate(BudgetItem budgetItem, Date fromDate, Date toDate) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<GoodsReceipt> grList = session.createQuery(
					"SELECT DISTINCT gr " +
					"FROM GoodsReceipt gr " +
					"	left join fetch gr.goodsReceiptItems grItem " +
					"	left join fetch grItem.budgetItem budgetItem " +
					"WHERE budgetItem = :pbudgetItem 			" +
					"AND gr.status in ('GR-FULL','GR-PAR','CLOSED')					" +
					"AND (gr.postingDate > :pfromDate			" +
					"OR gr.postingDate = :pfromDate)				" +
					"AND (gr.postingDate < :ptoDate				" +
					"OR gr.postingDate = :ptoDate)				" +
					"ORDER BY gr.grNumber desc ")
					.setParameter("pbudgetItem", budgetItem)
					.setParameter("pfromDate", fromDate)
					.setParameter("ptoDate", toDate)
					.list();
			tx.commit();
			return new ArrayList<GoodsReceipt>(grList);					
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลใบรับ");
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
	
	public GoodsReceipt getGoodsReceipt(Long aGoodsReceiptID) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			GoodsReceipt gr = (GoodsReceipt) session.createQuery(
					"SELECT DISTINCT gr " +
					"FROM GoodsReceipt gr " +
					"	left join fetch gr.goodsReceiptItems grItem " +
					"	left join fetch grItem.material mat " +
					"	left join fetch grItem.budgetItem budItem " +
					"	left join fetch gr.purchaseOrder po " +
					"WHERE " +
					"	gr.id = :pid " +
					"ORDER BY grItem.itemNumber " )
					.setParameter("pid", aGoodsReceiptID)
					.uniqueResult();
			tx.commit();
			return gr;
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
	
	public GoodsReceipt saveGoodsReceipt(GoodsReceipt aGoodsReceipt) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		int nextGRNumber = getNextGoodsReceiptNo();
		boolean isNewGr = false;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			if (aGoodsReceipt.getId() == null){
				// ใบใหม่
				aGoodsReceipt.setGrNumber(nextGRNumber);
				isNewGr = true;
			}
			
			// ถ้าเป็นการสร้างจาก PO
			if (aGoodsReceipt.getPurchaseOrder() != null ) {
				session.saveOrUpdate(aGoodsReceipt);
				// บันทึกทุกรายการย่อย
				PurchaseOrder po = aGoodsReceipt.getPurchaseOrder();
				po = getPurchaseOrder(po.getId());
				List<PurchaseOrderItem> poItemList = po.getPurchaseOrderItems();
				BigDecimal remainQty = new BigDecimal("0.00");
				BigDecimal poReceivedQty = new BigDecimal("0.00");
				boolean isGrFull;
				for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
					// ปรับปรุงยอดจำนวนในใบสั่งซื้อ
					for (PurchaseOrderItem poItem : poItemList) {
						if (grItem.getItemNumber() == poItem.getItemNumber()) {
							// เลขไอเทมตรงกันให้ตรวจสอบ
							if (grItem.getId() == null ){
								// รายการใหม่
								remainQty = poItem.getQuantity().subtract(poItem.getReceivedQuantity());
							} else {
								// แก้ไขรายการเดิม
								if(grItem.getStatus().equals(Status.DELETED)){
									remainQty = poItem.getQuantity().add(grItem.getOldReceivedQty());
								} else {
									remainQty = poItem.getQuantity().subtract(poItem.getReceivedQuantity()).add(grItem.getOldReceivedQty());
								}
							}
							if (grItem.getReceivedQty().compareTo(remainQty) == 1) {
								throw new ControllerException("ไม่สามารถรับของเกินจำนวนในใบสั่งซื้อได้ ");
							}
							if (grItem.getStatus().equals(Status.DELETED)){
								poReceivedQty = poItem.getReceivedQuantity();
								poReceivedQty = poReceivedQty.subtract(grItem.getOldReceivedQty());
//								poItem.setBudgetReservedAmount(poItem.getQuantity().multiply(poItem.getUnitPrice()));
								System.out.println("case 1 : gritem deleted");
								poItem.setReceivedQuantity(poReceivedQty);
							} else {
								if (isNewGr){
								poReceivedQty = poItem.getReceivedQuantity();
								poReceivedQty = poReceivedQty.add(grItem.getReceivedQty());
//								poItem.setBudgetReservedAmount(budgetReservedAmount)
								poItem.setReceivedQuantity(poReceivedQty);
								} else {
									poReceivedQty = poItem.getReceivedQuantity();
									poReceivedQty = poReceivedQty.add(grItem.getReceivedQty()).subtract(grItem.getOldReceivedQty());
									poItem.setReceivedQuantity(poReceivedQty);
								}
							}
							session.saveOrUpdate(poItem);
						}
					}
					if(grItem.getStatus().equals(Status.DELETED)) {
						session.delete(grItem);
					} else {
						grItem.setOldReceivedQty(grItem.getReceivedQty());
						session.saveOrUpdate(grItem);
					}
				}
				
				// เปลี่ยน status ของ GR
				if (po.totalQuantity().compareTo(aGoodsReceipt.totalReceivedQuantity()) == 0){
					aGoodsReceipt.setStatus(GRStatus.GR_FULL);
				} else {
					aGoodsReceipt.setStatus(GRStatus.GR_PAR);
				}
				
				// เปลี่ยน status ของ PO
				if (po.isReceiveAll()){
					po.setStatus(POStatus.CLOSED);
				} else {
					po.setStatus(POStatus.GR_PARTIAL);
				}
				aGoodsReceipt.calculateTotalPrice();
				session.saveOrUpdate(aGoodsReceipt);
				session.saveOrUpdate(po);
			} else {
				aGoodsReceipt.calculateTotalPrice();
				session.saveOrUpdate(aGoodsReceipt);
				for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
					session.saveOrUpdate(grItem);
				}
			}
			
			// รับของเข้าคลัง พร้อมทำการสร้าง movement
			MaterialLot materialLot;
			StockMovement stockMovement;
			for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
				// TODO แปลงหน่วยจัดซื้อเป็นหน่วยจัดเก็บ
				// create lot
				materialLot = new MaterialLot();
				materialLot.setMaterial(grItem.getMaterial());
				materialLot.setCreatedDate(CalendarUtils.getDateTimeInstance().getTime());
				materialLot.setTotalQty(grItem.getReceivedQty());
				materialLot.setUnitPrice(grItem.getUnitPrice());
				materialLot.setTotalPrice(grItem.getNetPrice());
				materialLot.setAvailableQty(materialLot.getTotalQty());
				materialLot.setStorageLocation(grItem.getStorageLocation());
				materialLot.setWarehouse(aGoodsReceipt.getWarehouseCode());
				
				session.save(materialLot);
				
				// create movement
				stockMovement = new StockMovement();
				stockMovement.setMaterial(materialLot.getMaterial());
				stockMovement.setMaterialLot(materialLot);
				stockMovement.setGoodsReceiptItem(grItem);
				stockMovement.setMovementDate(materialLot.getCreatedDate());
				stockMovement.setTotalQty(materialLot.getTotalQty());
				stockMovement.setUnitPrice(materialLot.getUnitPrice());
				stockMovement.setTotalPrice(materialLot.getTotalPrice());
				stockMovement.setMovementSign(StockMovement.SIGN_PLUS);
				stockMovement.setMovementType(MovementType.GR);
				stockMovement.setWarehouse(aGoodsReceipt.getWarehouseCode());
				stockMovement.setStorageLocation(grItem.getStorageLocation());
				stockMovement.setRemark("รับตามใบรับ : "+aGoodsReceipt.getGrNumber());
				
				session.save(stockMovement);
			}
			session.flush();
						
			// TODO ใช้งบประมาณ
//			BudgetController budgetController = new BudgetController();
//			BudgetItem budgetItem;
//			for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
//				budgetItem = (BudgetItem) session.load(BudgetItem.class, grItem.getBudgetItem().getId());
//				budgetController.expenseBudget(session, budgetItem, grItem.getNetPrice());
//				session.flush();
//				session.clear();
//			}
			// หักการจองงบประมาณ
//			if (aGoodsReceipt.getPurchaseOrder() != null) {
//				BigDecimal minus = new BigDecimal("-1.00");
//				BudgetItem item;
//				for (PurchaseOrderItem poItem : aGoodsReceipt.getPurchaseOrder().getPurchaseOrderItems()) {
//					item = (BudgetItem) session.load(BudgetItem.class, poItem.getBudgetItem().getId());
//					budgetController.reserveBudget(session, item, poItem.getTotalPrice().multiply(minus));
//					session.flush();
//					session.clear();
//				}
//			}		
			tx.commit();
			return aGoodsReceipt;
		} catch (ControllerException e) {
			if (tx != null ) {
				tx.rollback();
			}
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			if (tx != null ) {
				tx.rollback();
			}
			e.printStackTrace();
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกใบรับ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}

	public GoodsReceipt closeGoodsReceipt(GoodsReceipt aGoodsReceipt) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		BigDecimal zero = new BigDecimal("0.0");
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			GRStatus tempStatus = aGoodsReceipt.getStatus();
			
			if (aGoodsReceipt.getId() != null ){
				aGoodsReceipt.setStatus(GRStatus.CLOSED);
				session.saveOrUpdate(aGoodsReceipt);

				// เปิดการใช้งาน Lot และ Movement
				MaterialLot matLot;
				List<StockMovement> stockMovementList;
				for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
					grItem = (GoodsReceiptItem) session.load(GoodsReceiptItem.class, grItem.getId());
					stockMovementList = grItem.getStockMovements();
					for (StockMovement stockMovement : stockMovementList) {
						matLot = stockMovement.getMaterialLot();
						matLot.setStatus(LotStatus.OPEN);
						stockMovement.setStatus(MovementStatus.PERMANENT);
						session.saveOrUpdate(matLot);
						session.saveOrUpdate(stockMovement);
					}
				}
				
				session.flush();
				
				// คิดเงินเวลารับแบบไม่ผ่าน PO
				if (tempStatus.equals(GRStatus.OPEN)) {
					// หักเงินตาม netprice ของแต่ละ item
					BudgetController budgetController = new BudgetController();
					BudgetItem budgetItemForExpense;
					for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
						budgetItemForExpense = (BudgetItem) session.load(BudgetItem.class, grItem.getBudgetItem().getId());
						budgetController.expenseBudget(session, budgetItemForExpense, grItem.getNetPrice());
						session.flush();
						session.clear();
					}
				}
				
				// คิดเงินเวลารับแบบเต็ม
				if (tempStatus.equals(GRStatus.GR_FULL)) {
					// หักเงินตาม netprice ของแต่ละ item
					BudgetController budgetController = new BudgetController();
					BudgetItem budgetItemForExpense;
					for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
						budgetItemForExpense = (BudgetItem) session.load(BudgetItem.class, grItem.getBudgetItem().getId());
						budgetController.expenseBudget(session, budgetItemForExpense, grItem.getNetPrice());
						session.flush();
						session.clear();
					}
					if (!aGoodsReceipt.getPurchaseOrder().getTotalPrice().equals(zero)){
						// หักเงินจากที่กันงบไว้ตามจำนวน totalprice ของ grItem
						BudgetItem budgetItemForReserve;
						BigDecimal SIGN_MINUS = new BigDecimal("-1.00");
						for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
							budgetItemForReserve = (BudgetItem) session.load(BudgetItem.class, grItem.getBudgetItem().getId());
//							budgetController.reserveBudget(session, budgetItemForReserve, grItem.getTotalprice().multiply(SIGN_MINUS));
							budgetController.reserveBudget(session, budgetItemForReserve, grItem.getReceivedQty().multiply(grItem.getOrderUnitPrice().multiply(SIGN_MINUS)));
							session.flush();
							session.clear();				
						}
					}
				}
				
				if (tempStatus.equals(GRStatus.GR_PAR)) {
					// หักเงินตาม netprice ของแต่ละ item
					BudgetController budgetController = new BudgetController();
					BudgetItem budgetItemForExpense;
					for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
						budgetItemForExpense = (BudgetItem) session.load(BudgetItem.class, grItem.getBudgetItem().getId());
						budgetController.expenseBudget(session, budgetItemForExpense, grItem.getNetPrice());
						session.flush();
						session.clear();
					}
					if (!aGoodsReceipt.getPurchaseOrder().getTotalPrice().equals(zero)){
						// หักเงินจากที่กันงบไว้ตามจำนวน totalprice ของ grItem
						BudgetItem budgetItemForReserve;
						BigDecimal SIGN_MINUS = new BigDecimal("-1.00");
						for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
							budgetItemForReserve = (BudgetItem) session.load(BudgetItem.class, grItem.getBudgetItem().getId());
//							budgetController.reserveBudget(session, budgetItemForReserve, grItem.getTotalprice().multiply(SIGN_MINUS));
							budgetController.reserveBudget(session, budgetItemForReserve, grItem.getReceivedQty().multiply(grItem.getOrderUnitPrice().multiply(SIGN_MINUS)));
							session.flush();
							session.clear();				
						}
					}
					}
								
				// TODO คืนงบประมาณที่จองไว้
//				BudgetController budgetController = new BudgetController();
//				BudgetItem budgetItem;
//				BigDecimal SIGN_MINUS = new BigDecimal("-1.00");
//				for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
//					budgetItem = (BudgetItem) session.load(BudgetItem.class, grItem.getBudgetItem().getId());
//					budgetController.reserveBudget(session, budgetItem, grItem.getTotalprice().multiply(SIGN_MINUS));
//					session.flush();
//					session.clear();
//				}
				//คืนงบที่จองไว้จำนวนเท่ากับ PO
//				if (aGoodsReceipt.getPurchaseOrder() != null) {
//					BigDecimal minus = new BigDecimal("-1.00");
//					BudgetController budgetController = new BudgetController();
//					BudgetItem item;
//					for (PurchaseOrderItem poItem : aGoodsReceipt.getPurchaseOrder().getPurchaseOrderItems()) {
//						item = (BudgetItem) session.load(BudgetItem.class, poItem.getBudgetItem().getId());
//						budgetController.reserveBudget(session, item, poItem.getTotalPrice().multiply(minus));
//						session.flush();
//						session.clear();
//					}
//				}	
			}
			
			tx.commit();
			return aGoodsReceipt;
		} catch (Exception e) {
			if (tx != null ) {
				tx.rollback();
			}
			e.printStackTrace();
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกใบรับ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	public GoodsReceipt deleteGoodsReceipt(GoodsReceipt aGoodsReceipt) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();			
			if (aGoodsReceipt.getStatus().equals(GRStatus.GR_FULL)){				
				PurchaseOrder po = aGoodsReceipt.getPurchaseOrder();
				po = getPurchaseOrder(po.getId());
				List<PurchaseOrderItem> poItemList = po.getPurchaseOrderItems();
				
				for (PurchaseOrderItem poItem : poItemList) {
					poItem.setReceivedQuantity(new BigDecimal("0.00"));
					session.saveOrUpdate(poItem);
				}
				aGoodsReceipt.getPurchaseOrder().setStatus(POStatus.OPEN);
				aGoodsReceipt.setStatus(GRStatus.DELETED);
				
				session.saveOrUpdate(aGoodsReceipt);
				session.saveOrUpdate(aGoodsReceipt.getPurchaseOrder());
				
				// จองงบ
//				if (aGoodsReceipt.getPurchaseOrder() != null) {
//					BudgetItem item;
//					BudgetController budgetController = new BudgetController();
//					PurchaseOrder purchaseOrder = (PurchaseOrder) session.load(PurchaseOrder.class, aGoodsReceipt.getPurchaseOrder().getId());
//					for (PurchaseOrderItem poItem : purchaseOrder.getPurchaseOrderItems()) {
//						item = (BudgetItem) session.load(BudgetItem.class, poItem.getBudgetItem().getId());
//						budgetController.reserveBudget(session, item, poItem.getTotalPrice());
//						session.flush();
//						session.clear();
//					}
//				}
			}
			
			if (aGoodsReceipt.getStatus().equals(GRStatus.OPEN)){
				aGoodsReceipt.setStatus(GRStatus.DELETED);
				session.saveOrUpdate(aGoodsReceipt);
			}
			
			//ใช้ได้ก็ต่อเมื่อจำนวน item ของ GR กับ PO เท่ากัน
			if (aGoodsReceipt.getStatus().equals(GRStatus.GR_PAR)){
				PurchaseOrder po = aGoodsReceipt.getPurchaseOrder();
				po = getPurchaseOrder(po.getId());
				List<PurchaseOrderItem> poItemList = po.getPurchaseOrderItems();
				List<GoodsReceiptItem> grItemList = aGoodsReceipt.getGoodsReceiptItems();
				
				BigDecimal currentQty = new BigDecimal("0.00");
				int zeroQtyItemCount = 0;
				for (GoodsReceiptItem grItem : grItemList){
					for (PurchaseOrderItem poItem : poItemList){
						if (grItem.getItemNumber() == poItem.getItemNumber()){
							currentQty = poItem.getReceivedQuantity().subtract(grItem.getReceivedQty());
							poItem.setReceivedQuantity(currentQty);
							session.saveOrUpdate(poItem);
							
							if(currentQty.equals(new BigDecimal("0.00"))){
								zeroQtyItemCount++;
							}
						} 
					}					
				}
				if(zeroQtyItemCount == poItemList.size()){
					aGoodsReceipt.getPurchaseOrder().setStatus(POStatus.OPEN);
					
					session.saveOrUpdate(aGoodsReceipt.getPurchaseOrder());
					
					// จองงบ
//					if (aGoodsReceipt.getPurchaseOrder() != null) {
//						BudgetItem item;
//						BudgetController budgetController = new BudgetController();
//						PurchaseOrder purchaseOrder = (PurchaseOrder) session.load(PurchaseOrder.class, aGoodsReceipt.getPurchaseOrder().getId());
//						for (PurchaseOrderItem poItem : purchaseOrder.getPurchaseOrderItems()) {
//							item = (BudgetItem) session.load(BudgetItem.class, poItem.getBudgetItem().getId());
//							budgetController.reserveBudget(session, item, poItem.getTotalPrice());
//							session.flush();
//							session.clear();
//						}
//					}
				} else {
					System.out.println(zeroQtyItemCount);
					aGoodsReceipt.getPurchaseOrder().setStatus(POStatus.GR_PARTIAL);
					
					session.saveOrUpdate(aGoodsReceipt.getPurchaseOrder());
				}
				aGoodsReceipt.setStatus(GRStatus.DELETED);
				session.saveOrUpdate(aGoodsReceipt);
			}
			
			// TODO คืนของออกจากคลัง
			MaterialLot matLot;
			List<StockMovement> stockMovementList;
			StockMovement newStockMovement;
			for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
				grItem = (GoodsReceiptItem) session.load(GoodsReceiptItem.class, grItem.getId());
				stockMovementList =  grItem.getStockMovements();
				for (StockMovement stockMovement : stockMovementList) {
					matLot = stockMovement.getMaterialLot();
					if (matLot.getAvailableQty().doubleValue() != matLot.getTotalQty().doubleValue()) {
						// TODO มีการใช้งานไปแล้ว ( อาจไม่เกิดเพราะจะใช้ได้ก็ต่อเมื่อมีการปิดใบรับวัสดุ  )
					}
					matLot = stockMovement.getMaterialLot();
					matLot.setStatus(LotStatus.CANCELED);
					stockMovement.setStatus(MovementStatus.PERMANENT);
					session.saveOrUpdate(matLot);
					session.saveOrUpdate(stockMovement);
					
					// create movement
					newStockMovement = new StockMovement();
					newStockMovement.setMaterial(stockMovement.getMaterial());
					newStockMovement.setMaterialLot(matLot);
					newStockMovement.setGoodsReceiptItem(grItem);
					newStockMovement.setMovementDate(CalendarUtils.getDateTimeInstance().getTime());
					newStockMovement.setTotalQty(stockMovement.getTotalQty());
					newStockMovement.setUnitPrice(stockMovement.getUnitPrice());
					newStockMovement.setTotalPrice(stockMovement.getTotalPrice());
					newStockMovement.setMovementSign(StockMovement.SIGN_MINUS);
					newStockMovement.setMovementType(MovementType.GR_CANCEL);
					newStockMovement.setWarehouse(aGoodsReceipt.getWarehouseCode());
					newStockMovement.setStorageLocation(grItem.getStorageLocation());
					newStockMovement.setRemark("ยกเลิกใบรับ : "+aGoodsReceipt.getGrNumber());
					newStockMovement.setStatus(MovementStatus.CANCELED);
					session.saveOrUpdate(newStockMovement);
					
				}
			}
			session.flush();
			session.clear();
			
			// TODO ลดงบประมาณที่ใช้ไป
//			BudgetController budgetController = new BudgetController();
//			BudgetItem budgetItem;
//			BigDecimal SIGN_MINUS = new BigDecimal("-1.00");
//			for (GoodsReceiptItem grItem : aGoodsReceipt.getGoodsReceiptItems()) {
//				budgetItem = (BudgetItem) session.load(BudgetItem.class, grItem.getBudgetItem().getId());
//				budgetController.expenseBudget(session, budgetItem, grItem.getNetPrice().multiply(SIGN_MINUS));
//				session.flush();
//				session.clear();
//			}
			// เพิ่มการจองงบประมาณ
//			if (aGoodsReceipt.getPurchaseOrder() != null) {
//				BudgetItem item;
//				PurchaseOrder po = (PurchaseOrder) session.load(PurchaseOrder.class, aGoodsReceipt.getPurchaseOrder().getId());
//				for (PurchaseOrderItem poItem : po.getPurchaseOrderItems()) {
//					item = (BudgetItem) session.load(BudgetItem.class, poItem.getBudgetItem().getId());
//					budgetController.reserveBudget(session, item, poItem.getTotalPrice());
//					session.flush();
//					session.clear();
//				}
//			}
							
			tx.commit();
			return aGoodsReceipt;
		} catch (Exception e) {
			if (tx != null ) {
				tx.rollback();
			}
			e.printStackTrace();
			throw new ControllerException("เกิดความผิดพลาดระหว่างการบันทึกใบรับ");
		} finally {
			session.clear();
			session.close();
//			if (session!= null && session.isOpen()) {
//				session.close();
//			}
		}
	}
	
	public int getNextGoodsReceiptNo() throws ControllerException{
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
					"SELECT MAX(gr.grNumber)" +
					"FROM GoodsReceipt gr " +
					"WHERE gr.budgetYear = :pbudgetyear ")
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
	public ArrayList<String> getEntryNames(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT gr.entryName " +
					"FROM GoodsReceipt gr " +
					"WHERE " +
					"	gr.warehouseCode = :pwhcode " +
					"AND gr.entryName IS NOT NULL " +
					"ORDER BY gr.entryName " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<String>(list);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงรายชื่อผู้บันทึกใบรับวัสดุ");
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
	public ArrayList<String> getEntryPos(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT gr.entryPos " +
					"FROM GoodsReceipt gr " +
					"WHERE " +
					"	gr.warehouseCode = :pwhcode " +
					"AND gr.entryPos IS NOT NULL " +
					"ORDER BY gr.entryPos " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();

			return new ArrayList<String>(list);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงรายชื่อผู้บันทึกใบรับวัสดุ");
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
	public ArrayList<String> getRecipientPOs(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT gr.recipientPos " +
					"FROM GoodsReceipt gr " +
					"WHERE " +
					"	gr.warehouseCode = :pwhcode " +
					"AND gr.recipientPos IS NOT NULL " +
					"ORDER BY gr.recipientPos " )
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
	public ArrayList<String> getRecipientNames(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> list = session.createQuery(
					"SELECT DISTINCT gr.recipientName " +
					"FROM GoodsReceipt gr " +
					"WHERE " +
					"	gr.warehouseCode = :pwhcode " +
					"AND gr.recipientName IS NOT NULL " +
					"ORDER BY gr.recipientName " )
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
