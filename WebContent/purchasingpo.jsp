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
	<title><h:outputText value="Welfare System : Manage Purchase Order " /></title>
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
		<h:outputText value="�س#{purchasingPO.welcomeMsg}" style="color:blue" rendered="#{purchasingPO.welcomeMsg != null}"/>
		</div>
		</rich:panel>
			<a4j:region renderRegionOnly="false">
				<rich:panel id="controlPanel" style="width:100%">
					<f:facet name="header"><h:outputText value="��駨Ѵ����" /></f:facet>
					<h:outputText value="������" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingPO.selectedPOType}" style="float:left; margin-right:5px; width:180px" >
						<f:selectItems value="#{purchasingPO.poTypeSelectItemList}"/>
						<a4j:support action="#{purchasingPO.poTypeComboBoxSelected}" event="onchange" reRender="itemPanel, controlPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<h:outputText value="�����Ţ��駨Ѵ��" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingPO.selectedPrID}" style="float:left; margin-right:5px; width:120px" disabled="#{purchasingPO.emergencyOrExchange}" >
						<f:selectItems value="#{purchasingPO.prSelectItemList}"/>
						<a4j:support action="#{purchasingPO.prComboBoxSelected}" event="onchange" reRender="headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
					</h:selectOneMenu>
					<h:outputText value="������ҳ" style="float:left; margin-right:5px" />
					<h:selectOneMenu label="������ҳ" value="#{purchasingPO.selectedBudgetItemID}" styleClass="input long" disabled="#{!purchasingPO.emergencyOrExchange}">
					<f:selectItems value="#{purchasingPO.budgetItemSelectItemList}"/>
					<a4j:support action="#{purchasingPO.budgetItemComboBoxSelected}" event="onchange" ajaxSingle="true" reRender="availableamount"/>
					</h:selectOneMenu>		
					<h:panelGroup id="availableamount">
						<h:outputText value="�ӹǹ�Թ�������� " style="margin-right:3px;color:blue" rendered="#{purchasingPO.selectedBudgetItem != null}"/>
						<h:outputText value="#{purchasingPO.selectedBudgetItem.availableAmount}" rendered="#{purchasingPO.selectedBudgetItem != null}" style="color:blue">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</h:panelGroup>								
					<div id="wrapper"></div>
					<rich:panel>		
					<a4j:commandButton action="#{purchasingPO.newPO}" value="������¡������" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingPO.poCreatable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.savePO}" value="�ѹ�֡���觫���"  reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingPO.poEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.deletePO}" value="¡��ԡ���觫���" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingPO.poDeletable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.closePO}" value="�Դ���觫���" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingPO.poClosable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<h:commandLink action="#{purchasingPO.printPOLess}" value="�������§ҹ��͹��ѵԨѴ�Ҿ�ʴ�" immediate="true" target="_blank" rendered="#{purchasingPO.poLessPrintable}"/>
					<rich:spacer  style="margin-left:5px;margin-right:5px"/>
					<h:commandLink action="#{purchasingPO.printPOMore}" value="�������駨Ѵ����" immediate="true" target="_blank" rendered="#{purchasingPO.poMorePrintable}"/>
					<a4j:commandButton  id="link" value="�ʴ���¡����駨Ѵ����" ajaxSingle="true" disabled ="false"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" style="float:right">
						<rich:componentControl for="modalPanel" attachTo="link" operation="show" event="onclick"/>
					</a4j:commandButton>
					</rich:panel>																		
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="headerPanel" style="width:100%">
					<f:facet name="header"><h:outputText value="��ǹ���" /></f:facet>
					<h:outputText value="�Ţ������觫���" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.poNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<h:outputText value="�ѹ������ҧ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.postingDate}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
					<h:outputText value="ʶҹ�" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.status}" style="float:left; margin-right:5px;font-weight:bold">
					</h:outputText>
					<h:outputText value="�ӹǹ�Թ���" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.totalPrice}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<h:outputText value="�Ţ�����駨Ѵ��" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.purchaseRequisition.prNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<div id="wrapper"></div>
					<h:outputText value="������ҹ���" styleClass="label medium"/>
					<h:selectOneMenu value="#{purchasingPO.selectedVendorID}" styleClass="input" style="width:70%">
						<f:selectItems value="#{purchasingPO.vendorSelectItemList}"/>
						<a4j:support action="#{purchasingPO.vendorComboBoxSelected}" event="onchange" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<div id="wrapper"></div>
					<h:outputText value="������ҹ������" styleClass="label medium"/>
					<h:inputText value="#{purchasingPO.editPO.otherVendor}" styleClass="input verylong" style="width:50%" required="false" />
					<div id="wrapper"></div>
					<h:outputText value="�˵ؼ���Ф�������" styleClass="label medium"/>
					<h:inputTextarea value="#{purchasingPO.editPO.reason}" label="�˵ؼ���Ф�������" styleClass="input" style="width:70%" rows="3" required="true" >
						<f:validateLength maximum="255"/>
					</h:inputTextarea>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ��Ѵ����/��ҧ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.buyerName}" label="���ͼ��Ѵ����/��ҧ" suggestionValues="#{purchasingPO.buyerNameList}"  styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<h:outputText value="���ͼ���Ǩ�Ѻ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.receiverName}" label="���ͼ���Ǩ�Ѻ" suggestionValues="#{purchasingPO.receiverNameList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ���駨Ѵ��" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.requisitionerName}" label="���ͼ���駨Ѵ��" suggestionValues="#{purchasingPO.requisitionerNameList}"  styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<h:outputText value="���˹觼���駨Ѵ��" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.requisitionerPos}" label="���˹觼���駨Ѵ��" suggestionValues="#{purchasingPO.requisitionerPosList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ�����ͺ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.inspectorName}" label="���ͼ���Ǩ�ͺ" suggestionValues="#{purchasingPO.inspectorNameList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<h:outputText value="���˹觼���Ǩ�ͺ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.inspectorPos}" label="���˹觼���Ǩ�ͺ" suggestionValues="#{purchasingPO.inspectorPosList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ��͹��ѵ�" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.approverName}" label="���ͼ��͹��ѵ�" suggestionValues="#{purchasingPO.approverNameList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<h:outputText value="���˹�͹��ѵ�" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.approverPos}" label="���˹�͹��ѵ�" suggestionValues="#{purchasingPO.approverPosList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
				</rich:panel>
				</rich:panel>
				</a4j:region>				
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="itemPanel">
					<f:facet name="header"><h:outputText value="��¡����ʴ�" /></f:facet>
					<a4j:region renderRegionOnly="false">
					<rich:dataTable
						id="tablePOItemList"
						value="#{purchasingPO.poItemList}" 
						var="poItem"  
						rows="20" 
						rowKeyVar="rowNo"
						style="width:100%"		
						rowClasses="odd-row, even-row"		
						>
						<a4j:support event="onRowClick" action="#{purchasingPO.poItemTableRowClicked}" reRender="itemEditPanel" status="showload">
							<f:setPropertyActionListener value="#{poItem}" target="#{purchasingPO.editPOItem}" />
						</a4j:support>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="������ʴ�"/></f:facet>
							<h:outputText value="#{poItem.material.description}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="���;�ʴ����"/></f:facet>
							<h:outputText value="#{poItem.otherMaterial}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="�ӹǹ"/></f:facet>
							<h:outputText value="#{poItem.quantity}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="˹���"/></f:facet>
							<h:outputText value="#{poItem.orderUnit}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="�Ҥҵ��˹���"/></f:facet>
							<h:outputText value="#{poItem.unitPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" >
							<f:facet name="header"><h:outputText value="�Ҥ����" /> </f:facet>
							<h:outputText value="#{poItem.totalPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="�ѹ����ͧ�����"/></f:facet>
							<h:outputText value="#{poItem.deliveryDate}">
								<f:convertDateTime pattern="dd/MM/yyyy"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="ʶҹ�"/></f:facet>
							<h:outputText value="#{poItem.status}">
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
						<h:selectOneMenu label="�������ʴ�" value="#{purchasingPO.selectedMaterialGroupID}" styleClass="input medium" style="width:50%" disabled="#{!purchasingPO.emergencyOrExchange}">
							<f:selectItems value="#{purchasingPO.materialGroupSelectItemList}"/>
							<a4j:support action="#{purchasingPO.materialGroupComboBoxSelected}" event="onchange" reRender="material, otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true" status="showload"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="��ʴ�" styleClass="label medium" />
						<h:selectOneMenu id="material" label="��ʴ�" value="#{purchasingPO.selectedMaterialID}" styleClass="input medium" style="width:50%" disabled="#{!purchasingPO.emergencyOrExchange}">
							<f:selectItems value="#{purchasingPO.materialSelectItemList}"/>
							<a4j:support action="#{purchasingPO.materialComboBoxSelected}" event="onchange" reRender="otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="���;�ʴ����" styleClass="label medium"/>
						<h:inputText id="otherMaterial" value="#{purchasingPO.editPOItem.otherMaterial}" label="���;�ʴ����" styleClass="input verylong" style="width:50%" required="false" disabled="#{!purchasingPO.emergencyOrExchange}">
							<f:validateLength maximum="80"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="�ѹ����ͧ�����ҹ" styleClass="label medium"/>
						<rich:calendar value="#{purchasingPO.editPOItem.deliveryDate}" label="�ѹ����ͧ�����ҹ" style="float:left" required="true" locale="th" datePattern="dd/MM/yyyy" disabled="#{!purchasingPO.emergencyOrExchange}">
						</rich:calendar>
						<div id="wrapper"></div>					
						<h:outputText value="�ӹǹ" styleClass="label medium"/>
						<h:inputText id="quantity" value="#{purchasingPO.editPOItem.quantity}" label="�ӹǹ" styleClass="input medium" style="text-align:right" required="true" disabled="#{!purchasingPO.emergencyOrExchange}">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="˹���" styleClass="label medium"/>
						<rich:comboBox id="orderUnit" value="#{purchasingPO.editPOItem.orderUnit}" label="˹���" suggestionValues="#{purchasingPO.orderUnitList}" styleClass="input medium" listStyle="text-align:left" required="true" disabled="#{!purchasingPO.emergencyOrExchange}">
							<f:validateLength maximum="25"/>
						</rich:comboBox>
						<div id="wrapper"></div>					
						<h:outputText value="�Ҥҵ��˹���" styleClass="label medium"/>
						<h:inputText id="orderUnitPrice" value="#{purchasingPO.editPOItem.unitPrice}" label="�Ҥҵ��˹���" styleClass="input medium" style="text-align:right" required="true" disabled="#{!purchasingPO.emergencyOrExchange}">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="������ҳ" styleClass="label medium" />
						<h:selectOneMenu label="������ҳ" value="#{purchasingPO.selectedBudgetItemID}" styleClass="input verylong" style="width:50%" disabled="true">
							<f:selectItems value="#{purchasingPO.budgetItemSelectItemList}"/>
							<a4j:support action="#{purchasingPO.budgetItemComboBoxSelected}" event="onchange" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
					<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
					<a4j:commandButton action="#{purchasingPO.newPOItem}" value="������¡������" reRender="itemPanel" disabled ="#{!purchasingPO.poItemCreatable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.savePOItem}" value="�ѹ�֡��¡��" reRender="itemPanel" disabled ="#{!purchasingPO.poItemEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.deletePOItem}" value="ź��¡��" reRender="itemPanel" disabled ="#{!purchasingPO.poItemDeletable }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					</rich:panel>
					</a4j:region>
				</rich:panel>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:modalPanel id="modalPanel" height="700" width="800" resizeable="true" >
					<f:facet name="header">					
						<h:outputText value="��¡����駨Ѵ����" />					
					</f:facet>
					<f:facet name="controls">
						<h:panelGroup>
							<h:graphicImage value="/assets/icons/close.png" id="hideTable" style="cursor:pointer" />
							<rich:componentControl for="modalPanel" attachTo="hideTable" operation="hide" event="onclick" />
						</h:panelGroup>
					</f:facet>					
					<a4j:region renderRegionOnly="false">
						<rich:panel id="listPanel" style="width:100%">
							<h:outputText value="OPEN : ���觫�������ö�ӡ���Ѻ�ͧ������ӹǹ     /     CLOSED : ���觫��ͷӡ���Ѻ�ͧ��������ӹǹ  " />
							<div id="wrapper"></div>
							<h:outputText value="GR-PAR : ���觫��ͷӡ���Ѻ�ͧ���Ǻҧ��ǹ     /     DELETED : ���觫��Ͷ١ź����������ö�ӧҹ����� " />
							<div id="wrapper"></div>
							<rich:spacer height="5"/>
							<rich:dataTable
								id="tablePRList"
								value="#{purchasingPO.poList}" 
								var="po"  
								rows="20" 
								rowKeyVar="rowNo"
								style="width:100%"	
								rowClasses="odd-row, even-row"			
								>
							<a4j:support event="onRowClick" action="#{purchasingPO.poTableRowClicked}" reRender="controlPanel, headerPanel, itemPanel" status="showload">
								<f:setPropertyActionListener value="#{po}" target="#{purchasingPO.editPO}" />
							</a4j:support>
							<rich:componentControl for="modalPanel" operation="hide" event="onclick"/>			
							<rich:column style="text-align:right"  width="50px">
								<f:facet name="header"><h:outputText value="��·����駨Ѵ����"/></f:facet>
								<h:outputText value="#{po.poNumber}">
									<f:convertNumber  pattern="00000000"/>
								</h:outputText>
							</rich:column>
							<rich:column style="text-align:right"  width="50px">
								<f:facet name="header"><h:outputText value="�ѹ������ҧ"/></f:facet>
								<h:outputText value="#{po.postingDate}">
									<f:convertDateTime pattern="dd/MM/yyyy"/>
								</h:outputText>
							</rich:column>
							<rich:column style="text-align:left">
								<f:facet name="header"><h:outputText value="�˵ؼ�"/></f:facet>
								<h:outputText value="#{po.reason}">
								</h:outputText>
							</rich:column>
							<rich:column style="text-align:right" width="100px">
								<f:facet name="header"><h:outputText value="�Ҥ����"/></f:facet>
								<h:outputText value="#{po.totalPrice}">
									<f:convertNumber pattern="#,##0.00"/>
								</h:outputText>
							</rich:column>
							<rich:column style="text-align:left" width="50px">
								<f:facet name="header"><h:outputText value="ʶҹ�"/></f:facet>
								<h:outputText value="#{po.status}">
								</h:outputText>
						</rich:column>				
				</rich:dataTable>
				 <rich:datascroller  renderIfSinglePage="false" align="center" for="tablePRList" />
					<%--<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>--%>
				</rich:panel>
				</a4j:region>
				</rich:modalPanel>
		<%--<rich:tabPanel style="width:100%" switchType="ajax">
			<rich:tab label="��¡��㺨Ѵ����" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
				<rich:panel id="listPanel" style="width:100%">
				<h:outputText value="OPEN : ���觫�������ö�ӡ���Ѻ�ͧ������ӹǹ     /     CLOSED : ���觫��ͷӡ���Ѻ�ͧ��������ӹǹ  " />
				<div id="wrapper"></div>
				<h:outputText value="GR-PAR : ���觫��ͷӡ���Ѻ�ͧ���Ǻҧ��ǹ     /     DELETED : ���觫��Ͷ١ź����������ö�ӧҹ����� " />
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:dataTable
					id="tablePRList"
					value="#{purchasingPO.poList}" 
					var="po"  
					rows="20" 
					rowKeyVar="rowNo"
					onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
					onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
					style="width:100%"				
					>
					<a4j:support event="onRowClick" action="#{purchasingPO.poTableRowClicked}" reRender="controlPanel, headerPanel, itemPanel">
						<f:setPropertyActionListener value="#{po}" target="#{purchasingPO.editPO}" />
					</a4j:support>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="��·����駨Ѵ����"/></f:facet>
						<h:outputText value="#{po.poNumber}">
							<f:convertNumber  pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="�ѹ������ҧ"/></f:facet>
						<h:outputText value="#{po.postingDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="100px">
						<f:facet name="header"><h:outputText value="�˵ؼ�"/></f:facet>
						<h:outputText value="#{po.reason}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right" >
						<f:facet name="header"><h:outputText value="�Ҥ����"/></f:facet>
						<h:outputText value="#{po.totalPrice}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="50px">
						<f:facet name="header"><h:outputText value="ʶҹ�"/></f:facet>
						<h:outputText value="#{po.status}">
						</h:outputText>
					</rich:column>
					<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>
				</rich:dataTable>
				</rich:panel>
				</a4j:region>
			</rich:tab>
			<rich:tab label="���㺨Ѵ����" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
				<rich:panel id="controlPanel" style="width:100%">
					<h:outputText value="������" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingPO.selectedPOType}" style="float:left; margin-right:5px; width:150px" >
						<f:selectItems value="#{purchasingPO.poTypeSelectItemList}"/>
						<a4j:support action="#{purchasingPO.poTypeComboBoxSelected}" event="onchange" reRender="headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<h:outputText value="�����Ţ��駨Ѵ��" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingPO.selectedPrID}" style="float:left; margin-right:5px; width:100px" >
						<f:selectItems value="#{purchasingPO.prSelectItemList}"/>
						<a4j:support action="#{purchasingPO.prComboBoxSelected}" event="onchange" reRender="headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<a4j:commandButton action="#{purchasingPO.newPO}" value="������¡������" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingPO.poCreatable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.savePO}" value="�ѹ�֡���觫���"  reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingPO.poEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.deletePO}" value="¡��ԡ���觫���" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingPO.poDeletable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.closePO}" value="�Դ���觫���" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingPO.poClosable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.printPO}" value="��������觫���" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingPO.poPrintable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>													
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="headerPanel" style="width:100%">
					<f:facet name="header"><h:outputText value="Header" /></f:facet>
					<h:outputText value="�Ţ������觫���" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.poNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<h:outputText value="�ѹ������ҧ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.postingDate}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
					<h:outputText value="ʶҹ�" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.status}" style="float:left; margin-right:5px;font-weight:bold">
					</h:outputText>
					<h:outputText value="�ӹǹ�Թ���" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.totalPrice}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<h:outputText value="�Ţ�����駨Ѵ��" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPO.editPO.purchaseRequisition.prNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<div id="wrapper"></div>
					<h:outputText value="������ҹ���" styleClass="label medium"/>
					<h:selectOneMenu value="#{purchasingPO.selectedVendorID}" styleClass="input" style="width:70%">
						<f:selectItems value="#{purchasingPO.vendorSelectItemList}"/>
						<a4j:support action="#{purchasingPO.vendorComboBoxSelected}" event="onchange" reRender="headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<div id="wrapper"></div>
					<h:outputText value="�˵ؼ���Ф�������" styleClass="label medium"/>
					<h:inputTextarea value="#{purchasingPO.editPO.reason}" label="�˵ؼ���Ф�������" styleClass="input" style="width:70%" rows="3" required="true" >
						<f:validateLength maximum="255"/>
					</h:inputTextarea>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ��Ѵ����/��ҧ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.buyerName}" label="���ͼ��Ѵ����/��ҧ" suggestionValues="#{purchasingPO.buyerNameList}"  styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="���ͼ���Ǩ�Ѻ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.receiverName}" label="���ͼ���Ǩ�Ѻ" suggestionValues="#{purchasingPO.receiverNameList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ���駨Ѵ��" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.requisitionerName}" label="���ͼ���駨Ѵ��" suggestionValues="#{purchasingPO.requisitionerNameList}"  styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="���˹觼���駨Ѵ��" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.requisitionerPos}" label="���˹觼���駨Ѵ��" suggestionValues="#{purchasingPO.requisitionerPosList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ�����ͺ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.inspectorName}" label="���ͼ���Ǩ�ͺ" suggestionValues="#{purchasingPO.inspectorNameList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="���˹觼���Ǩ�ͺ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.inspectorPos}" label="���˹觼���Ǩ�ͺ" suggestionValues="#{purchasingPO.inspectorPosList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="���ͼ��͹��ѵ�" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.approverName}" label="���ͼ��͹��ѵ�" suggestionValues="#{purchasingPO.approverNameList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="���˹�͹��ѵ�" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPO.editPO.approverPos}" label="���˹�͹��ѵ�" suggestionValues="#{purchasingPO.approverPosList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
				</rich:panel>
				</rich:panel>
				</a4j:region>				
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="itemPanel">
					<f:facet name="header"><h:outputText value="PO Item" /></f:facet>
					<a4j:region renderRegionOnly="false">
					<rich:dataTable
						id="tablePOItemList"
						value="#{purchasingPO.poItemList}" 
						var="poItem"  
						rows="20" 
						rowKeyVar="rowNo"
						onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
						onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
						style="width:100%"				
						>
						<a4j:support event="onRowClick" action="#{purchasingPO.poItemTableRowClicked}" reRender="itemEditPanel">
							<f:setPropertyActionListener value="#{poItem}" target="#{purchasingPO.editPOItem}" />
						</a4j:support>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="������ʴ�"/></f:facet>
							<h:outputText value="#{poItem.material.description}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="���;�ʴ����"/></f:facet>
							<h:outputText value="#{poItem.otherMaterial}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="�ӹǹ"/></f:facet>
							<h:outputText value="#{poItem.quantity}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="˹���"/></f:facet>
							<h:outputText value="#{poItem.orderUnit}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="�Ҥҵ��˹���"/></f:facet>
							<h:outputText value="#{poItem.unitPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="�ѹ����ͧ�����"/></f:facet>
							<h:outputText value="#{poItem.deliveryDate}">
								<f:convertDateTime pattern="dd/MM/yyyy"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="ʶҹ�"/></f:facet>
							<h:outputText value="#{poItem.status}">
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
						<h:selectOneMenu label="�������ʴ�" value="#{purchasingPO.selectedMaterialGroupID}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingPO.materialGroupSelectItemList}"/>
							<a4j:support action="#{purchasingPO.materialGroupComboBoxSelected}" event="onchange" reRender="material, otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="��ʴ�" styleClass="label medium" />
						<h:selectOneMenu id="material" label="��ʴ�" value="#{purchasingPO.selectedMaterialID}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingPO.materialSelectItemList}"/>
							<a4j:support action="#{purchasingPO.materialComboBoxSelected}" event="onchange" reRender="otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="���;�ʴ����" styleClass="label medium"/>
						<h:inputText id="otherMaterial" value="#{purchasingPO.editPOItem.otherMaterial}" label="���;�ʴ����" styleClass="input verylong" style="width:50%" required="false">
							<f:validateLength maximum="80"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="�ѹ����ͧ�����ҹ" styleClass="label medium"/>
						<rich:calendar value="#{purchasingPO.editPOItem.deliveryDate}" label="�ѹ����ͧ�����ҹ" style="float:left" required="true" locale="th" datePattern="dd/MM/yyyy">
						</rich:calendar>
						<div id="wrapper"></div>					
						<h:outputText value="�ӹǹ" styleClass="label medium"/>
						<h:inputText id="quantity" value="#{purchasingPO.editPOItem.quantity}" label="�ӹǹ" styleClass="input medium" style="text-align:right" required="true">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="˹���" styleClass="label medium"/>
						<rich:comboBox id="orderUnit" value="#{purchasingPO.editPOItem.orderUnit}" label="˹���" suggestionValues="#{purchasingPO.orderUnitList}" styleClass="input medium" listStyle="text-align:left" required="true">
							<f:validateLength maximum="25"/>
						</rich:comboBox>
						<div id="wrapper"></div>					
						<h:outputText value="�Ҥҵ��˹���" styleClass="label medium"/>
						<h:inputText id="orderUnitPrice" value="#{purchasingPO.editPOItem.unitPrice}" label="�Ҥҵ��˹���" styleClass="input medium" style="text-align:right" required="true">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="������ҳ" styleClass="label medium" />
						<h:selectOneMenu label="������ҳ" value="#{purchasingPO.selectedBudgetItemID}" styleClass="input verylong" style="width:50%">
							<f:selectItems value="#{purchasingPO.budgetItemSelectItemList}"/>
							<a4j:support action="#{purchasingPO.budgetItemComboBoxSelected}" event="onchange" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
					<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
					<a4j:commandButton action="#{purchasingPO.newPoItem}" value="������¡������" reRender="itemPanel" disabled ="#{!purchasingPO.poItemEditable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.savePoItem}" value="�ѹ�֡��¡��" reRender="itemPanel" disabled ="#{!purchasingPO.poItemEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPO.deletePoItem}" value="ź��¡��" reRender="itemPanel" disabled ="#{!purchasingPO.poItemDeletable }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
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
