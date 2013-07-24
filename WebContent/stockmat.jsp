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
		<h:outputText value="�Թ�յ�͹�Ѻ : "/>
		<h:outputText value="�س#{stockMat.welcomeMsg}" style="color:blue" rendered="#{stockMat.welcomeMsg != null}"/>
		</div>
		</rich:panel>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="listPanel" style="float:left; width:100%">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="�������ʴ�" />
				</h:panelGroup>
			</f:facet>
			<h:outputText value="�������ʴ�" styleClass="label" />
			<h:selectOneMenu label="�������ʴ�" value="#{stockMat.selectedMaterialGroupID}" styleClass="input" style="width:70%">
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
					<f:facet name="header"><h:outputText value="�ӴѺ���"/></f:facet>
					<h:outputText value="#{rowNo+1}"/>
				</rich:column>
				<rich:column style="text-align:left"  width="50px">
					<f:facet name="header"><h:outputText value="������ʴ�"/></f:facet>
					<h:outputText value="#{material.code}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" >
					<f:facet name="header"><h:outputText value="������ʴ�"/></f:facet>
					<h:outputText value="#{material.description}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" >
					<f:facet name="header"><h:outputText value="˹����ԡ"/></f:facet>
					<h:outputText value="#{material.issueUnit}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" >
					<f:facet name="header"><h:outputText value="˹��¨Ѵ����"/></f:facet>
					<h:outputText value="#{material.orderUnit}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:right" >
					<f:facet name="header"><h:outputText value="�Ҥҵ��˹���"/></f:facet>
					<h:outputText value="#{material.orderUnitPrice}">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:right" >
					<f:facet name="header"><h:outputText value="�ӹǹ����ش"/></f:facet>
					<h:outputText value="#{material.minStock}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:right" >
					<f:facet name="header"><h:outputText value="�ӹǹ�٧�ش"/></f:facet>
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
					<h:outputText value="��˹���ʴ�" />
				</h:panelGroup>
			</f:facet>
			<h:outputText value="������ʴ�" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.code}" label="������ʴ�" styleClass="input medium" style="text-align:right" required="true">
				<f:validateLength maximum="4"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="������ʴ�" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.description}" label="������ʴ�" styleClass="input medium" style="width:60%" required="true">
				<f:validateLength maximum="50"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="˹����ԡ" styleClass="label medium" />
			<rich:comboBox value="#{stockMat.editMaterial.issueUnit}" label="˹����ԡ" suggestionValues="#{stockMat.issueUnitList}" styleClass="input medium" style="float:left;width:60%" required="true">
				<f:validateLength maximum="25"/>
			</rich:comboBox>
			<div id="wrapper"></div>
			<h:outputText value="˹��¨Ѵ����" styleClass="label medium" />
			<rich:comboBox value="#{stockMat.editMaterial.orderUnit}" label="˹��¨Ѵ����" suggestionValues="#{stockMat.orderUnitList}" styleClass="input medium" style="float:left;width:60%" required="true">
				<f:validateLength maximum="25"/>
			</rich:comboBox>
			<div id="wrapper"></div>
			<h:outputText value="˹����ԡ/˹��¨Ѵ����" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.unitConverter}" label="˹����ԡ/˹��¨Ѵ����" styleClass="input medium" style="text-align:right" required="true">
				<f:convertNumber pattern="#,##0.00"/>
				<f:validateDoubleRange minimum="1"/>
			</h:inputText>
			<h:outputText value="* �кؤ�ҷ��������Ѻ�ŧ�ҡ˹��¨Ѵ�������˹����ԡ"/>
			<div id="wrapper"></div>
			<h:outputText value="�Ҥҵ��˹��¨Ѵ����" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.orderUnitPrice}" label="�Ҥҵ��˹���" styleClass="input medium" style="text-align:right" required="true">
				<f:convertNumber pattern="#,##0.00"/>
				<f:validateDoubleRange minimum="1"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="�ӹǹ����ش" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.minStock}" label="�ӹǹ����ش" styleClass="input medium" style="text-align:right" required="true">
				<f:convertNumber pattern="#,##0.00"/>
				<f:validateDoubleRange minimum="0"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="�ӹǹ�٧�ش" styleClass="label medium" />
			<h:inputText value="#{stockMat.editMaterial.maxStock}" label="�ӹǹ�٧�ش" styleClass="input medium" style="text-align:right" required="true">
				<f:convertNumber pattern="#,##0.00"/>
				<f:validateDoubleRange minimum="1"/>
			</h:inputText>
			<div id="wrapper"></div>
			<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
			<a4j:commandButton action="#{stockMat.newMaterial}" value="������ʴ�" reRender="listPanel, editPanel" disabled ="#{stockMat.editMaterial.id == null || !stockMat.editable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{stockMat.saveMaterial}" value="�ѹ�֡��ʴ�" reRender="listPanel, editPanel" disabled ="#{!stockMat.editable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{stockMat.deleteMaterial}" value="ź��ʴ�" reRender="listPanel, editPanel" disabled ="#{stockMat.editMaterial.id == null || !stockMat.editable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
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
