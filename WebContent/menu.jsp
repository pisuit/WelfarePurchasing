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
		<rich:dropDownMenu value="�к�">
			<rich:menuItem value="����к�" action="LOGIN" disabled="#{sessionScope.user != null}"/>		
			<rich:menuItem value="�͡�ҡ�к�" action="#{login.logout}" disabled="#{sessionScope.user == null}"/>		
			<rich:menuSeparator />
			<rich:menuItem value="˹���á" action="MENU_HOME"/>		
		</rich:dropDownMenu>
		<rich:dropDownMenu value="������ҳ">
			<rich:menuItem value="��Ǵ��ѡ" action="MENU_BUDGET_MAIN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_MAIN == null}"/>
			<rich:menuItem value="��Ǵ����" action="MENU_BUDGET_DETAIL" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_SUB == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="���͹������ҳ" action="MENU_BUDGET_TRANSFER_REQUEST" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_TRANSFER_REQUEST == null}"/>
			<rich:menuItem value="͹��ѵ�㺢��͹������ҳ" action="MENU_BUDGET_TRANSFER_APPROVE" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_TRANSFER_APPROVE_MAIN == null && sessionScope.user.pageAuthorizations.ROLE_BUDGET_TRANSFER_APPROVE_SUB == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="�ѹ�֡��������" action="MENU_BUDGET_EXPENSE" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_EXPENSE == null}"/>
		</rich:dropDownMenu>
		<rich:dropDownMenu value="�Ѵ���ͨѴ��">
			<rich:menuItem value="�駨Ѵ��" action="#{menu.pr}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_PR == null}"/>
			<rich:menuItem value="�Ѵ����" action="#{menu.po}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_PO == null}"/>
			<rich:menuItem value="�Ѻ��ʴ�" action="#{menu.gr}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_GR == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="��ҹ���" action="MENU_PURCHASING_VENDOR" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_VENDOR == null}"/>
		</rich:dropDownMenu>
		<rich:dropDownMenu value="��ѧ">
			<rich:menuItem value="�Ѵ��á������ʴ�" action="MENU_STOCK_MATGROUP" disabled="#{sessionScope.user.pageAuthorizations.ROLE_STOCK_MATGROUP == null}"/>
			<rich:menuItem value="�Ѵ�����ʴ�" action="MENU_STOCK_MAT" disabled="#{sessionScope.user.pageAuthorizations.ROLE_STOCK_MAT == null}"/>
			<rich:menuItem value="��¡������͹���" action="#{menu.movement}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_MAT == null}" rendered="false"/>
			<rich:menuItem value="��¡����ʴط��ӹǹ��ӡ��� minimum" action="MENU_STOCK_MINIMUM" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_MAT == null}" rendered="false"/>
			<rich:menuSeparator />
			<rich:menuItem value="�ԡ��ʴ�" action="#{menu.gi}" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_GI == null}" rendered="false"/>
			<rich:menuItem value="�׹��ʴ�" action="MENU_STOCK_RETURN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_RETURN == null}" rendered="false"/>
			<rich:menuItem value="�͹��ʴ�" action="MENU_STOCK_TRANSFER" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_TRANSFER == null}" rendered="false"/>
			<rich:menuItem value="�ͧ���ͨѴ����§" action="MENU_STOCK_BANQUET" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_BANQUET == null}" rendered="false"/>
			<rich:menuItem value="����Ң�����������¹��" action="MENU_STOCK_SWIMMING" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_SWIMMING == null}" rendered="false"/>
			<rich:menuItem value="ʶԵ������" action="MENU_STOCK_NURSERY_CHILDEN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_NURSERY_CHILDEN == null}" rendered="false"/>
		</rich:dropDownMenu>
		<rich:dropDownMenu value="�������к�">
			<rich:menuItem value="�������к�������ҳ" action="MENU_BUDGET_ADMIN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_BUDGET_ADMIN == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="�������к��Ѵ���ͨѴ��" action="MENU_PURCHASING_ADMIN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_PURCHASING_ADMIN == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="�������к��Ѵ��ä�ѧ" action="MENU_STOCK_ADMIN" disabled="#{sessionScope.user.pageAuthorizations.ROLE_STOCK_ADMIN == null}"/>
			<rich:menuSeparator />
			<rich:menuItem value="����Ǩ�ͺ�к� Log" action="MENU_LOG" disabled="#{sessionScope.user.pageAuthorizations.ROLE_LOG == null}"/>
		</rich:dropDownMenu>
		<rich:dropDownMenu value="���������">
			<rich:menuItem value="������" action="MENU_HELP_MANUAL"/>
		</rich:dropDownMenu>
	</rich:toolBar>
	</h:form>
	</div>
	<div id="menu_bottom"></div>
