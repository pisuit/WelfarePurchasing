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
		<h:outputText value="ยินดีต้อนรับ : "/>
		<h:outputText value="คุณ#{budgetExpense.welcomeMsg}" style="color:blue" rendered="#{budgetExpense.welcomeMsg != null}" />
		</div>
		</rich:panel>
	<div style="float:left;width:31%">	
		<a4j:region renderRegionOnly="false">
		<rich:panel id="listPanel" style="width:100%">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="รายการข้อมูลงบประมาณสวัสดิการ" />
				</h:panelGroup>
			</f:facet>
			<rich:tree
				id="budgetItemTree"
				value="#{budgetExpense.budgetItemTree}"
				var="budgetItem" 
				nodeSelectListener="#{budgetExpense.budgetItemTreeNodeSelection}"
				ajaxSubmitSelection="true"
				reRender="listPanel, listSubCategoryPanel, editPanel "
				style="width:100%; overflow:scroll; "
				oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
				status="showload"
			>
			</rich:tree>
		</rich:panel>	
		</a4j:region>
		<rich:spacer height="5px"/>
		<a4j:region renderRegionOnly="false">
			<rich:panel id="controlPanel" style="width:100%">
				<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="ตัวควบคุมการแสดงผล" />
				</h:panelGroup>
			</f:facet>
			<h:selectOneRadio id="radio" value="#{budgetExpense.selectedView}" layout="pageDirection" style="margin-bottom:10px" >
			<a4j:support event="onclick" reRender="controlPanel" status="showload">
			</a4j:support>
				<f:selectItem itemLabel="แสดงรายการของวันนี้" itemValue="today" />
				<f:selectItem itemLabel="แสดงรายการโดยระบุวันที่" itemValue="selectday"/>
			</h:selectOneRadio>
			<div id="wrapper"></div>
			<h:outputText value="จากวันที่" styleClass="label small"/>
			<rich:calendar value="#{budgetExpense.fromDate}" label="จากวันที่" style="float:left" required="false" locale="th" datePattern="dd/MM/yyyy" styleClass="data" disabled="#{budgetExpense.dateRendered}">
			<a4j:support event="oncollapse" oncomplete="document.forms[0].submit();" status="showload"/>
			</rich:calendar>
			<div id="wrapper"></div>
			<h:outputText value="ถึงวันที่" styleClass="label small"/>
			<rich:calendar value="#{budgetExpense.toDate}" label="ถึงวันที่" style="float:left" required="false" locale="th" datePattern="dd/MM/yyyy" styleClass="data" disabled="#{budgetExpense.dateRendered}">
			<a4j:support event="oncollapse" oncomplete="document.forms[0].submit();" status="showload"/>
			</rich:calendar>
			</rich:panel>
		</a4j:region>
	</div>			
		<div style="float:left;width:68%;margin-left:5px;">		
		<a4j:region renderRegionOnly="false">
			<rich:panel id="listSubCategoryPanel">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="รายการงบประมาณแสดงตามหมวด #{budgetExpense.editBudgetItem.category}" />
					</h:panelGroup>
				</f:facet>
				<h:outputText value=" ปีงบประมาณ " styleClass="label medium"/>
				<h:outputText value="#{budgetExpense.editBudgetItem.budget.budgetYear}" styleClass="data"/>
				<div id="wrapper"></div>
				<h:outputText value=" หมวดงบประมาณ " styleClass="label medium"/>
				<h:outputText value="#{budgetExpense.editBudgetItem.category}" styleClass="data" style="width:75%"/>
				<div id="wrapper"></div>
				<h:outputText value=" วงเงินรวม " styleClass="label medium"/>
				<h:outputText value="#{budgetExpense.editBudgetItem.initialAmount}" styleClass="data number" >
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value=" กันงบ " styleClass="label medium"/>
				<h:outputText value="#{budgetExpense.editBudgetItem.reservedAmount}" styleClass="data number">
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value=" ใช้งบ "  styleClass="label medium"/>
				<h:outputText value="#{budgetExpense.editBudgetItem.expensedAmount}" styleClass="data number">
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value=" คงเหลือ " styleClass="label medium"/>
				<h:outputText value="#{budgetExpense.editBudgetItem.availableAmount}" styleClass="data number">
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:dataTable
				id="tableExpenseList"
				value="#{budgetExpense.expenseList}"
				var="expense"
				rows="10"
				rowKeyVar="rowNo"
				style="width:100%"
				rowClasses="odd-row, even-row"
				>
				<a4j:support event="onRowClick" action="#{budgetExpense.budgetExpenseTableRowClicked}" reRender="editPanel" status="showload">
					<f:setPropertyActionListener value="#{expense}" target="#{budgetExpense.editBudgetExpense}" />
				</a4j:support>
				<rich:column width="20%">
					<f:facet name="header"><h:outputText value="วันที่" /> </f:facet>
					<h:outputText value="#{expense.postingDate}">
						<f:convertDateTime pattern="dd-MM-yyyy"/>
					</h:outputText>
				</rich:column>
				<rich:column width="60๔">
					<f:facet name="header"><h:outputText value="รายละเอียด" /> </f:facet>
					<h:outputText value="#{expense.detail}" />
				</rich:column>
				<rich:column width="20%" style="text-align:right">
					<f:facet name="header"><h:outputText value="จำนวนเงิน" /> </f:facet>
					<h:outputText value="#{expense.amount}" />
				</rich:column>
				<f:facet name="footer"><rich:datascroller renderIfSinglePage="false"></rich:datascroller></f:facet>
				</rich:dataTable>
				<%--<rich:dataTable
					id="tableSubBudgetItemList"
					value="#{budgetExpense.subBudgetItemList}" 
					var="budgetItem"  
					rows="5" 
					rowKeyVar="rowNo"
					onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
					onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
					style="width:100%"				
					>
					<rich:column style="text-align:right"  width="20px">
						<f:facet name="header"><h:outputText value=""/></f:facet>
						<h:outputText value="#{rowNo+1}"/>
					</rich:column>
					<rich:column style="text-align:left" >
						<f:facet name="header"><h:outputText value="หมวดงบประมาณ"/></f:facet>
						<h:outputText value="#{budgetItem.category}"/>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="วงเงินที่ตั้ง"/></f:facet>
						<h:outputText value="#{budgetItem.initialAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="กันงบ"/></f:facet>
						<h:outputText value="#{budgetItem.reservedAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="ใช้งบ"/></f:facet>
						<h:outputText value="#{budgetItem.expensedAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="75px">
						<f:facet name="header"><h:outputText value="คงเหลือ"/></f:facet>
						<h:outputText value="#{budgetItem.availableAmount}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>
				</rich:dataTable>--%>
			</rich:panel>
		</a4j:region>
		<div id="wrapper"></div>
		<rich:spacer height="5px"/>
		<a4j:region renderRegionOnly="false">
			<rich:panel id="editPanel" >
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="บันทึกค่าใข้จ่ายประจำปี #{budgetExpense.editBudget.budgetYear}" />
					</h:panelGroup>
				</f:facet>
			<h:outputText value="หมวดงบประมาณ" styleClass="label medium" />
			<h:outputText value="#{budgetExpense.editBudgetItem.category}" styleClass="data" style="width:75%" />
			<div id="wrapper"></div>
			<h:outputText value="ร้านค้า" styleClass="label medium" />
			<h:selectOneMenu label="ร้านค้า" value="#{budgetExpense.selectedVendorID}" styleClass="input verylong" style="width:75%">
				<f:selectItem itemValue="-1" itemLabel="เลือกร้านค้า"/>
				<f:selectItems value="#{budgetExpense.vendorSelectItemList}"/>
				<a4j:support action="#{budgetExpense.vendorComboboxSelected}" event="onchange"/>
			</h:selectOneMenu>
			<div id="wrapper"></div>
			<h:outputText value="จำนวนเงิน" styleClass="label medium"/>
			<h:inputText label="จำนวนเงิน" value="#{budgetExpense.editBudgetExpense.amount}" styleClass="input medium" style="text-align:right" required="true" >
				<f:convertNumber pattern="#,##0.00"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="เลขที่ใบเสร็จ" styleClass="label medium"/>
			<h:inputText label="เลขที่ใบเสร็จ" value="#{budgetExpense.editBudgetExpense.invoiceNumber}" styleClass="input medium" >
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="รายละเอียด" styleClass="label medium"/>
			<h:inputTextarea label="รายละเอียด" value="#{budgetExpense.editBudgetExpense.detail}" styleClass="input medium" style="width:75%">
			</h:inputTextarea>
			<div id="wrapper"></div>
			<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
			<a4j:commandButton action="#{budgetExpense.newExpense}" value="เพิ่ม" reRender="listPanel, listSubCategoryPanel, editPanel" disabled ="#{!budgetExpense.newable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{budgetExpense.saveExpense}" value="บันทึก" reRender="listPanel, listSubCategoryPanel, editPanel" disabled ="#{!budgetExpense.editable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{budgetExpense.deleteExpense}" value="ลบ" reRender="listPanel, listSubCategoryPanel, editPanel" disabled ="#{!budgetExpense.newable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			</rich:panel>
		</a4j:region>
		</div>
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
