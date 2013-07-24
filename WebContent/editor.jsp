<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ page language="java" contentType="text/html; charset=TIS-620"
	pageEncoding="TIS-620"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<f:view>
	<head>
<title><h:outputText value="Welfare Purchasing System" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=TIS-620" />
<link rel="stylesheet" type="text/css"
	href="assets/css/templatestyle.css" />
<link rel="stylesheet" type="text/css" href="assets/css/facestyle.css" />
	</head>
	<body>
		<div id="page">
			<jsp:include page="header.jsp" />
			<jsp:include page="menu.jsp" />
			<div id="contentarea">
				<h:form>
					<rich:dataTable value="#{editor.budgetItemList}" var="budget"
						id="list" rows="20" rowClasses="odd-row, even-row" >
						<a4j:support event="onRowClick" reRender="editPanel">
							<f:setPropertyActionListener value="#{budget}"
								target="#{editor.editBudgetItem}" />
						</a4j:support>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="CAT" />
							</f:facet>
							<h:outputText value="#{budget.category}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="CODE" />
							</f:facet>
							<h:outputText value="#{budget.accountCode}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="INIT" />
							</f:facet>
							<h:outputText value="#{budget.initialAmount}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="RESERVE" />
							</f:facet>
							<h:outputText value="#{budget.reservedAmount}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="EXP" />
							</f:facet>
							<h:outputText value="#{budget.expensedAmount}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="TRANIN" />
							</f:facet>
							<h:outputText value="#{budget.transferInAmount}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="TRANOUT" />
							</f:facet>
							<h:outputText value="#{budget.transferOutAmount}" />
						</rich:column>
						<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"/>
					</f:facet>
					</rich:dataTable>
					<rich:panel id="editPanel">
						<h:panelGrid columns="2">
							<h:outputText value="INIT:" />
							<h:inputText value="#{editor.editBudgetItem.initialAmount}" />
							<h:outputText value="RESERVE:" />
							<h:inputText value="#{editor.editBudgetItem.reservedAmount}" />
							<h:outputText value="EXP:" />
							<h:inputText value="#{editor.editBudgetItem.expensedAmount}" />
							<h:outputText value="TRANIN:" />
							<h:inputText value="#{editor.editBudgetItem.transferInAmount}" />
							<h:outputText value="TRANOUT:" />
							<h:inputText value="#{editor.editBudgetItem.transferOutAmount}" />
						</h:panelGrid>
						<a4j:commandButton value="Save" action="#{editor.saveBudgetItem}"
							reRender="list, editPanel" />
						<a4j:commandButton value="Clear" action="#{editor.clear}"
							reRender="editPanel" />
						<a4j:commandButton value="List"
							action="#{editor.createBudgetItem}" reRender="list" />
					</rich:panel>
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
