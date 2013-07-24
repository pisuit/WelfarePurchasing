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
		<h:outputText value="คุณ#{stockMat.welcomeMsg}" style="color:blue" rendered="#{stockMat.welcomeMsg != null}"/>
		</div>
		</rich:panel>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="listPanel" style="float:left; width:100%">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="กลุ่มวัสดุ" />
				</h:panelGroup>
			</f:facet>
			<h:outputText value="กลุ่มวัสดุ" styleClass="label" />
			<h:selectOneMenu label="กลุ่มวัสดุ" value="#{stockMat.selectedMaterialGroupID}" styleClass="input" style="width:70%">
				<f:selectItems value="#{stockMat.materialGroupSelectItemList}"/>
				<a4j:support action="#{stockMat.materialGroupComboboxSelected}" event="onchange" reRender="listPanel, editPanel" status="showload"/>
			</h:selectOneMenu>
			<div id="wrapper"></div>
			<rich:dataTable
				id="tableMaterialList"
				value="#{stockMat.materialList}" 
				var="material"  
				rows="20" 
				rowKeyVar="rowNo"
				style="width:100%"
				rowClasses="odd-row, even-row"				
				>
				<a4j:support event="onRowClick" action="#{stockMat.materialTableRowClicked}" reRender="editPanel" status="showload">
					<f:setPropertyActionListener value="#{material}" target="#{stockMat.editMaterial}" />
				</a4j:support>
				<rich:column style="text-align:right"  width="20px">
					<f:facet name="header"><h:outputText value="ลำดับที่"/></f:facet>
					<h:outputText value="#{rowNo+1}"/>
				</rich:column>
				<rich:column style="text-align:left"  width="50px">
					<f:facet name="header"><h:outputText value="รหัสวัสดุ"/></f:facet>
					<h:outputText value="#{material.code}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" >
					<f:facet name="header"><h:outputText value="ชื่อวัสดุ"/></f:facet>
					<h:outputText value="#{material.description}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" >
					<f:facet name="header"><h:outputText value="หน่วยเบิก"/></f:facet>
					<h:outputText value="#{material.issueUnit}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" >
					<f:facet name="header"><h:outputText value="หน่วยจัดซื้อ"/></f:facet>
					<h:outputText value="#{material.orderUnit}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:right" >
					<f:facet name="header"><h:outputText value="ราคาต่อหน่วย"/></f:facet>
					<h:outputText value="#{material.orderUnitPrice}">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:right" >
					<f:facet name="header"><h:outputText value="จำนวนต่ำสุด"/></f:facet>
					<h:outputText value="#{material.minStock}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:right" >
					<f:facet name="header"><h:outputText value="จำนวนสูงสุด"/></f:facet>
					<h:outputText value="#{material.maxStock}">
					</h:outputText>
				</rich:column>
				<f:facet name="footer">
					<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
				</f:facet>
			</rich:dataTable>
		</rich:panel>
		</a4j:region>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="editPanel" style="float:left; width:100%">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="กำหนดวัสดุ" />
				</h:panelGroup>
			</f:facet>
			<h:outputText value="รหัสวัสดุ" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.code}" label="รหัสวัสดุ" styleClass="input medium" style="text-align:right" required="true">
				<f:validateLength maximum="4"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="ชื่อวัสดุ" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.description}" label="ชื่อวัสดุ" styleClass="input medium" style="width:60%" required="true">
				<f:validateLength maximum="50"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="หน่วยเบิก" styleClass="label medium" />
			<rich:comboBox value="#{stockMat.editMaterial.issueUnit}" label="หน่วยเบิก" suggestionValues="#{stockMat.issueUnitList}" styleClass="input medium" style="float:left;width:60%" required="true">
				<f:validateLength maximum="25"/>
			</rich:comboBox>
			<div id="wrapper"></div>
			<h:outputText value="หน่วยจัดซื้อ" styleClass="label medium" />
			<rich:comboBox value="#{stockMat.editMaterial.orderUnit}" label="หน่วยจัดซื้อ" suggestionValues="#{stockMat.orderUnitList}" styleClass="input medium" style="float:left;width:60%" required="true">
				<f:validateLength maximum="25"/>
			</rich:comboBox>
			<div id="wrapper"></div>
			<h:outputText value="หน่วยเบิก/หน่วยจัดซื้อ" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.unitConverter}" label="หน่วยเบิก/หน่วยจัดซื้อ" styleClass="input medium" style="text-align:right" required="true">
				<f:convertNumber pattern="#,##0.00"/>
				<f:validateDoubleRange minimum="1"/>
			</h:inputText>
			<h:outputText value="* ระบุค่าที่ใช้สำหรับแปลงจากหน่วยจัดซื้อไปเป็นหน่วยเบิก"/>
			<div id="wrapper"></div>
			<h:outputText value="ราคาต่อหน่วยจัดซื้อ" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.orderUnitPrice}" label="ราคาต่อหน่วย" styleClass="input medium" style="text-align:right" required="true">
				<f:convertNumber pattern="#,##0.00"/>
				<f:validateDoubleRange minimum="1"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="จำนวนต่ำสุด" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.minStock}" label="จำนวนต่ำสุด" styleClass="input medium" style="text-align:right" required="true">
				<f:convertNumber pattern="#,##0.00"/>
				<f:validateDoubleRange minimum="0"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="จำนวนสูงสุด" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.maxStock}" label="จำนวนสูงสุด" styleClass="input medium" style="text-align:right" required="true">
				<f:convertNumber pattern="#,##0.00"/>
				<f:validateDoubleRange minimum="1"/>
			</h:inputText>
			<div id="wrapper"></div>
			<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
			<a4j:commandButton action="#{stockMat.newMaterial}" value="เพิ่มวัสดุ" reRender="listPanel, editPanel" disabled ="#{stockMat.editMaterial.id == null || !stockMat.editable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{stockMat.saveMaterial}" value="บันทึกวัสดุ" reRender="listPanel, editPanel" disabled ="#{!stockMat.editable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{stockMat.deleteMaterial}" value="ลบวัสดุ" reRender="listPanel, editPanel" disabled ="#{stockMat.editMaterial.id == null || !stockMat.editable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
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
