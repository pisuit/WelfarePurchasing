package welfare.presentation.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import bsh.ParseException;

import welfare.persistent.controller.LogController;
import welfare.persistent.controller.SecurityController;
import welfare.persistent.customtype.LogType;
import welfare.persistent.domain.security.Log;
import welfare.persistent.domain.security.User;
import welfare.persistent.exception.ControllerException;
import welfare.security.SecurityUser;
import welfare.utils.CalendarUtils;
import welfare.utils.Constants;
import welfare.utils.FacesUtils;

public class LogManager {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private SecurityController secController =  new SecurityController();
	private LogController logController = new LogController();
	private ArrayList<Log> logList = new ArrayList<Log>();
	private String searchedUser = "";
	private Date toDateTime = null;
	private Date fromDateTime = null;
	private String selectedType = "-1";
	private String selectedDay = "";
	private ArrayList<SelectItem> typeList = new ArrayList<SelectItem>();
	private LogType logType = null;
					
	public LogManager(){
		createTypeList();
	}
	
	public String getIpFromUser(){
		String ip = "Unknown";
		 HttpServletRequest	request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();	 
			if (request.getHeader("X-FORWARDED-FOR") != null) {
				// if the client is behind a proxy
				ip = request.getHeader("X-FORWARDED-FOR");
			} else {
				ip = request.getRemoteAddr();
			} 
			return ip;
	}
	
	public User getUserFromSession(){
		User user = new User();
		try {
			SecurityUser securityUser = (SecurityUser) FacesUtils.getSessionObject(Constants.USER_KEY, SecurityUser.class);
			user = secController.getUser(securityUser.getUsername(), securityUser.getSystemName(), securityUser.getSubSystemName());			
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		return user;
	}
	//================================================ Start - login ====================================================//
	/**
	 * เก็บ log การ login
	 */
	public void recordLogin(){	
		System.out.println("logging login");
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ACCESS);
			String systemName = getMainSystemName(user.getMainSystemName());
			log.setDescription("login เข้าสู่ "+systemName);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การ logout
	 */
	public void recordLogout(){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);			
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ACCESS);
			String systemName = getMainSystemName(user.getMainSystemName());
			log.setDescription("logout ออกจาก "+systemName);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - login ====================================================//
	
	//================================================ Start - Budget ====================================================//
	/**
	 * เก็บ log การสร้างงบประมาณประจำปี
	 * @param year
	 */
	public void recordCreateBudget(int year){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("สร้างงบประมาณประจำปี "+year);
			logController.logRecord(log);
		}  catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การปิด/เปิดให้แก้ไขงบประมาณประจำปี
	 * @param year
	 * @param flag
	 */
	public void recordLockBudget(int year, boolean flag){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			if (!flag) {
				log.setDescription("ปิดให้แก้ไขงบประมาณประจำปี "+year);
			} else {
				log.setDescription("เปิดให้แก้ไขงบประมาณประจำปี "+year);
			}
			logController.logRecord(log);
		}  catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การปิด/เปิดการเบิกใช้งบประมาณประจำปี
	 * @param year
	 * @param flag
	 */
	public void recordUseBudget(int year, boolean flag){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			if (!flag) {
				log.setDescription("ปิดการเบิกใช้งบประมาณประจำปี "+year);
			} else {
				log.setDescription("เปิดการเบิกใช้งบประมาณประจำปี "+year);
			}
			logController.logRecord(log);
		}  catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	public void printBudgetReport(int year){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("พิมพ์รายงานงบประมาณปี "+year);
			logController.logRecord(log);
		}  catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - Budget ====================================================//
	
