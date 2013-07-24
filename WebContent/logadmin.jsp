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
	<title><h:outputText value="Welfare System : PageName " /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=TIS-620" />
	<link rel="stylesheet" type="text/css" href="assets/css/templatestyle.css" />
	<link rel="stylesheet" type="text/css" href="assets/css/facestyle.css" />
	</head>
	<body>
	<div id="page">
	<jsp:include page="header.jsp" />
	<jsp:include page="menu.jsp" />
	<div id="contentarea">
	<h:form>
	<rich:panel>
		<h:outputText value="Status: " />
		<a4j:region>
				<a4j:status id="showload" stopStyle="color:green" startStyle="color:red">
					<f:facet name="start">					
						<h:outputText value="Please Wait ......." />
					</f:facet>
					<f:facet name="stop">
						<h:outputText value="OK" />					
					</f:facet>
				</a4j:status>
		</a4j:region>
		</rich:panel>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="controlPanel">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="ตัวควบคุม" />
				</h:panelGroup>
			</f:facet>
			<h:outputText value="ผู้ใช้: " />
			<h:inputText id="searchUser" value="#{logManager.searchedUser}" style="margin-right:5px"/>
			<h:outputText value="ประเภท: "/>
			<h:selectOneMenu id="logType" value="#{logManager.selectedType}">
				<f:selectItems value="#{logManager.typeList}"/>
				<a4j:support event="onchange" action="#{logManager.logTypeComboboxSelected}"/>
			</h:selectOneMenu>
			<a4j:commandButton action="#{logManager.logList}" value="แสดง Log ทั้งหมด" reRender="tableLogList, controlPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" style="float:right" status="showload"/>
			<div id="wrapper"></div>
			<rich:spacer height="20"/>
			<h:outputText value="ช่วงเวลา: " />
			<rich:calendar value="#{logManager.fromDateTime}" locale="th" datePattern="dd/MM/yy" style="margin-right:5px"/>
			<h:outputText value="ถึง" style="margin-right:5px"/>
			<rich:calendar value="#{logManager.toDateTime}" locale="th" datePattern="dd/MM/yy" style="margin-right:5px"/>
			<div id="wrapper"></div>
			<a4j:commandButton action="#{logManager.searchLog}" value="ค้นหา" style="margin-top:5px" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload" reRender="listPanel"/>
			<div id="wrapper"></div>
			<rich:spacer height="20"/>
			<h:outputText value="ลบ Log ที่มีอายุมากกว่า: " />	
			<h:selectOneRadio id="radio" value="#{logManager.selectedDay}">
				<f:selectItem itemLabel="30 วัน" itemValue="30"/>
				<f:selectItem itemLabel="60 วัน" itemValue="60"/>
				<f:selectItem itemLabel="90 วัน" itemValue="90"/>
			</h:selectOneRadio>
			<a4j:commandButton action="#{logManager.deleteLog}" value="ลบ" reRender="tableLogList, radio" ajaxSingle="false" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
		</rich:panel>
		<rich:panel id="listPanel">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="รายการ Log" />
				</h:panelGroup>
			</f:facet>
		<rich:dataTable
				id="tableLogList"
				value="#{logManager.logList}" 
				var="log"  
				rows="30"
				rowKeyVar="rowNo"						
				style="width:100%"
				rowClasses="odd-row, even-row"			
				>
				<rich:column style="text-align:left" width="10%" sortBy="#{log.user.username}">
					<f:facet name="header"><h:outputText value="ชื่อผู้ใช้งาน"/></f:facet>
					<h:outputText value="#{log.user.username}"/>
				</rich:column>
				<rich:column style="text-align:left" width="10%">
					<f:facet name="header"><h:outputText value="IP Address"/></f:facet>
					<h:outputText value="#{log.ip}"/>
				</rich:column>
				<rich:column style="text-align:left" width="15%" sortBy="#{log.dateTime}">
					<f:facet name="header"><h:outputText value="เวลาที่บันทึก"/></f:facet>
					<h:outputText value="#{log.dateTime}">
						<f:convertDateTime pattern="dd-MM-yyyy HH:mm:ss"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" width="10%" sortBy="#{log.logType}">
					<f:facet name="header"><h:outputText value="ประเภทของ Log"/></f:facet>
					<h:outputText value="#{log.logType}"/>
				</rich:column>
				<rich:column style="text-align:left" width="55%">
					<f:facet name="header"><h:outputText value="รายละเอียด"/></f:facet>
					<h:outputText value="#{log.description}"/>
				</rich:column>
			</rich:dataTable>
			<rich:datascroller  renderIfSinglePage="false" id="sc" align="center" for="tableLogList" />
		</rich:panel>		
		</a4j:region>
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
