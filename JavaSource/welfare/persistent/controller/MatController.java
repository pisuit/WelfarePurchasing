package welfare.persistent.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import welfare.persistent.domain.security.User;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.MaterialGroup;
import welfare.persistent.exception.ControllerException;
import welfare.persistent.services.HibernateUtil;
import welfare.utils.Constants;

public class MatController {
	
	@SuppressWarnings("unchecked")
	public ArrayList<MaterialGroup> getMaterialGroups(String pwhcode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			System.out.println("Get matgroup for warehouse : "+pwhcode);
			
			List<MaterialGroup> matGroupList = session.createQuery(
					"SELECT DISTINCT matgroup " +
					"FROM MaterialGroup matgroup " +
					"WHERE " +
					"	matgroup.status = 'NORMAL' " +
					"AND matgroup.warehouseCode = :pwhcode " +
					"ORDER BY matgroup.code " )
					.setParameter("pwhcode", pwhcode)
					.list();
			tx.commit();
			return new ArrayList<MaterialGroup>(matGroupList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลกลุ่มวัสดุ");
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
	public MaterialGroup saveMaterialGroup(MaterialGroup aMaterialGroup) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			// TODO ตรวจสอบว่ารหัสไม่ซ้ำ
			// TODO เพิ่ม code อัตโนมัติ
			session.saveOrUpdate(aMaterialGroup);
			
			tx.commit();
			return aMaterialGroup;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างบันทึกข้อมูลกลุ่มวัสดุ");
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
	public MaterialGroup getMaterialGroup(Long aID) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			MaterialGroup materialGroup = (MaterialGroup) session.createQuery(
					"FROM MaterialGroup matgroup " +
					"WHERE " +
					"matgroup.id = :pid " )
					.setParameter("pid", aID)
					.uniqueResult();
			tx.commit();
			return materialGroup;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลกลุ่มวัสดุ ID:"+aID);
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
	
	public String getNextMaterialGroupCode(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			String maxCode = (String) session.createQuery(
					"SELECT MAX(matgroup.code) " +
					"FROM MaterialGroup matgroup " +
					"WHERE " +
					"	matgroup.status = 'NORMAL' " +
					"AND matgroup.warehouseCode = :pwhcode " )
					.setParameter("pwhcode", aWarehouseCode)
					.uniqueResult();
			
			if (maxCode == null ){
				maxCode = Constants.DF_MATGROUP_CODE.format(Long.valueOf(1));
			} else {
				maxCode = Constants.DF_MATGROUP_CODE.format(Long.valueOf(maxCode).longValue()+1); 
			}
			tx.commit();
			return maxCode;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการกำหนดรหัสกลุ่มวัสดุ");
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
	public ArrayList<Material> getMaterials(MaterialGroup aMaterialGroup) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Material> matList = session.createQuery(
					"SELECT DISTINCT mat " +
					"FROM Material mat " +
					"	left join fetch mat.materialGroup matgroup " +
					"WHERE " +
					"	mat.status = 'NORMAL' " +
					"AND matgroup = :pmatgroup " +
					"ORDER BY mat.code " )
					.setParameter("pmatgroup", aMaterialGroup)
					.list();
			tx.commit();
			return new ArrayList<Material>(matList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลวัสดุภายในกลุ่ม "+aMaterialGroup);
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
	public ArrayList<Material> getMaterials(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Material> matList = session.createQuery(
					"SELECT DISTINCT mat " +
					"FROM Material mat " +
					"	left join fetch mat.materialGroup matgroup " +
					"WHERE " +
					"	mat.status = 'NORMAL' " +
					"AND mat.warehouseCode = :pwhcode " +
					"ORDER BY mat.code " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<Material>(matList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลวัสดุในคลัง ");
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
	public Material saveMaterial(Material aMaterial) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			// TODO ตรวจสอบว่ารหัสไม่ซ้ำ
			// TODO เพิ่ม code อัตโนมัติ
			
			System.out.println("orderUnitPrice : "+aMaterial.getOrderUnitPrice());
			session.saveOrUpdate(aMaterial);
			
			tx.commit();
			return aMaterial;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างบันทึกข้อมูลวัสดุ");
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
	
	public Material getMaterial(Long aID) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			Material material = (Material) session.createQuery(
					"FROM Material mat " +
					"WHERE " +
					"mat.id = :pid " )
					.setParameter("pid", aID)
					.uniqueResult();
			tx.commit();
			return material;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงข้อมูลกลุ่มวัสดุ ID:"+aID);
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
	
	public String getNextMaterialCode(String aWarehouseCode, MaterialGroup amMaterialGroup) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			String maxCode = (String) session.createQuery(
					"SELECT MAX(mat.code) " +
					"FROM Material mat " +
					"WHERE " +
					"	mat.status = 'NORMAL' " +
					"AND mat.warehouseCode = :pwhcode " +
					"AND mat.materialGroup = :pmatgroup" )
					.setParameter("pwhcode", aWarehouseCode)
					.setParameter("pmatgroup", amMaterialGroup)
					.uniqueResult();
			
			if (maxCode == null ){
				maxCode = Constants.DF_MAT_CODE.format(Long.valueOf(1));
			} else {
				maxCode = Constants.DF_MAT_CODE.format(Long.valueOf(maxCode).longValue()+1); 
			}
			
			tx.commit();
			return maxCode;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการกำหนดรหัสวัสดุ");
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
	public ArrayList<String> getIssueUnits(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> issueUnitList = session.createQuery(
					"SELECT DISTINCT mat.issueUnit " +
					"FROM Material mat " +
					"WHERE " +
					"	mat.status = 'NORMAL' " +
					"AND mat.warehouseCode = :pwhcode " +
					"ORDER BY mat.issueUnit " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<String>(issueUnitList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงหน่วยเบิก");
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
	public ArrayList<String> getOrderUnits(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<String> orderUnitList = session.createQuery(
					"SELECT DISTINCT mat.orderUnit " +
					"FROM Material mat " +
					"WHERE " +
					"	mat.status = 'NORMAL' " +
					"AND mat.warehouseCode = :pwhcode " +
					"ORDER BY mat.orderUnit " )
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<String>(orderUnitList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงหน่วยเบิก");
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
	public ArrayList<Material> getMinimumMaterials(String aWarehouseCode) throws ControllerException{
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = null;
		Transaction tx = null;
		try {
			session = sf.openSession();
			tx = session.beginTransaction();
			
			List<Material> minimumMaterialList = session.createQuery(
					"SELECT DISTINCT mat " +
					"FROM Material mat " +
					"	left join fetch mat.materialLot matlot" +
					"WHERE " +
					"mat.warehouseCode = :pwhcode ")
					.setParameter("pwhcode", aWarehouseCode)
					.list();
			tx.commit();
			return new ArrayList<Material>(minimumMaterialList);
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ControllerException("เกิดความผิดพลาดระหว่างการดึงหน่วยเบิก");
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