	//================================================ Start - BudgetItem ====================================================//
	/**
	 * เก็บ log การสร้างงบประมาณหมวดหลัก
	 */
	public void recordCreateBudgetItem(String accCode, BigDecimal amount, boolean flag){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			if (flag) {
				log.setLogType(LogType.BUDGET);
				log.setDescription("สร้างหมวดงบประมาณเลขที่ "+accCode+" ด้วยวงเงิน "+amount);
			} else {
				log.setLogType(LogType.BUDGET);
				log.setDescription("แก้ไขหมวดงบประมาณเลขที่ "+accCode+" ด้วยวงเงิน "+amount);
			}
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การลบงบประมาณหมวดหลัก
	 * @param category
	 */
	public void recordDeleteBudgetItem(String accCode){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("ลบหมวดงบประมาณเลขที่ "+accCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การปิด/เปิดการบันทึกงบประมาณ
	 * @param category
	 * @param flag
	 */
	public void recordLockBudgetItem(String accCode, boolean flag){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			if (!flag) {
				log.setDescription("ปิดการบันทึกหมวดงบประมาณเลขที่ "+accCode);			
			} else {
				log.setDescription("เปิดการบันทึกหมวดงบประมาณเลขที่ "+accCode);
			}
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไขหมวดงบประมาณ
	 * @param category
	 */
	public void recordUpdateBudgetItem(String accCode){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("แก้ไขหมวดงบประมาณย่อยเลขที่ "+accCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การเพิ่มหมวดงบประมาณย่อย
	 * @param category
	 */
	public void recordCreateSubBudgetItem(String accCode){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("เพิ่มหมวดงบประมาณย่อยเลขที่ "+accCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การพิมพ์ใบรายการการโอนงบประมาณ
	 * @param category
	 */
	public void recordPrintBudgetTransferReport(String category){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("พิมพ์รายงานการโอนงบประมาณหมวด "+category);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - BudgetItem ====================================================//
	
	//================================================ Start - BudgetTransfer ====================================================//
	/**
	 * เก็บ log การสร้างใบขอโอนงบประมาณ
	 * @param number
	 */
	public void recordCreateTransfer(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("สร้างใบขอโอนงบประมาณเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไขใบขอโอนงบประมาณ
	 * @param number
	 */
	public void recordUpdateTransfer(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("แก้ไขใบขอโอนงบประมาณเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การอนุมัติใบขอโอนงบประมาณ
	 * @param number
	 */
	public void recordApproveTransfer(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("อนุมัติใบขอโอนงบประมาณเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การยกเลิกใบขอโอนงบประมาณ
	 * @param number
	 */
	public void recordDiscardTransfer(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("ยกเลิกใบขอโอนงบประมาณเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - BudgetTransfer ====================================================//
	
	//================================================ Start - BudgeExpense ====================================================//
	/**
	 * เก็บ log การบันทึกค่าใช้จ่าย
	 * @param accCode
	 * @param invoiceNo
	 */
	public void recordBudgetExpense(String accCode, String detail){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("บันทึกค่าใช้จ่ายในหมวดงบประมาณเลขที่ "+accCode+" รายละเอียดคือ "+detail);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	public void recordUpdateBudgetExpense(String accCode, String detail){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("แก้ไขค่าใช้จ่ายในหมวดงบประมาณเลขที่ "+accCode+" เรายละเอียดคือ "+detail);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	public void recordDeleteBudgetExpense(String accCode, String detail){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.BUDGET);
			log.setDescription("ลบค่าใช้จ่ายในหมวดงบประมาณเลขที่ "+accCode+" รายละเอียดคือ "+detail);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - BudgetExpense ====================================================//
	
	//================================================ Start - PR ====================================================//
	/**
	 * เก็บ log การสร้างใบ PR
	 * @param number
	 * @param totalPrice
	 */
	public void recordCreatePr(String number, BigDecimal totalPrice){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PR);
			log.setDescription("สร้างใบแจ้งจัดหาเลขที่ "+number+" เป็นจำนวนเงินทั้งหมด "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไขใบ PR
	 * @param number
	 * @param totalPrice
	 */
	public void recordUpdatePr(String number, BigDecimal totalPrice){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PR);
			log.setDescription("แก้ไขใบแจ้งจัดหาเลขที่ "+number+" เป็นจำนวนเงินทั้งหมด "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การยกเลิกใบ PR
	 * @param number
	 */
	public void recordDeletePr(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PR);
			log.setDescription("ยกเลิกใบแจ้งจัดหาเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การพิมพ์ใบ PR
	 * @param number
	 */
	public void recordPrintPr(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PR);
			log.setDescription("พิมพ์ใบแจ้งจัดหาเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - PR ====================================================//
	
	//================================================ Start - PO ====================================================//
	/**
	 * เก็บ log การสร้างใบ PO
	 * @param number
	 * @param totalPrice
	 */
	public void recordCreatePo(String number, BigDecimal totalPrice){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PO);
			log.setDescription("สร้างใบแจ้งจัดซื้อเลขที่ "+number+" เป็นจำนวนเงินทั้งหมด "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไขใบ PO
	 * @param number
	 * @param totalPrice
	 */
	public void recordUpdatePo(String number, BigDecimal totalPrice){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PO);
			log.setDescription("แก้ไขใบแจ้งจัดซื้อเลขที่ "+number+" เป็นจำนวนเงินทั้งหมด "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การลบใบ PO
	 * @param number
	 */
	public void recordDeletePo(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PO);
			log.setDescription("ยกเลิกใบแจ้งจัดซื้อเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การปิดใบ PO
	 * @param number
	 */
	public void recordClosePo(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PO);
			log.setDescription("ปิดใบแจ้งจัดซื้อเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การพิมพ์ใบ PO
	 * @param number
	 */
	public void recordPrintPoLess(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PO);
			log.setDescription("พิมพ์ใบรายงานขออนุมัติจัดหาพัสดุ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	public void recordPrintPoMore(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.PO);
			log.setDescription("พิมพ์ใบแจ้งจัดซื้อเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - PO ====================================================//
	
	//================================================ Start - Vendor ====================================================//
	/**
	 * เก็บ log การสร้างร้านค้า
	 * @param number
	 */
	public void recordCreateVendor(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.VENDOR);
			log.setDescription("เพิ่มร้านค้าเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไขร้านค้า
	 * @param number
	 */
	public void recordUpdateVendor(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.VENDOR);
			log.setDescription("แก้ไขร้านค้าเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การลบร้านค้า
	 * @param number
	 */
	public void recordDeleteVendor(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.VENDOR);
			log.setDescription("ลบร้านค้าเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - Vendor ====================================================//
	
	//================================================ Start - MaterialGroup ====================================================//
	/**
	 * เก็บ log การสร้างกลุ่มวัสดุ
	 * @param code
	 */
	public void recordCreateMaterialGroup(String code){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.STOCK);
			log.setDescription("เพิ่มกลุ่มวัสดุรหัส "+code);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไขกลุ่มวัสดุ
	 * @param code
	 */
	public void recordUpdateMaterialGroup(String code){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.STOCK);
			log.setDescription("แก้ไขกลุ่มวัสดุรหัส "+code);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การลบกลุ่มวัสดุ
	 * @param code
	 */
	public void recordDeleteMaterialGroup(String code){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.STOCK);
			log.setDescription("ลบกลุ่มวัสดุรหัส "+code);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - MaterialGroup ====================================================//
	
	//================================================ Start - Material ====================================================//
	/**
	 * เก็บ log การสร้างกลุ่มวัสดุ
	 * @param code
	 */
	public void recordCreateMaterial(String matCode, String groupCode){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.STOCK);
			log.setDescription("เพิ่มวัสดุรหัส "+matCode+" กายใต้กลุ่มวัสดุรหัส "+groupCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไขกลุ่มวัสดุ
	 * @param code
	 */
	public void recordUpdateMaterial(String matCode, String groupCode){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.STOCK);
			log.setDescription("แก้ไขวัสดุรหัส "+matCode+" กายใต้กลุ่มวัสดุรหัส "+groupCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การลบกลุ่มวัสดุ
	 * @param code
	 */
	public void recordDeleteMaterial(String matCode, String groupCode){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.STOCK);
			log.setDescription("ลบวัสดุรหัส "+matCode+" กายใต้กลุ่มวัสดุรหัส "+groupCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - Material ====================================================//
	
	//================================================ Start - GR ====================================================//
	/**
	 * เก็บ log การสร้างใบ GR
	 * @param number
	 * @param totalPrice
	 */
	public void recordCreateGr(String number, BigDecimal totalPrice){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.GR);
			log.setDescription("สร้างใบรับวัสดุเลขที่ "+number+" เป็นจำนวนเงินทั้งหมด "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไขใบ GR
	 * @param number
	 * @param totalPrice
	 */
	public void recordUpdateGr(String number, BigDecimal totalPrice){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.GR);
			log.setDescription("แก้ไขใบรับวัสดุเลขที่ "+number+" เป็นจำนวนเงินทั้งหมด "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การยกเลิกใบ GR
	 * @param number
	 */
	public void recordDeleteGr(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.GR);
			log.setDescription("ยกเลิกใบรับวัสดุเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การปิดใบ GR
	 * @param number
	 */
	public void recordCloseGr(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.GR);
			log.setDescription("ปิดใบรับวัสดุเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การพิมพ์ใบ GR
	 * @param number
	 */
	public void recordPrintGr(String number){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.GR);
			log.setDescription("พิมพ์ใบรับวัสดุเลขที่ "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - GR ====================================================//
	
	//================================================ Start - BudgetAdmin ====================================================//
	/**
	 * เก็บ log การสร้าง user ใน Budget
	 * @param number
	 * @param system
	 */
	public void recordCreateBudgetUser(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("เพิ่มผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไข user ใน Budget
	 * @param number
	 * @param system
	 */
	public void recordUpdateBudgetUser(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("แก้ไขผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การลบ user ใน Budget
	 * @param number
	 * @param system
	 */
	public void recordDeleteBudgetUser(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("ลบผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไขสิทธิการใช้งบประมาณ
	 * @param username
	 * @param system
	 */
	public void recordUpdateBudgetAuthorization(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("แก้ไขสิทธิการใช้งบประมาณของผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - BudgetAdmin ====================================================//
	
	//================================================ Start - PurchasingAdmin ====================================================//
	/**
	 * เก็บ log การสร้าง user ใน Purchasing
	 * @param number
	 * @param system
	 */
	public void recordCreatePurchasingUser(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("เพิ่มผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การแก้ไข user ใน Purchasing
	 * @param number
	 * @param system
	 */
	public void recordUpdatePurchasingUser(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("แก้ไขผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * เก็บ log การลบ user ใน Purchasing
	 * @param number
	 * @param system
	 */
	public void recordDeletePurchasingUser(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("ลบผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}	
	//================================================ End - PurchasingAdmin ====================================================//
	
	//================================================ Start - StockAdmin ====================================================//
	
	/**
	 * เก็บ log การสร้าง user ใน Stock
	 * @param number
	 * @param system
	 */
	public void recordCreateStockUser(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("เพิ่มผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * เก็บ log การแก้ไข user ใน Stock
	 * @param number
	 * @param system
	 */
	public void recordUpdateStockUser(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("แก้ไขผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * เก็บ log การลบ user ใน Stock
	 * @param number
	 * @param system
	 */
	public void recordDeleteStockUser(String username, String system){
		try {
			User user = getUserFromSession();
			String ip = getIpFromUser();
			Log log = new Log();
			log.setIp(ip);
			log.setDateTime(CalendarUtils.getDateTimeInstance().getTime());
			log.setUser(user);
			log.setLogType(LogType.ADMIN);
			log.setDescription("ลบผู้ใช้ "+username+" ใน "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}	
	
	//================================================ End - StockAdmin ====================================================//
	
	public String getSystemName(String aSystemName){
		String systemName = "";
		if (aSystemName.equals(Constants.WH_MAHAMEK)) {
			systemName = "ทุ่งมหาเมฆ";
		}
		if (aSystemName.equals(Constants.WH_CHIANGMAI)) {
			systemName = "ศูนย์เชียงใหม่";
		}
		if (aSystemName.equals(Constants.WH_HADYAI)) {
			systemName = "ศูนย์หาดใหญ่";
		}
		if (aSystemName.equals(Constants.WH_HUAHIN)) {
			systemName = "ศูนย์หัวหิน";
		}
		if (aSystemName.equals(Constants.WH_NAKORN)) {
			systemName = "ศูนย์นครราชสีมา";
		}
		if (aSystemName.equals(Constants.WH_PHITSANULOK)) {
			systemName = "ศูนย์พิษณุโลก";
		}
		if (aSystemName.equals(Constants.WH_PUKET)) {
			systemName = "ศูนย์ภูเก็ต";
		}
		if (aSystemName.equals(Constants.WH_SURAT)) {
			systemName = "ศูนย์สุราษฎร์ธานี";
		}
		if (aSystemName.equals(Constants.WH_UBON)) {
			systemName = "ศูนย์อุบลราชธานี";
		}
		if (aSystemName.equals(Constants.WH_UDON)) {
			systemName = "ศูนย์อุดรธานี";
		}
		return systemName;
	}
	
	public String getMainSystemName(String aMainSystemName) {
		String mainSystemName = "";
		if (aMainSystemName.equals(Constants.SYS_MAIN_BUDGET)) {
			mainSystemName = "ระบบงบประมาณ";
		}
		if (aMainSystemName.equals(Constants.SYS_MAIN_PURCHASING)) {
			mainSystemName = "ระบบจัดซื้อจัดหา";
		}
		if (aMainSystemName.equals(Constants.SYS_MAIN_STOCK)) {
			mainSystemName = "ระบบจัดการคลัง";
		}
		return mainSystemName;
	}
	
//	public String getSystemName(String aSystemName){
//		String systemName = "";
//		if (aSystemName.equals(Constants.SYS_BUDGET_BUDGET)){
//			systemName = "ระบบงบประมาณ";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_CANTEEN)){
//			systemName = "ระบบงานโภชการและสโมสร";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_SPORT)){
//			systemName = "ระบบงานกีฬา";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_NURSERY)){
//			systemName = "ระบบงานสถานรับเลี้ยงเด็ก";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_HOTEL)){
//			systemName = "ระบบงานห้องพักงามดูพลี";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_MEDICAL)){
//			systemName = "ระบบงานเวชภัณฑ์";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_SWIMMING)){
//			systemName = "ระบบงานสระว่ายน้ำ";	
//		}
//		if (aSystemName.equals(Constants.SYS_MAIN_LOG)){
//			systemName = "ระบบตรวจสอบ Log";
//		}
//		return systemName;
//	}
	
	public void logList(){
		try {
			if (logList != null) logList.clear();
			logList = logController.getLogList();
			selectedType = "-1";
			searchedUser = "";
			fromDateTime = null;
			toDateTime = null;
			logType = null;
		} catch (ControllerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
//	public void searchUser(){
//		try {
//			ArrayList<Log> oldList = new ArrayList<Log>();
//			oldList.addAll(logList);
//			
//			if (logList != null) logList.clear();
//			logList = logController.getSearchedUser(searchedUser);
//			
//			if (logList.size() == 0) {
//				logList.clear();
//				logList.addAll(oldList);
//				oldList.clear();
//				throw new ControllerException("ไม่พบชื่อผู้ใช้ "+searchedUser);
//			}
//		} catch (ControllerException e) {
//			// TODO: handle exception
//			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
//			e.printStackTrace();
//		}
//	}
	
	public void logTypeComboboxSelected(){
		logType = LogType.find(selectedType);
	}
	
	public void searchLog() {
		try {
			ArrayList<Log> oldList = new ArrayList<Log>();
			oldList.addAll(logList);						
			if (logList != null) logList.clear();
						
			logList = logController.searchLog(searchedUser, fromDateTime, toDateTime, logType);
		} catch (ControllerException e) {
			e.printStackTrace();
			// TODO: handle exception
		}		
	}
	
//	public void searchType(){
//		try {
//			if (!selectedType.equalsIgnoreCase("-1")){
//				if (logList != null) logList.clear();
//				logList = logController.getSearchedType(LogType.find(selectedType));
//			}
//		} catch (ControllerException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}
//	public void searchByTime(){
//		try {
//			if (logList != null) logList.clear();
//
//			Date parsedFrom = dateFormat.parse(fromDateTime);
//			Date parsedTo = dateFormat.parse(toDateTime);
//			Timestamp timestampFrom = new Timestamp(parsedFrom.getTime());
//			Timestamp timestampTo = new Timestamp(parsedTo.getTime());
//			
//			logList = logController.getSearchedTime(timestampFrom,timestampTo);
//		} catch (ControllerException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} catch (java.text.ParseException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}
	public void deleteLog(){
		try {
			if (selectedDay.equalsIgnoreCase("")) throw new ControllerException("กรุณาระบุจำนวนวัน");
			if (logList != null) logList.clear();
			
			int day = Integer.parseInt(selectedDay);
			Calendar cal = Calendar.getInstance();
			
			if (day == 30) cal.add(Calendar.DATE, -30);
			if (day == 60) cal.add(Calendar.DATE, -60);
			if (day == 90) cal.add(Calendar.DATE, -90);
			
			String dateString = dateFormat.format(cal.getTime());
			Date dateDate = dateFormat.parse(dateString);			
			Timestamp timestamp = new Timestamp(dateDate.getTime());
			
			logList = logController.deleteOldLog(timestamp);
			selectedDay = "";
		} catch (ControllerException e) {
			// TODO: handle exception
			FacesUtils.reportError(FacesContext.getCurrentInstance(), e.getMessage(), e.getMessage(), e);
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO: handle exception
		}
	}
	private void createTypeList(){
		SelectItem selectItem;
		typeList.clear();
		typeList.add(new SelectItem("-1","เลือกประเภท"));
		for (LogType logType : LogType.values()){
			selectItem = new SelectItem();
			selectItem.setValue(logType.getID());
			selectItem.setLabel(logType.getValue());
			typeList.add(selectItem);
		}
	}
	public ArrayList<Log> getLogList() {
		return logList;
	}
	public void setLogList(ArrayList<Log> logList) {
		this.logList = logList;
	}
	public String getSearchedUser() {
		return searchedUser;
	}
	public void setSearchedUser(String searchedUser) {
		this.searchedUser = searchedUser;
	}
	public String getSelectedType() {
		return selectedType;
	}
	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}
	public ArrayList<SelectItem> getTypeList() {
		return typeList;
	}
	public Date getToDateTime() {
		return toDateTime;
	}
	public void setToDateTime(Date toDateTime) {
		this.toDateTime = toDateTime;
	}
	public Date getFromDateTime() {
		return fromDateTime;
	}
	public void setFromDateTime(Date fromDateTime) {
		this.fromDateTime = fromDateTime;
	}
	public String getSelectedDay() {
		return selectedDay;
	}
	public void setSelectedDay(String selectedDay) {
		this.selectedDay = selectedDay;
	}
}
