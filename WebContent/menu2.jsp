<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ page language="java" contentType="text/html; charset=TIS-620" pageEncoding="TIS-620"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

	
	<div id="menu">
	<c:if test="${not empty user}" >
	<div id="menuitem"><a href="welcome.jsf"><span><h:outputText value="Home" /></span></a></div>
	<div id="menuspacer"></div>
	<c:if test="${user.pageAuthorizations.BudgetMain}">
	<div id="menuitem"><a href="budgetmain.jsf"><span><h:outputText value="Budget Management - Main" /></span></a></div>
	<div id="menuspacer"></div>
	</c:if>
	<c:if test="${user.pageAuthorizations.BudgetDetail}">
	<div id="menuitem"><a href="budgetdetail.jsf"><span><h:outputText value="Budget Management - Detail" /></span></a></div>
	<div id="menuspacer"></div>
	</c:if>
	<c:if test="${user.pageAuthorizations.BudgetDetail}">
	<div id="menuitem"><a href="budgetdetail.jsf"><span><h:outputText value="Budget Management - Detail" /></span></a></div>
	<div id="menuspacer"></div>
	</c:if>
	<c:if test="${user.pageAuthorizations.BudgetTransferRequest}">
	<div id="menuitem"><a href="budgettransfrerequest.jsf"><span><h:outputText value="Transfer Request" /></span></a></div>
	<div id="menuspacer"></div>
	</c:if>
	<c:if test="${user.pageAuthorizations.BudgetAdmin}">
	<div id="menuitem"><a href="budgetadmin.jsf"><span><h:outputText value="Administration" /></span></a></div>
	<div id="menuspacer"></div>
	</c:if>
	</c:if>
	<div id="menuitem"><a href="manual.jsf"><span><h:outputText value="Manual" /></span></a></div>
	<div id="menuspacer"></div>
	</div>
	<div id="menu_bottom"></div>
