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
	<title><h:outputText value="Welfare System : Home" /></title>
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
		<h:outputText value="�س#{budgetTransferRequest.welcomeMsg}" style="color:blue" rendered="#{budgetTransferRequest.welcomeMsg != null}"/>
		</div>
		</rich:panel>
		<rich:tabPanel style="width:100%" switchType="ajax">
		<a4j:support event="ontabchange" status="showload"/>
		<rich:tab label="���ҧ��͹������ҳ" ajaxSingle="true" >
		<a4j:region renderRegionOnly="false">
			<rich:panel id="editPanel" >
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="���͹������ҳ" />
					</h:panelGroup>
				</f:facet>
				<h:outputText value="�է�����ҳ" styleClass="label long"/>
				<h:outputText value="#{budgetTransferRequest.editBudget.budgetYear}" styleClass="data number" />
				<div id="wrapper"></div>
				<h:outputText value="�Ţ���㺢��͹" styleClass="label long"/>
				<h:outputText value="#{budgetTransferRequest.editBudgetTransfer.transferNumber}" styleClass="data number" >
					<f:convertNumber pattern="0000"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value="���͹������ҳ�ҡ��Ǵ" styleClass="label long"/>
				<h:outputText id="fromBudgetItem" value="#{budgetTransferRequest.editFromBudgetItem.category}" styleClass="data" style="width:60%"/>
				<div id="wrapper"></div>
				<h:outputText value="�͹������ҳ��ѧ��Ǵ" styleClass="label long"/>
				<h:outputText id="toBudgetItem" value="#{budgetTransferRequest.editToBudgetItem.category}" styleClass="data" style="width:60%"/>
				<div id="wrapper"></div>
				<h:outputText value="�ѹ������¡�â��͹" styleClass="label long"/>
				<h:outputText value="#{budgetTransferRequest.editBudgetTransfer.requestDate}" styleClass="data long">
					<f:convertDateTime pattern="dd/MM/yyyy"/>
				</h:outputText>
				<div id="wrapper"></div>			
				<h:outputText value="�˵ؼŷ����͹" styleClass="label long"/>
				<h:inputText id="editreason" label="�˵ؼŷ����͹" value="#{budgetTransferRequest.editBudgetTransfer.reason}" styleClass="input medium" style="width:60%" required="true" >
					<f:validateLength maximum="100"/>
				</h:inputText>
				<h:message for="editreason" />
				<div id="wrapper"></div>
				<h:outputText value="�Ţ����͡�����ҧ�ԧ" styleClass="label long"/>
				<h:inputText label="�Ţ����͡�����ҧ�ԧ" value="#{budgetTransferRequest.editBudgetTransfer.externalTransferNumber}" styleClass="input medium" >
					<f:validateLength maximum="10"/>
				</h:inputText>
				<div id="wrapper"></div>
				<h:outputText value="�ӹǹ�Թ" styleClass="label long"/>
				<h:inputText label="�ӹǹ�Թ" value="#{budgetTransferRequest.editBudgetTransfer.requestAmount}" styleClass="input medium" style="text-align:right" required="true" >
					<f:convertNumber pattern="#,##0.00"/>
					<f:validateDoubleRange minimum="0"/>
				</h:inputText>				
				<div id="wrapper"></div>
				<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
				<a4j:commandButton action="#{budgetTransferRequest.newTransfer}" value="����㺢��͹" reRender="editPanel,listToSubBudgetItemPanel,listFromSubBudgetItemPanel" disabled ="#{!budgetTransferRequest.creatable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
				<a4j:commandButton action="#{budgetTransferRequest.saveTransfer}" value="�ѹ�֡㺢��͹" reRender="editPanel,listToSubBudgetItemPanel,listFromSubBudgetItemPanel" disabled ="#{!budgetTransferRequest.editable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
				<a4j:commandButton action="#{budgetTransferRequest.printTransfer}" value="�����㺢��͹" reRender="editPanel,listToSubBudgetItemPanel,listFromSubBudgetItemPanel" disabled ="#{!budgetTransferRequest.printable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
			</rich:panel>
		</a4j:region>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="fromBudgetPanel" style="width:48%; float:left">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="�͹������ҳ�ҡ" />
				</h:panelGroup>
			</f:facet>
			<rich:tree
				id="fromBudgetItemTree"
				value="#{budgetTransferRequest.fromBudgetItemTree}"
				var="fromBudgetItem" 
				nodeSelectListener="#{budgetTransferRequest.fromBudgetItemTreeNodeSelection}"
				ajaxSubmitSelection="true"
				switchType="client"
				reRender="fromBudgetPanel, toBudgetPanel, fromBudgetItem, toBudgetItem"
				status="showload"
			>
			</rich:tree>
			<rich:panel id="listFromSubBudgetItemPanel">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="��¡�ç�����ҳ�ʴ������Ǵ #{budgetTransferRequest.editFromBudgetItem.category}" />
					</h:panelGroup>
				</f:facet>
				<h:outputText value=" �է�����ҳ " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editFromBudgetItem.budget.budgetYear}" styleClass="data"/>
				<div id="wrapper"></div>
				<h:outputText value=" ��Ǵ������ҳ " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editFromBudgetItem.category}" styleClass="data" style="width:60%"/>
				<div id="wrapper"></div>
				<h:outputText value=" ǧ�Թ��� " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editFromBudgetItem.initialAmount}" styleClass="data number" >
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value=" �ѹ�� " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editFromBudgetItem.reservedAmount}" styleClass="data number">
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value=" �駺 "  styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editFromBudgetItem.expensedAmount}" styleClass="data number">
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value=" ������� " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editFromBudgetItem.availableAmount}" styleClass="data number">
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:dataTable
					id="tableFromSubBudgetItemList"
					value="#{budgetTransferRequest.fromBudgetItemSubBudgetItemList}" 
					var="budgetItem"  
					rows="5" 
					rowKeyVar="rowNo"
					style="width:100%"	
					rowClasses="odd-row, even-row"			
					>
					<rich:column style="text-align:right"  width="20px">
						<f:facet name="header"><h:outputText value=""/></f:facet>
						<h:outputText value="#{rowNo+1}"/>
					</rich:column>
					<rich:column style="text-align:left" >
						<f:facet name="header"><h:outputText value="��Ǵ������ҳ"/></f:facet>
						<h:outputText value="#{budgetItem.category}"/>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="ǧ�Թ�����"/></f:facet>
						<h:outputText value="#{budgetItem.initialAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="�ѹ��"/></f:facet>
						<h:outputText value="#{budgetItem.reservedAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="�駺"/></f:facet>
						<h:outputText value="#{budgetItem.expensedAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="�������"/></f:facet>
						<h:outputText value="#{budgetItem.availableAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:center"  width="16px">
						<h:graphicImage value="/assets/icons/green-button.png" rendered="#{budgetItem.editable && budgetTransferRequest.editBudget.editable}" width="16" height="16"/>
						<h:graphicImage value="/assets/icons/red-button.png" rendered="#{!budgetItem.editable && budgetTransferRequest.editBudget.editable}" width="16" height="16"/>
					</rich:column>
					<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>
				</rich:dataTable>
			</rich:panel>
		</rich:panel>
		</a4j:region>
		<rich:spacer width="10"/>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="toBudgetPanel" style="width:48%; float:left">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="�͹������ҳ��ѧ" />
				</h:panelGroup>
			</f:facet>
			<rich:tree
				id="toBudgetItemTree"
				value="#{budgetTransferRequest.toBudgetItemTree}"
				var="toBudgetItem" 
				nodeSelectListener="#{budgetTransferRequest.toBudgetItemTreeNodeSelection}"
				ajaxSubmitSelection="true"
				switchType="client"
				reRender="toBudgetPanel, toBudgetPanel, fromBudgetItem, toBudgetItem"
				status="showload"
			>
			</rich:tree>
			<rich:panel id="listToSubBudgetItemPanel">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="��¡�ç�����ҳ�ʴ������Ǵ #{budgetTransferRequest.editToBudgetItem.category}" />
					</h:panelGroup>
				</f:facet>
				<h:outputText value=" �է�����ҳ " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editToBudgetItem.budget.budgetYear}" styleClass="data"/>
				<div id="wrapper"></div>
				<h:outputText value=" ��Ǵ������ҳ " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editToBudgetItem.category}" styleClass="data" style="width:60%"/>
				<div id="wrapper"></div>
				<h:outputText value=" ǧ�Թ��� " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editToBudgetItem.initialAmount}" styleClass="data number" >
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value=" �ѹ�� " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editToBudgetItem.reservedAmount}" styleClass="data number">
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value=" �駺 "  styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editToBudgetItem.expensedAmount}" styleClass="data number">
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value=" ������� " styleClass="label medium"/>
				<h:outputText value="#{budgetTransferRequest.editToBudgetItem.availableAmount}" styleClass="data number">
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:dataTable
					id="tableToSubBudgetItemList"
					value="#{budgetTransferRequest.toBudgetItemSubBudgetItemList}" 
					var="budgetItem"  
					rows="5" 
					rowKeyVar="rowNo"
					style="width:100%"	
					rowClasses="odd-row, even-row"			
					>
					<rich:column style="text-align:right"  width="20px">
						<f:facet name="header"><h:outputText value=""/></f:facet>
						<h:outputText value="#{rowNo+1}"/>
					</rich:column>
					<rich:column style="text-align:left" >
						<f:facet name="header"><h:outputText value="��Ǵ������ҳ"/></f:facet>
						<h:outputText value="#{budgetItem.category}"/>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="ǧ�Թ�����"/></f:facet>
						<h:outputText value="#{budgetItem.initialAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="�ѹ��"/></f:facet>
						<h:outputText value="#{budgetItem.reservedAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="�駺"/></f:facet>
						<h:outputText value="#{budgetItem.expensedAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="�������"/></f:facet>
						<h:outputText value="#{budgetItem.availableAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:center"  width="16px">
						<h:graphicImage value="/assets/icons/green-button.png" rendered="#{budgetItem.editable && budgetTransferRequest.editBudget.editable}" width="16" height="16"/>
						<h:graphicImage value="/assets/icons/red-button.png" rendered="#{!budgetItem.editable && budgetTransferRequest.editBudget.editable}" width="16" height="16"/>
					</rich:column>
					<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>
				</rich:dataTable>
			</rich:panel>
		</rich:panel>
		</a4j:region>
		</rich:tab>
		<rich:tab label="��¡����͹������ҳ" ajaxSingle="true" >
			<h:outputText value="�է�����ҳ" styleClass="label long"/>
			<h:outputText value="#{budgetTransferApprove.listBudgetYear}" styleClass="data number" />
			<div id="wrapper"></div>
			<rich:spacer height="5"/>
			<div style="float:left; width:950px; max-width:950px; overflow:scroll;">
			<rich:dataTable
				id="tableBudgetTransferList"
				value="#{budgetTransferRequest.budgetTransferList}" 
				var="budgetTransfer"  
				rows="20" 
				rowKeyVar="rowNo"
				style="width:100%"		
				rowClasses="odd-row, even-row"		
				>
				<a4j:support event="onRowClick" action="#{budgetTransferRequest.transferRequestTableRowClicked}" reRender="editPanel,listFromSubBudgetItemPanel,listToSubBudgetItemPanel" status="showload">
					<f:setPropertyActionListener value="#{budgetTransfer}" target="#{budgetTransferRequest.editBudgetTransfer}" />
				</a4j:support>
				<rich:column style="text-align:right"  width="50px" sortBy="#{budgetTransfer.transferNumber}">
					<f:facet name="header"><h:outputText value="�Ţ���"/></f:facet>
					<h:outputText value="#{budgetTransfer.transferNumber}" >
						<f:convertNumber pattern="0000"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left"  width="75px">
					<f:facet name="header"><h:outputText value="�ѹ�����͹"/></f:facet>
					<h:outputText value="#{budgetTransfer.requestDate}">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:right"  width="75px">
					<f:facet name="header"><h:outputText value="�ӹǹ�Թ���͹"/></f:facet>
					<h:outputText value="#{budgetTransfer.requestAmount}">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left"  width="75px">
					<f:facet name="header"><h:outputText value="���͹�ҡ��Ǵ"/></f:facet>
					<h:outputText value="#{budgetTransfer.oldFromBudgetItem.category}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left"  width="75px">
					<f:facet name="header"><h:outputText value="���͹��ѧ��Ǵ"/></f:facet>
					<h:outputText value="#{budgetTransfer.oldToBudgetItem.category}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left"  width="75px">
					<f:facet name="header"><h:outputText value="�ѹ���͹��ѵ�"/></f:facet>
					<h:outputText value="#{budgetTransfer.approveDate}">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:right"  width="75px">
					<f:facet name="header"><h:outputText value="�ӹǹ�Թ͹��ѵ�"/></f:facet>
					<h:outputText value="#{budgetTransfer.approveAmount}">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left"  width="75px">
					<f:facet name="header"><h:outputText value="����¹�繢��͹�ҡ��Ǵ"/></f:facet>
					<h:outputText value="#{budgetTransfer.changeFromBudgetItem ? budgetTransfer.fromBudgetItem.category : ''} ">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left"  width="75px">
					<f:facet name="header"><h:outputText value="����¹�繢��͹��ѧ��Ǵ"/></f:facet>
					<h:outputText value="#{budgetTransfer.changeToBudgetItem ? budgetTransfer.toBudgetItem.category : '' }">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left">
					<f:facet name="header"><h:outputText value="�˵ؼŷ����͹"/></f:facet>
					<h:outputText value="#{budgetTransfer.reason}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:center"  width="75px" sortBy="#{budgetTransfer.status}">
					<f:facet name="header"><h:outputText value="ʶҹ�"/></f:facet>
					<h:outputText value="#{budgetTransfer.status}">
					</h:outputText>
				</rich:column>
				<f:facet name="footer">
					<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
				</f:facet>
			</rich:dataTable>
			</div>
		</rich:tab>
		</rich:tabPanel>
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
