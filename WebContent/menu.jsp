<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ page language="java" contentType="text/html; charset=TIS-620" pageEncoding="TIS-620"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	
	<div id="menu">
	<h:form id="menuForm">
	<rich:toolBar style="width:1005px">
		<rich:dropDownMenu value="ระบบ">
			<rich:menuItem value="เข้าระบบ" action="LOGIN" disabled="#{sessionScope.user != null}"/>		
			<rich:menuItem value="ออกจากระบบ" action="#{login.logout}" disabled="#{sessionScope.user == null}"/>		
			<rich:menuSeparator />
			<rich:menuItem value="หน้าแรก" action="MENU_HOME"/>		
		</rich:dropDownMenu>
		<rich:dropDownMenu value="งบประมาณ">
			<rich:menuItem value="หมวดหลัก" action="MENU_BUDGET_MAIN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_MAIN == null}"/>
			<rich:menuItem value="หมวดย่อย" action="MENU_BUDGET_DETAIL" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_SUB == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="ขอโอนงบประมาณ" action="MENU_BUDGET_TRANSFER_REQUEST" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_TRANSFER_REQUEST == null}"/>
			<rich:menuItem value="อนุมัติใบขอโอนงบประมาณ" action="MENU_BUDGET_TRANSFER_APPROVE" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_TRANSFER_APPROVE_MAIN == null && sessionScope.user.pageAuthorizations.ROLE_BUDGET_TRANSFER_APPROVE_SUB == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="บันทึกค่าใข้จ่าย" action="MENU_BUDGET_EXPENSE" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_EXPENSE == null}"/>
		</rich:dropDownMenu>
		<rich:dropDownMenu value="จัดซื้อจัดหา">
			<rich:menuItem value="แจ้งจัดหา" action="#{menu.pr}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_PR == null}"/>
			<rich:menuItem value="จัดซื้อ" action="#{menu.po}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_PO == null}"/>
			<rich:menuItem value="รับวัสดุ" action="#{menu.gr}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_GR == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="ร้านค้า" action="MENU_PURCHASING_VENDOR" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_VENDOR == null}"/>
		</rich:dropDownMenu>
		<rich:dropDownMenu value="คลัง">
			<rich:menuItem value="จัดการกลุ่มวัสดุ" action="MENU_STOCK_MATGROUP" disabled="#{sessionScope.user.pageAuthorizations.ROLE_STOCK_MATGROUP == null}"/>
			<rich:menuItem value="จัดการวัสดุ" action="MENU_STOCK_MAT" disabled="#{sessionScope.user.pageAuthorizations.ROLE_STOCK_MAT == null}"/>
			<rich:menuItem value="รายการเคลื่อนไหว" action="#{menu.movement}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_MAT == null}" rendered="false"/>
			<rich:menuItem value="รายการวัสดุที่จำนวนต่ำกว่า minimum" action="MENU_STOCK_MINIMUM" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_MAT == null}" rendered="false"/>
			<rich:menuSeparator />
			<rich:menuItem value="เบิกวัสดุ" action="#{menu.gi}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_GI == null}" rendered="false"/>
			<rich:menuItem value="คืนวัสดุ" action="MENU_STOCK_RETURN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_RETURN == null}" rendered="false"/>
			<rich:menuItem value="โอนวัสดุ" action="MENU_STOCK_TRANSFER" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_TRANSFER == null}" rendered="false"/>
			<rich:menuItem value="จองเพื่อจัดเลี้ยง" action="MENU_STOCK_BANQUET" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_BANQUET == null}" rendered="false"/>
			<rich:menuItem value="นำเข้าข้อมูลสระว่ายน้ำ" action="MENU_STOCK_SWIMMING" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_SWIMMING == null}" rendered="false"/>
			<rich:menuItem value="สถิติเด็กเล็ก" action="MENU_STOCK_NURSERY_CHILDEN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_NURSERY_CHILDEN == null}" rendered="false"/>
		</rich:dropDownMenu>
		<rich:dropDownMenu value="ผู้ดูแลระบบ">
			<rich:menuItem value="ผู้ดูแลระบบงบประมาณ" action="MENU_BUDGET_ADMIN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_ADMIN == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="ผู้ดูแลระบบจัดซื้อจัดหา" action="MENU_PURCHASING_ADMIN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_ADMIN == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="ผู้ดูแลระบบจัดการคลัง" action="MENU_STOCK_ADMIN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_STOCK_ADMIN == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="ผู้ตรวจสอบระบบ Log" action="MENU_LOG" disabled="#{sessionScope.user.pageAuthorizations.ROLE_LOG == null}"/>
		</rich:dropDownMenu>
		<rich:dropDownMenu value="ช่วยเหลือ">
			<rich:menuItem value="คู่มือ" action="MENU_HELP_MANUAL"/>
		</rich:dropDownMenu>
	</rich:toolBar>
	</h:form>
	</div>
	<div id="menu_bottom"></div>
