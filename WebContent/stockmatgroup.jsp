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
	<title><h:outputText value="Welfare System : Manage Material Group " /></title>
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
		<div style="float:right">
		<h:outputText value="ยินดีต้อนรับ : "/>
		<h:outputText value="คุณ#{stockMatGroup.welcomeMsg}" style="color:blue" rendered="#{stockMatGroup.welcomeMsg != null}"/>
		</div>
		</rich:panel>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="listPanel" style="float:left; width:49%">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="กลุ่มวัสดุ" />
				</h:panelGroup>
			</f:facet>
			<rich:dataTable
				id="tableMaterialGroupList"
				value="#{stockMatGroup.materialGroupList}" 
				var="materialGroup"  
				rows="20" 
				rowKeyVar="rowNo"
				style="width:100%"	
				rowClasses="odd-row, even-row"			
				>
				<a4j:support event="onRowClick" action="#{stockMatGroup.materialTableRowClicked}" reRender="editPanel" status="showload">
					<f:setPropertyActionListener value="#{materialGroup}" target="#{stockMatGroup.editMaterialGroup}" />
				</a4j:support>
				<rich:column style="text-align:right"  width="20px">
					<f:facet name="header"><h:outputText value="ลำดับที่"/></f:facet>
					<h:outputText value="#{rowNo+1}"/>
				</rich:column>
				<rich:column style="text-align:left"  width="50px">
					<f:facet name="header"><h:outputText value="รหัสกลุ่ม"/></f:facet>
					<h:outputText value="#{materialGroup.code}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" >
					<f:facet name="header"><h:outputText value="ชื่อกลุ่ม"/></f:facet>
					<h:outputText value="#{materialGroup.description}">
					</h:outputText>
				</rich:column>
				<f:facet name="footer">
					<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
				</f:facet>
			</rich:dataTable>
		</rich:panel>
		</a4j:region>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="editPanel" style="float:left; width:49%">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="กำหนดกลุ่มวัสดุ" />
				</h:panelGroup>
			</f:facet>
			<h:outputText value="รหัสกลุ่มวัสดุ" styleClass="label medium"/>
			<h:inputText value="#{stockMatGroup.editMaterialGroup.code}" label="รหัสกลุ่มวัสดุ" styleClass="input medium" required="true">
				<f:validateLength maximum="2"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="ชื่อกลุ่มวัสดุ" styleClass="label medium" />
			<h:inputText value="#{stockMatGroup.editMaterialGroup.description}" label="ชื่อกลุ่มวัสดุ" styleClass="input medium" style="width:60%" required="true">
				<f:validateLength maximum="50"/>
			</h:inputText>
			<div id="wrapper"></div>
			<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
			<a4j:commandButton action="#{stockMatGroup.newMaterialGroup}" value="เพิ่มกลุ่มวัสดุ" reRender="listPanel, editPanel" disabled ="#{stockMatGroup.editMaterialGroup.id == null || !stockMatGroup.editable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{stockMatGroup.saveMaterialGroup}" value="บันทึกกลุ่มวัสดุ" reRender="listPanel, editPanel" disabled ="#{!stockMatGroup.editable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{stockMatGroup.deleteMaterialGroup}" value="ลบกลุ่มวัสดุ" reRender="listPanel, editPanel" disabled ="#{stockMatGroup.editMaterialGroup.id == null || !stockMatGroup.editable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
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
