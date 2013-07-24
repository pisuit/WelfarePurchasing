<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ page language="java" contentType="text/html; charset=TIS-620" pageEncoding="TIS-620"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<f:view>
	<head>
	<title><h:outputText value="Welfare Purchasing System : Login" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=TIS-620" />
	<link rel="stylesheet" type="text/css" href="assets/css/templatestyle.css" />
	<link rel="stylesheet" type="text/css" href="assets/css/facestyle.css" />
	</head>
	<body>
	<div id="page">
	<jsp:include page="header.jsp" />
	<jsp:include page="menu.jsp" />
	<div id="contentarea">
	<h:form >
		<rich:panel style="text-align:center; border:0px; " rendered="#{sessionScope.user == null}">
			<rich:panel style="width:300px; text-align:left; ">
				<f:facet name="header">
					<h:panelGroup>
						<h:graphicImage value="/assets/icons/login.png" width="24" height="24"/>
						<h:outputText value="Login" />
					</h:panelGroup>
				</f:facet>
				<div id="wrapper"></div>
				<h:outputText value="ระบบงาน" styleClass="label" style="width:100px" />
				<h:selectOneMenu value="#{login.selectedItem}" style="width:155px">
					<f:selectItems value="#{login.selectItems}"/>
				</h:selectOneMenu>
				<div id="wrapper"></div>
				<h:outputText value="ชื่อผู้ใช้งาน" styleClass="label" style="width:100px" />
				<h:inputText value="#{login.username}" styleClass="input medium" required="true" requiredMessage="กรุณาใส่ชื่อผู้ใช้งาน" />
				<div id="wrapper"></div>
				<h:outputText value="รหัสผ่าน" styleClass="label" style="width:100px" />
				<h:inputSecret value="#{login.password}" styleClass="input medium" required="true" requiredMessage="กรุณาใส่รหัสผ่าน" />
				<div id="wrapper"></div>
				<a4j:commandButton value="เข้าสู่ระบบ" action="#{login.login}" style="width:75px" type="submit" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" >
					<rich:componentControl for="mpStatus" operation="show" event="onclick"/>
				</a4j:commandButton>
				<div id="wrapper"></div>
			</rich:panel>
		</rich:panel>
		<rich:modalPanel id="mpErrors" resizeable="true">
			<f:facet name="header">
				<h:outputText value="Error" />
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<h:graphicImage value="/assets/icons/close.png" id="hideError" style="cursor:pointer" />
					<rich:componentControl for="mpErrors" attachTo="hideError" operation="hide" event="onclick" />
				</h:panelGroup>
			</f:facet>
			<rich:messages layout="table">
				<f:facet name="errorMarker">
					<h:graphicImage url="/assets/icons/critical.png" />
				</f:facet>
			</rich:messages>
		</rich:modalPanel>
		<rich:modalPanel id="mpStatus" autosized="true" width="800" height="100">
			<a4j:status onstop="#{rich:component('mpStatus')}.hide()" >
	               <f:facet name="start">
	                   <h:graphicImage  value="/assets/icons/loading14.gif"/>
	               </f:facet>
	        </a4j:status>        
		</rich:modalPanel>
	</h:form>
	</div>
	<div id="content_bottom">
	<div id="content_bottom_left"></div>
	<div id="content_bottom_center"></div>
	<div id="content_bottom_right"></div>
	</div>
	<jsp:include page="footer.jsp" />
	</div>
	</body>
</f:view>
</html>
