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
	<title><h:outputText value="Welfare System : Goods Receipt " /></title>
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
		<h:outputText value="�س#{purchasingGR.welcomeMsg}" style="color:blue" rendered="#{purchasingGR.welcomeMsg != null}"/>
		</div>
		</rich:panel>
		<a4j:region renderRegionOnly="false">
				<rich:panel id="controlPanel" style="width:100%">
					<f:facet name="header"><h:outputText value="��Ѻ��ʴ�" /> </f:facet>
					<h:outputText value="������" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingGR.selectedGRType}" style="float:left; margin-right:5px; width:150px" >
						<f:selectItems value="#{purchasingGR.grTypeSelectItemList}"/>
						<a4j:support action="#{purchasingGR.grTypeComboBoxSelected}" event="onchange" reRender="controlPanel, headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<h:outputText value="�����Ţ㺨Ѵ����" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingGR.selectedPoID}" style="float:left; margin-right:5px; width:100px" disabled="#{purchasingGR.grNoPo}">
						<f:selectItems value="#{purchasingGR.poSelectItemList}"/>
						<a4j:support action="#{purchasingGR.poComboBoxSelected}" event="onchange" reRender="controlPanel, headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
					</h:selectOneMenu>
					<h:outputText value="������ҳ" style="float:left; margin-right:5px" />
					<h:selectOneMenu label="������ҳ" value="#{purchasingGR.selectedBudgetItemID}" styleClass="input long" disabled="#{!purchasingGR.grNoPo}">
						<f:selectItems value="#{purchasingGR.budgetItemSelectItemList}"/>
						<a4j:support action="#{purchasingGR.budgetItemComboBoxSelected}" event="onchange" ajaxSingle="true" reRender="availableamount"/>
					</h:selectOneMenu>
					<h:panelGroup id="availableamount">
						<h:outputText value="�ӹǹ�Թ�������� " style="margin-right:3px;color:blue" rendered="#{purchasingGR.selectedBudgetItem != null}"/>
						<h:outputText value="#{purchasingGR.selectedBudgetItem.availableAmount}" rendered="#{purchasingGR.selectedBudgetItem != null}" style="color:blue">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</h:panelGroup>
					<div id="wrapper"></div>
					<rich:panel>
						<a4j:commandButton action="#{purchasingGR.newGR}" value="������¡������" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingGR.grCreatable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
						</a4j:commandButton>
						<a4j:commandButton action="#{purchasingGR.saveGR}" value="�ѹ�֡��Ѻ"  reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingGR.grSavable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
						</a4j:commandButton>
						<a4j:commandButton action="#{purchasingGR.deleteGR}" value="¡��ԡ��Ѻ" reRender="controlPanel, headerPanel, itemPanel,listPanel"  disabled ="#{!purchasingGR.grDeletable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
						</a4j:commandButton>
						<a4j:commandButton action="#{purchasingGR.closeGR}" value="�Դ��Ѻ" reRender="controlPanel, headerPanel, itemPanel,listPanel"  disabled ="#{!purchasingGR.grClosable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
						</a4j:commandButton>
						<h:commandLink action="#{purchasingGR.printGR}" value="�������Ѻ��ʴ�" immediate="true" target="_blank" rendered="#{purchasingGR.grPrintable}"/>
						<a4j:commandButton id="link" value="�ʴ���¡����Ѻ��ʴ�" ajaxSingle="true" disabled ="false"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" style="float:right">
						<rich:componentControl for="modalPanel" attachTo="link" operation="show" event="onclick"/>
					</a4j:commandButton>
					</rich:panel>
				</rich:panel>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="headerPanel" style="width:100%">
					<f:facet name="header"><h:outputText value="��ǹ���" /></f:facet>
					<h:outputText value="�Ţ�����Ѻ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.grNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<h:outputText value="�ѹ���ѹ�֡" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.postingDate}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
					<h:outputText value="ʶҹ�" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.status}" style="float:left; margin-right:5px;font-weight:bold">
					</h:outputText>
					<h:outputText value="�ӹǹ�Թ���" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.totalPrice}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<h:outputText value="�Ţ������觫���" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.purchaseOrder.poNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<div id="wrapper"></div>
					<h:outputText value="�ѹ����Ѻ��ʴ�" styleClass="label medium"/>
					<rich:calendar value="#{purchasingGR.editGR.receivedDate}" label="�ѹ����Ѻ��ʴ�" style="float:left"  locale="th" datePattern="dd/MM/yyyy">
					</rich:calendar>
					<div id="wrapper"></div>					
					<h:outputText value="㺡ӡѺ�Թ���/����" styleClass="label medium"/>
					<h:inputText id="invoiceNumber" value="#{purchasingGR.editGR.invoiceNumber}" label="㺡ӡѺ�Թ���/����" styleClass="input medium" required="false">
					</h:inputText>
					<div id="wrapper"></div>
					<h:outputText value="��ǹŴ���" styleClass="label medium"/>
					<h:inputText id="totalDiscountAmount" value="#{purchasingGR.editGR.totalDiscountAmount}" label="��ǹŴ���" styleClass="input medium" required="false">	
					</h:inputText>
					<a4j:commandButton action="#{purchasingGR.distributeDiscount}" value="��Ш����ǹŴ" reRender="controlPanel, headerPanel, itemPanel,listPanel, warningText" status="showload"/>
					<div id="wrapper"></div>					
					<h:outputText value="�˵ؼ���Ф�������" styleClass="label medium"/>
					<h:inputTextarea value="#{purchasingGR.editGR.reason}" label="�˵ؼ���Ф�������" styleClass="input" style="width:70%" rows="3"  >
						<f:validateLength maximum="255"/>
					</h:inputTextarea>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ���Ѻ��ʴ�" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.recipientName}" label="���ͼ���Ѻ��ʴ�" suggestionValues="#{purchasingGR.recipientNameList}"  styleClass="input medium" style="float:left" >
						<f:validateLength maximum="50"/>
					</rich:comboBox>										
					<h:outputText value="���˹觼���Ѻ��ʴ�" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.recipientPos}" label="���˹觼���Ѻ��ʴ�" suggestionValues="#{purchasingGR.recipientPosList}" styleClass="input medium" style="float:left" >
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ��ѹ�֡" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.entryName}" label="���ͼ��ѹ�֡" suggestionValues="#{purchasingGR.entryNameList}"  styleClass="input medium" style="float:left" >
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<h:outputText value="���˹觼��ѹ�֡" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.entryPos}" label="���˹觼��ѹ�֡" suggestionValues="#{purchasingGR.entryPosList}" styleClass="input medium" style="float:left" >
						<f:validateLength maximum="50"/>
					</rich:comboBox>					
					<div id="wrapper"></div>			
				</rich:panel>
				</a4j:region>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="itemPanel">
					<f:facet name="header"><h:outputText value="��¡����ʴ�" /></f:facet>
					<a4j:region renderRegionOnly="false">
					<rich:dataTable
						id="tableGRItemList"
						value="#{purchasingGR.grItemList}" 
						var="grItem"  
						rows="20" 
						rowKeyVar="rowNo"
						style="width:100%"	
						rowClasses="odd-row, even-row"			
						>
						<a4j:support event="onRowClick" action="#{purchasingGR.grItemTableRowClicked}" reRender="itemEditPanel, pricePanel" status="showload">
							<f:setPropertyActionListener value="#{grItem}" target="#{purchasingGR.editGRItem}" />
						</a4j:support>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="������ʴ�"/></f:facet>
							<h:outputText value="#{grItem.material.description}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="���;�ʴ����"/></f:facet>
							<h:outputText value="#{grItem.otherMaterial}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="70px">
							<f:facet name="header"><h:outputText value="�ӹǹ����Ѻ"/></f:facet>
							<h:outputText value="#{grItem.receivedQty}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="70px">
							<f:facet name="header"><h:outputText value="�ӹǹ�����觫���"/></f:facet>
							<h:outputText value="#{grItem.orderQty}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" width="80px">
							<f:facet name="header"><h:outputText value="˹���"/></f:facet>
							<h:outputText value="#{grItem.receiveUnit}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="100px">
							<f:facet name="header"><h:outputText value="�Ҥҵ��˹��·���Ѻ"/></f:facet>
							<h:outputText value="#{grItem.unitPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="�Ҥҵ��˹��·����觫���"/></f:facet>
							<h:outputText value="#{grItem.orderUnitPrice}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" >
							<f:facet name="header"><h:outputText value="��ǹŴ"/></f:facet>
							<h:outputText value="#{grItem.discountAmount}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" >
							<f:facet name="header"><h:outputText value="�Ҥ��ط��"/></f:facet>
							<h:outputText value="#{grItem.netPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="ʶҹ�"/></f:facet>
							<h:outputText value="#{grItem.status}">
							</h:outputText>
						</rich:column>
						<f:facet name="footer">
							<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
						</f:facet>
					</rich:dataTable>
					</a4j:region>
					<div id="wrapper"></div>
					<rich:spacer height="5px"/>
					<a4j:region renderRegionOnly="false">
					<rich:panel id="itemEditPanel" style="width:100%">
					<div style="float:right;width:29%">
					<rich:panel id="pricePanel" header="�������������">
						<h:outputText value="�Ҥ������͹�ѡ��ǹŴ:" styleClass="label custom"/>
						<h:outputText value="#{purchasingGR.editGRItem.totalprice}" >
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
						<div id="wrapper"></div>
						<h:outputText value="��ǹŴ:" styleClass="label custom"/>
						<h:outputText value="#{purchasingGR.editGRItem.discountAmount}" >
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
						<div id="wrapper"></div>
						<h:outputText value="�Ҥ��ط��:" styleClass="label custom"/>
						<h:outputText value="#{purchasingGR.editGRItem.netPrice}" >
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
						<div id="wrapper"></div>
						<h:outputText value="�Ҥ�����µ��˹���:" styleClass="label custom"/>
						<h:outputText value="#{purchasingGR.editGRItem.avgPrice}" >
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:panel>
					</div>
					<div style="float:left;width:70%">
						<h:outputText value="�������ʴ�" styleClass="label medium" />
							<h:selectOneMenu label="�������ʴ�" value="#{purchasingGR.selectedMaterialGroupID}" disabled="#{!purchasingGR.grNoPo}" styleClass="input medium" style="width:70%">
							<f:selectItems value="#{purchasingGR.materialGroupSelectItemList}"/>
							<a4j:support action="#{purchasingGR.materialGroupComboBoxSelected}" event="onchange" reRender="material, otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="��ʴ�" styleClass="label medium" />
							<h:selectOneMenu id="material" label="��ʴ�" value="#{purchasingGR.selectedMaterialID}" disabled="#{!purchasingGR.grNoPo}" styleClass="input medium" style="width:70%" >
							<f:selectItems value="#{purchasingGR.materialSelectItemList}"/>
							<a4j:support action="#{purchasingGR.materialComboBoxSelected}" event="onchange" reRender="otherMaterial, receiveUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="���;�ʴ����" styleClass="label medium"/>
						<h:inputText id="otherMaterial" value="#{purchasingGR.editGRItem.otherMaterial}" label="���;�ʴ����" disabled="#{!purchasingGR.grNoPo}" styleClass="input verylong" style="width:70%" required="false">							
							<f:validateLength maximum="80"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="�ӹǹ�����觫���" styleClass="label medium"/>
						<h:inputText id="orderQty" value="#{purchasingGR.editGRItem.orderQty}" label="�ӹǹ�����觫���" disabled="true" styleClass="input medium" style="text-align:right" >							
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="�ӹǹ����Ѻ" styleClass="label medium"/>
						<h:inputText id="receivedQty" value="#{purchasingGR.editGRItem.receivedQty}" label="�ӹǹ����Ѻ" styleClass="input medium" style="text-align:right"  disabled="false">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>				
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="˹���" styleClass="label medium"/>
						<rich:comboBox id="receiveUnit" value="#{purchasingGR.editGRItem.receiveUnit}" label="˹���" suggestionValues="#{purchasingGR.orderUnitList}"  styleClass="input medium" style="float:left" disabled="#{!purchasingGR.grNoPo}" >
							<f:validateLength maximum="25"/>
						</rich:comboBox>
						<div id="wrapper"></div>					
						<h:outputText value="�Ҥҵ��˹���" styleClass="label medium"/>
						<h:inputText id="orderUnitPrice" value="#{purchasingGR.editGRItem.unitPrice}" label="�Ҥҵ��˹���" styleClass="input medium" style="text-align:right"  disabled="false">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>						
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="��ǹŴ" styleClass="label medium"/>
						<h:inputText id="discountAmount" value="#{purchasingGR.editGRItem.discountAmount}" label="��ǹŴ" styleClass="input medium" style="text-align:right" required="false" disabled="#{!purchasingGR.itemDiscountable}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>
						<h:outputText value="ʶҹ���Ѵ��" styleClass="label medium" />
						<h:selectOneMenu id="storageLocation" label="ʶҹ���Ѵ��" value="#{purchasingGR.storageLocation}" disabled="true" styleClass="input medium" style="width:70%">
							<f:selectItems value="#{purchasingGR.storageLocationSelectItemList}"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>					
						<h:outputText value="������ҳ" styleClass="label medium" />
						<h:outputText value="#{purchasingGR.editGRItem.budgetItem.category}" styleClass="input verylong" style="width:50%">
						</h:outputText>
						</div>
						<div id="wrapper"></div>
					<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
					<a4j:commandButton action="#{purchasingGR.newGRItem}" value="������¡������" reRender="itemPanel" disabled ="#{!purchasingGR.grItemNewable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.saveGRItem}" value="�ѹ�֡��¡��" reRender="itemPanel" disabled ="#{!purchasingGR.grItemEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.deleteGRItem}" value="ź��¡��" reRender="itemPanel" disabled ="#{!purchasingGR.grItemDeletable }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					</rich:panel>
					</a4j:region>
					</rich:panel>
					<rich:modalPanel id="modalPanel" height="700" width="800" resizeable="true" >
					<f:facet name="header">					
						<h:outputText value="��¡����Ѻ��ʴ�" />					
					</f:facet>
					<f:facet name="controls">
						<h:panelGroup>
							<h:graphicImage value="/assets/icons/close.png" id="hideTable" style="cursor:pointer" />
							<rich:componentControl for="modalPanel" attachTo="hideTable" operation="hide" event="onclick" />
						</h:panelGroup>
					</f:facet>
					<a4j:region renderRegionOnly="false">
				<rich:panel id="listPanel" style="width:100%">
				<h:outputText value="GR-FULL : ��Ѻ��ʴع���繡���Ѻ��ʴ�Ẻ����ӹǹ    /    GR-PAR : ��Ѻ��ʴع���繡���Ѻ��ʴ�Ẻ�ҧ��ǹ  " />
				<div id="wrapper"></div>
				<h:outputText value="DELETED : ��Ѻ��ʴع��١¡��ԡ    /    CLOSED : ��Ѻ��ʴض١�Դ����������ö�ӡ��¡��ԡ " />
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:dataTable
					id="tableGRList"
					value="#{purchasingGR.grList}" 
					var="gr"  
					rows="20" 
					rowKeyVar="rowNo"
					style="width:100%"	
					rowClasses="odd-row, even-row"			
					>
					<a4j:support event="onRowClick" action="#{purchasingGR.grTableRowClicked}" reRender="controlPanel, headerPanel, itemPanel" status="showload">
						<f:setPropertyActionListener value="#{gr}" target="#{purchasingGR.editGR}" />
					</a4j:support>
					<rich:componentControl for="modalPanel" operation="hide" event="onclick"/>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="�Ţ���"/></f:facet>
						<h:outputText value="#{gr.grNumber}">
							<f:convertNumber  pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="�ѹ����Ѻ��ʴ�"/></f:facet>
						<h:outputText value="#{gr.receivedDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="�ѹ���ѹ�֡"/></f:facet>
						<h:outputText value="#{gr.postingDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="75px">
						<f:facet name="header"><h:outputText value="�Ţ���㺡ӡѺ"/></f:facet>
						<h:outputText value="#{gr.invoiceNumber}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" >
						<f:facet name="header"><h:outputText value="�˵ؼ�"/></f:facet>
						<h:outputText value="#{gr.reason}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:center" width="75px">
						<f:facet name="header"><h:outputText value="�Ţ������觫���"/></f:facet>
						<h:outputText value="#{gr.purchaseOrder.poNumber}">
							<f:convertNumber pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="50px">
						<f:facet name="header"><h:outputText value="ʶҹ�"/></f:facet>
						<h:outputText value="#{gr.status}">
						</h:outputText>
					</rich:column>
					<%--<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>--%>
				</rich:dataTable>
				<rich:datascroller  renderIfSinglePage="false" align="center" for="tableGRList" />
				</rich:panel>
				</a4j:region>
				</rich:modalPanel>
		<%--<rich:tabPanel style="width:100%" switchType="ajax">
			<rich:tab label="��¡����Ѻ��ʴ�" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
				<rich:panel id="listPanel" style="width:100%">
				<h:outputText value="GR-FULL : ��Ѻ��ʴع���繡���Ѻ��ʴ�Ẻ����ӹǹ    /    GR-PAR : ��Ѻ��ʴع���繡���Ѻ��ʴ�Ẻ�ҧ��ǹ  " />
				<div id="wrapper"></div>
				<h:outputText value="CANCELLED : ��Ѻ��ʴع��١¡��ԡ    /    CLOSED : ��Ѻ��ʴض١�Դ����������ö�ӡ��¡��ԡ " />
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:dataTable
					id="tableGRList"
					value="#{purchasingGR.grList}" 
					var="gr"  
					rows="20" 
					rowKeyVar="rowNo"
					onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
					onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
					style="width:100%"				
					>
					<a4j:support event="onRowClick" action="#{purchasingGR.grTableRowClicked}" reRender="controlPanel, headerPanel, itemPanel">
						<f:setPropertyActionListener value="#{gr}" target="#{purchasingGR.editGR}" />
					</a4j:support>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="�Ţ���"/></f:facet>
						<h:outputText value="#{gr.grNumber}">
							<f:convertNumber  pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="�ѹ����Ѻ��ʴ�"/></f:facet>
						<h:outputText value="#{gr.receivedDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="�ѹ���ѹ�֡"/></f:facet>
						<h:outputText value="#{gr.postingDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="75px">
						<f:facet name="header"><h:outputText value="�Ţ���㺡ӡѺ"/></f:facet>
						<h:outputText value="#{gr.invoiceNumber}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" >
						<f:facet name="header"><h:outputText value="�˵ؼ�"/></f:facet>
						<h:outputText value="#{gr.reason}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:center" width="75px">
						<f:facet name="header"><h:outputText value="�Ţ������觫���"/></f:facet>
						<h:outputText value="#{gr.purchaseOrder.poNumber}">
							<f:convertNumber pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="50px">
						<f:facet name="header"><h:outputText value="ʶҹ�"/></f:facet>
						<h:outputText value="#{gr.status}">
						</h:outputText>
					</rich:column>
					<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>
				</rich:dataTable>
				</rich:panel>
				</a4j:region>
			</rich:tab>
			<rich:tab label="�����Ѻ��ʴ�" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
				<rich:panel id="controlPanel" style="width:100%">
					<h:outputText value="����������Ѻ" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingGR.selectedGRType}" style="float:left; margin-right:5px; width:150px" >
						<f:selectItems value="#{purchasingGR.grTypeSelectItemList}"/>
						<a4j:support action="#{purchasingGR.grTypeComboBoxSelected}" event="onchange" reRender="controlPanel, headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<h:outputText value="�����Ţ㺨Ѵ����" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingGR.selectedPoID}" style="float:left; margin-right:5px; width:100px" >
						<f:selectItems value="#{purchasingGR.poSelectItemList}"/>
						<a4j:support action="#{purchasingGR.poComboBoxSelected}" event="onchange" reRender="controlPanel, headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<a4j:commandButton action="#{purchasingGR.newGR}" value="������¡������" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingGR.grCreatable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.saveGR}" value="�ѹ�֡��Ѻ"  reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingGR.grSavable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.deleteGR}" value="¡��ԡ��Ѻ" reRender="controlPanel, headerPanel, itemPanel,listPanel"  disabled ="#{!purchasingGR.grDeletable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.closeGR}" value="�Դ��Ѻ" reRender="controlPanel, headerPanel, itemPanel,listPanel"  disabled ="#{!purchasingGR.grClosable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.printGR}" value="�������Ѻ" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingGR.grPrintable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
				</rich:panel>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="headerPanel" style="width:100%">
					<f:facet name="header"><h:outputText value="Header" /></f:facet>
					<h:outputText value="�Ţ�����Ѻ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.grNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<h:outputText value="�ѹ���ѹ�֡" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.postingDate}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
					<h:outputText value="ʶҹ�" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.status}" style="float:left; margin-right:5px;font-weight:bold">
					</h:outputText>
					<h:outputText value="�ӹǹ�Թ���" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.totalPrice}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<h:outputText value="�Ţ������觫���" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.purchaseOrder.poNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<div id="wrapper"></div>
					<h:outputText value="�ѹ����Ѻ��ʴ�" styleClass="label medium"/>
					<rich:calendar value="#{purchasingGR.editGR.receivedDate}" label="�ѹ����Ѻ��ʴ�" style="float:left"  locale="th" datePattern="dd/MM/yyyy">
					</rich:calendar>
					<div id="wrapper"></div>					
					<h:outputText value="㺡ӡѺ�Թ���/����" styleClass="label medium"/>
					<h:inputText id="invoiceNumber" value="#{purchasingGR.editGR.invoiceNumber}" label="㺡ӡѺ�Թ���/����" styleClass="input medium" required="false">
					</h:inputText>
					<div id="wrapper"></div>					
					<h:outputText value="�˵ؼ���Ф�������" styleClass="label medium"/>
					<h:inputTextarea value="#{purchasingGR.editGR.reason}" label="�˵ؼ���Ф�������" styleClass="input" style="width:70%" rows="3"  >
						<f:validateLength maximum="255"/>
					</h:inputTextarea>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ���Ѻ��ʴ�" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.recipientName}" label="���ͼ���Ѻ��ʴ�" suggestionValues="#{purchasingGR.recipientNameList}"  styleClass="input medium" style="float:left" >
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="���˹觼���Ѻ��ʴ�" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.recipientPos}" label="���˹觼���Ѻ��ʴ�" suggestionValues="#{purchasingGR.recipientPosList}" styleClass="input medium" style="float:left" >
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ��ѹ�֡" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.entryName}" label="���ͼ��ѹ�֡" suggestionValues="#{purchasingGR.entryNameList}"  styleClass="input medium" style="float:left" >
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="���˹觼��ѹ�֡" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.entryPos}" label="���˹觼��ѹ�֡" suggestionValues="#{purchasingGR.entryPosList}" styleClass="input medium" style="float:left" >
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
				</rich:panel>
				</a4j:region>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="itemPanel">
					<f:facet name="header"><h:outputText value="GR Item" /></f:facet>
					<a4j:region renderRegionOnly="false">
					<rich:dataTable
						id="tableGRItemList"
						value="#{purchasingGR.grItemList}" 
						var="grItem"  
						rows="20" 
						rowKeyVar="rowNo"
						onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
						onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
						style="width:100%"				
						>
						<a4j:support event="onRowClick" action="#{purchasingGR.grItemTableRowClicked}" reRender="itemEditPanel">
							<f:setPropertyActionListener value="#{grItem}" target="#{purchasingGR.editGRItem}" />
						</a4j:support>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="������ʴ�"/></f:facet>
							<h:outputText value="#{grItem.material.description}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="���;�ʴ����"/></f:facet>
							<h:outputText value="#{grItem.otherMaterial}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="�ӹǹ����Ѻ"/></f:facet>
							<h:outputText value="#{grItem.receivedQty}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="�ӹǹ�����觫���"/></f:facet>
							<h:outputText value="#{grItem.orderQty}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="˹���"/></f:facet>
							<h:outputText value="#{grItem.receiveUnit}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="�Ҥҵ��˹���"/></f:facet>
							<h:outputText value="#{grItem.unitPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="ʶҹ���Ѵ��"/></f:facet>
							<h:outputText value="#{grItem.storageLocation}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="ʶҹ�"/></f:facet>
							<h:outputText value="#{grItem.status}">
							</h:outputText>
						</rich:column>
						<f:facet name="footer">
							<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
						</f:facet>
					</rich:dataTable>
					</a4j:region>
					<div id="wrapper"></div>
					<rich:spacer height="5px"/>
					<a4j:region renderRegionOnly="false">
					<rich:panel id="itemEditPanel" style="width:100%">
						<h:outputText value="�������ʴ�" styleClass="label medium" />
						<h:selectOneMenu label="�������ʴ�" value="#{purchasingGR.selectedMaterialGroupID}" disabled="#{purchasingGR.editGRItem.id != null}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingGR.materialGroupSelectItemList}"/>
							<a4j:support action="#{purchasingGR.materialGroupComboBoxSelected}" event="onchange" reRender="material, otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="��ʴ�" styleClass="label medium" />
						<h:selectOneMenu id="material" label="��ʴ�" value="#{purchasingGR.selectedMaterialID}" disabled="#{purchasingGR.editGRItem.id != null}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingGR.materialSelectItemList}"/>
							<a4j:support action="#{purchasingGR.materialComboBoxSelected}" event="onchange" reRender="otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="���;�ʴ����" styleClass="label medium"/>
						<h:inputText id="otherMaterial" value="#{purchasingGR.editGRItem.otherMaterial}" label="���;�ʴ����" disabled="#{purchasingGR.editGRItem.id != null}" styleClass="input verylong" style="width:50%" required="false">
							<f:validateLength maximum="80"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="�ӹǹ�����觫���" styleClass="label medium"/>
						<h:inputText id="orderQty" value="#{purchasingGR.editGRItem.orderQty}" label="�ӹǹ�����觫���" disabled="#{purchasingGR.editGRItem.id != null}" styleClass="input medium" style="text-align:right" >
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="�ӹǹ����Ѻ" styleClass="label medium"/>
						<h:inputText id="receivedQty" value="#{purchasingGR.editGRItem.receivedQty}" label="�ӹǹ����Ѻ" styleClass="input medium" style="text-align:right" >
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="˹���" styleClass="label medium"/>
						<rich:comboBox value="#{purchasingGR.editGRItem.receiveUnit}" label="˹���" suggestionValues="#{purchasingGR.orderUnitList}"  styleClass="input medium" style="float:left" disabled="#{purchasingGR.editGRItem != null && purchasingGR.editGRItem.material != null }" >
							<f:validateLength maximum="25"/>
						</rich:comboBox>
						<div id="wrapper"></div>					
						<h:outputText value="�Ҥҵ��˹���" styleClass="label medium"/>
						<h:inputText id="orderUnitPrice" value="#{purchasingGR.editGRItem.unitPrice}" label="�Ҥҵ��˹���" styleClass="input medium" style="text-align:right" >
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>
						<h:outputText value="ʶҹ���Ѵ��" styleClass="label medium" />
						<h:selectOneMenu id="storageLocation" label="ʶҹ���Ѵ��" value="#{purchasingGR.storageLocation}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingGR.storageLocationSelectItemList}"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>					
						<h:outputText value="������ҳ" styleClass="label medium" />
						<h:outputText value="#{purchasingGR.editGRItem.budgetItem.category}" styleClass="input verylong" style="width:50%">
						</h:outputText>
						<div id="wrapper"></div>
					<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
					<a4j:commandButton action="#{purchasingGR.newGRItem}" value="������¡������" reRender="itemPanel" disabled ="#{!purchasingGR.grItemNewable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.saveGRItem}" value="�ѹ�֡��¡��" reRender="itemPanel" disabled ="#{!purchasingGR.grItemEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.deleteGRItem}" value="ź��¡��" reRender="itemPanel" disabled ="#{!purchasingGR.grItemDeletable }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					</rich:panel>
					</a4j:region>
				</rich:panel>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
			</rich:tab>
		</rich:tabPanel>--%>
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
