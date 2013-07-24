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
	 * �� log ��� login
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
			log.setDescription("login ������ "+systemName);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��� logout
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
			log.setDescription("logout �͡�ҡ "+systemName);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - login ====================================================//
	
	//================================================ Start - Budget ====================================================//
	/**
	 * �� log ������ҧ������ҳ��Шӻ�
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
			log.setDescription("���ҧ������ҳ��Шӻ� "+year);
			logController.logRecord(log);
		}  catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��ûԴ/�Դ�����䢧�����ҳ��Шӻ�
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
				log.setDescription("�Դ�����䢧�����ҳ��Шӻ� "+year);
			} else {
				log.setDescription("�Դ�����䢧�����ҳ��Шӻ� "+year);
			}
			logController.logRecord(log);
		}  catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��ûԴ/�Դ����ԡ�駺����ҳ��Шӻ�
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
				log.setDescription("�Դ����ԡ�駺����ҳ��Шӻ� "+year);
			} else {
				log.setDescription("�Դ����ԡ�駺����ҳ��Шӻ� "+year);
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
			log.setDescription("�������§ҹ������ҳ�� "+year);
			logController.logRecord(log);
		}  catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - Budget ====================================================//
	
	//================================================ Start - BudgetItem ====================================================//
	/**
	 * �� log ������ҧ������ҳ��Ǵ��ѡ
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
				log.setDescription("���ҧ��Ǵ������ҳ�Ţ��� "+accCode+" ����ǧ�Թ "+amount);
			} else {
				log.setLogType(LogType.BUDGET);
				log.setDescription("�����Ǵ������ҳ�Ţ��� "+accCode+" ����ǧ�Թ "+amount);
			}
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���ź������ҳ��Ǵ��ѡ
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
			log.setDescription("ź��Ǵ������ҳ�Ţ��� "+accCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��ûԴ/�Դ��úѹ�֡������ҳ
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
				log.setDescription("�Դ��úѹ�֡��Ǵ������ҳ�Ţ��� "+accCode);			
			} else {
				log.setDescription("�Դ��úѹ�֡��Ǵ������ҳ�Ţ��� "+accCode);
			}
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��������Ǵ������ҳ
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
			log.setDescription("�����Ǵ������ҳ�����Ţ��� "+accCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���������Ǵ������ҳ����
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
			log.setDescription("������Ǵ������ҳ�����Ţ��� "+accCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��þ�������¡�á���͹������ҳ
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
			log.setDescription("�������§ҹ����͹������ҳ��Ǵ "+category);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - BudgetItem ====================================================//
	
	//================================================ Start - BudgetTransfer ====================================================//
	/**
	 * �� log ������ҧ㺢��͹������ҳ
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
			log.setDescription("���ҧ㺢��͹������ҳ�Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ������㺢��͹������ҳ
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
			log.setDescription("���㺢��͹������ҳ�Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���͹��ѵ�㺢��͹������ҳ
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
			log.setDescription("͹��ѵ�㺢��͹������ҳ�Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���¡��ԡ㺢��͹������ҳ
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
			log.setDescription("¡��ԡ㺢��͹������ҳ�Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - BudgetTransfer ====================================================//
	
	//================================================ Start - BudgeExpense ====================================================//
	/**
	 * �� log ��úѹ�֡��������
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
			log.setDescription("�ѹ�֡�����������Ǵ������ҳ�Ţ��� "+accCode+" ��������´��� "+detail);
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
			log.setDescription("��䢤����������Ǵ������ҳ�Ţ��� "+accCode+" ���������´��� "+detail);
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
			log.setDescription("ź�����������Ǵ������ҳ�Ţ��� "+accCode+" ��������´��� "+detail);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - BudgetExpense ====================================================//
	
	//================================================ Start - PR ====================================================//
	/**
	 * �� log ������ҧ� PR
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
			log.setDescription("���ҧ��駨Ѵ���Ţ��� "+number+" �繨ӹǹ�Թ������ "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ������� PR
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
			log.setDescription("�����駨Ѵ���Ţ��� "+number+" �繨ӹǹ�Թ������ "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���¡��ԡ� PR
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
			log.setDescription("¡��ԡ��駨Ѵ���Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��þ����� PR
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
			log.setDescription("�������駨Ѵ���Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - PR ====================================================//
	
	//================================================ Start - PO ====================================================//
	/**
	 * �� log ������ҧ� PO
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
			log.setDescription("���ҧ��駨Ѵ�����Ţ��� "+number+" �繨ӹǹ�Թ������ "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ������� PO
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
			log.setDescription("�����駨Ѵ�����Ţ��� "+number+" �繨ӹǹ�Թ������ "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���ź� PO
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
			log.setDescription("¡��ԡ��駨Ѵ�����Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��ûԴ� PO
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
			log.setDescription("�Դ��駨Ѵ�����Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��þ����� PO
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
			log.setDescription("��������§ҹ��͹��ѵԨѴ�Ҿ�ʴ� "+number);
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
			log.setDescription("�������駨Ѵ�����Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - PO ====================================================//
	
	//================================================ Start - Vendor ====================================================//
	/**
	 * �� log ������ҧ��ҹ���
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
			log.setDescription("������ҹ����Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��������ҹ���
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
			log.setDescription("�����ҹ����Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���ź��ҹ���
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
			log.setDescription("ź��ҹ����Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - Vendor ====================================================//
	
	//================================================ Start - MaterialGroup ====================================================//
	/**
	 * �� log ������ҧ�������ʴ�
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
			log.setDescription("�����������ʴ����� "+code);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log �����䢡������ʴ�
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
			log.setDescription("��䢡������ʴ����� "+code);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���ź�������ʴ�
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
			log.setDescription("ź�������ʴ����� "+code);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - MaterialGroup ====================================================//
	
	//================================================ Start - Material ====================================================//
	/**
	 * �� log ������ҧ�������ʴ�
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
			log.setDescription("������ʴ����� "+matCode+" �����������ʴ����� "+groupCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log �����䢡������ʴ�
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
			log.setDescription("�����ʴ����� "+matCode+" �����������ʴ����� "+groupCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���ź�������ʴ�
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
			log.setDescription("ź��ʴ����� "+matCode+" �����������ʴ����� "+groupCode);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - Material ====================================================//
	
	//================================================ Start - GR ====================================================//
	/**
	 * �� log ������ҧ� GR
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
			log.setDescription("���ҧ��Ѻ��ʴ��Ţ��� "+number+" �繨ӹǹ�Թ������ "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ������� GR
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
			log.setDescription("�����Ѻ��ʴ��Ţ��� "+number+" �繨ӹǹ�Թ������ "+totalPrice);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���¡��ԡ� GR
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
			log.setDescription("¡��ԡ��Ѻ��ʴ��Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��ûԴ� GR
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
			log.setDescription("�Դ��Ѻ��ʴ��Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ��þ����� GR
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
			log.setDescription("�������Ѻ��ʴ��Ţ��� "+number);
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - GR ====================================================//
	
	//================================================ Start - BudgetAdmin ====================================================//
	/**
	 * �� log ������ҧ user � Budget
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
			log.setDescription("��������� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ������ user � Budget
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
			log.setDescription("��䢼���� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���ź user � Budget
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
			log.setDescription("ź����� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log �������Է�ԡ���駺����ҳ
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
			log.setDescription("����Է�ԡ���駺����ҳ�ͧ����� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	//================================================ End - BudgetAdmin ====================================================//
	
	//================================================ Start - PurchasingAdmin ====================================================//
	/**
	 * �� log ������ҧ user � Purchasing
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
			log.setDescription("��������� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ������ user � Purchasing
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
			log.setDescription("��䢼���� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �� log ���ź user � Purchasing
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
			log.setDescription("ź����� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}	
	//================================================ End - PurchasingAdmin ====================================================//
	
	//================================================ Start - StockAdmin ====================================================//
	
	/**
	 * �� log ������ҧ user � Stock
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
			log.setDescription("��������� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �� log ������ user � Stock
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
			log.setDescription("��䢼���� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �� log ���ź user � Stock
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
			log.setDescription("ź����� "+username+" � "+getMainSystemName(system));
			logController.logRecord(log);
		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}	
	
	//================================================ End - StockAdmin ====================================================//
	
	public String getSystemName(String aSystemName){
		String systemName = "";
		if (aSystemName.equals(Constants.WH_MAHAMEK)) {
			systemName = "���������";
		}
		if (aSystemName.equals(Constants.WH_CHIANGMAI)) {
			systemName = "�ٹ����§����";
		}
		if (aSystemName.equals(Constants.WH_HADYAI)) {
			systemName = "�ٹ���Ҵ�˭�";
		}
		if (aSystemName.equals(Constants.WH_HUAHIN)) {
			systemName = "�ٹ������Թ";
		}
		if (aSystemName.equals(Constants.WH_NAKORN)) {
			systemName = "�ٹ�칤��Ҫ����";
		}
		if (aSystemName.equals(Constants.WH_PHITSANULOK)) {
			systemName = "�ٹ���ɳ��š";
		}
		if (aSystemName.equals(Constants.WH_PUKET)) {
			systemName = "�ٹ������";
		}
		if (aSystemName.equals(Constants.WH_SURAT)) {
			systemName = "�ٹ������ɮ��ҹ�";
		}
		if (aSystemName.equals(Constants.WH_UBON)) {
			systemName = "�ٹ���غ��Ҫ�ҹ�";
		}
		if (aSystemName.equals(Constants.WH_UDON)) {
			systemName = "�ٹ���شøҹ�";
		}
		return systemName;
	}
	
	public String getMainSystemName(String aMainSystemName) {
		String mainSystemName = "";
		if (aMainSystemName.equals(Constants.SYS_MAIN_BUDGET)) {
			mainSystemName = "�к�������ҳ";
		}
		if (aMainSystemName.equals(Constants.SYS_MAIN_PURCHASING)) {
			mainSystemName = "�к��Ѵ���ͨѴ��";
		}
		if (aMainSystemName.equals(Constants.SYS_MAIN_STOCK)) {
			mainSystemName = "�к��Ѵ��ä�ѧ";
		}
		return mainSystemName;
	}
	
//	public String getSystemName(String aSystemName){
//		String systemName = "";
//		if (aSystemName.equals(Constants.SYS_BUDGET_BUDGET)){
//			systemName = "�к�������ҳ";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_CANTEEN)){
//			systemName = "�к��ҹ��������������";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_SPORT)){
//			systemName = "�к��ҹ����";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_NURSERY)){
//			systemName = "�к��ҹʶҹ�Ѻ����§��";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_HOTEL)){
//			systemName = "�к��ҹ��ͧ�ѡ����پ��";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_MEDICAL)){
//			systemName = "�к��ҹ�Ǫ�ѳ��";
//		}
//		if (aSystemName.equals(Constants.SYS_PURCHASING_SWIMMING)){
//			systemName = "�к��ҹ������¹��";	
//		}
//		if (aSystemName.equals(Constants.SYS_MAIN_LOG)){
//			systemName = "�к���Ǩ�ͺ Log";
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
//				throw new ControllerException("��辺���ͼ���� "+searchedUser);
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
			if (selectedDay.equalsIgnoreCase("")) throw new ControllerException("��س��кبӹǹ�ѹ");
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
		typeList.add(new SelectItem("-1","���͡������"));
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
