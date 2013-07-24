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
		<h:outputText value="คุณ#{budgetTransferApprove.welcomeMsg}" style="color:blue" rendered="#{budgetTransferApprove.welcomeMsg != null}"/>
		</div>
		</rich:panel>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="listPanel" style="width:49%; float:left">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="รายการใบขอโอนงบประมาณ" />
				</h:panelGroup>
			</f:facet>
			<h:outputText value="ปีงบประมาณ" styleClass="label long"/>
			<h:inputText value="#{budgetTransferApprove.listBudgetYear}" styleClass="input medium" required="true" requiredMessage="กรุณาใส่ปีงบประมาณที่ต้องการแสดงข้อมูล"/>
			<a4j:commandButton action="#{budgetTransferApprove.listBudgetTransfer}" value="ค้นหา" reRender="listPanel, editPanel" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<div id="wrapper"></div>
			<rich:spacer height="5"/>
			<rich:dataTable
				id="tableBudgetTransferList"
				value="#{budgetTransferApprove.budgetTransferList}" 
				var="budgetTransfer"  
				rows="20" 
				rowKeyVar="rowNo"
				style="width:100%"	
				rowClasses="odd-row, even-row"			
				>
				<a4j:support event="onRowClick" action="#{budgetTransferApprove.tableBudgetTransferRowClicked}" reRender="editPanel" status="showload">
					<f:setPropertyActionListener value="#{budgetTransfer}" target="#{budgetTransferApprove.editBudgetTransfer}" />
				</a4j:support>
				<rich:column style="text-align:right"  width="50px" sortBy="#{budgetTransfer.transferNumber}">
					<f:facet name="header"><h:outputText value="เลขที่ใบขอโอน"/></f:facet>
					<h:outputText value="#{budgetTransfer.transferNumber}" >
						<f:convertNumber pattern="0000"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left"  width="75px">
					<f:facet name="header"><h:outputText value="วันที่ขอโอน"/></f:facet>
					<h:outputText value="#{budgetTransfer.requestDate}">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left">
					<f:facet name="header"><h:outputText value="เหตุผลที่ขอโอน"/></f:facet>
					<h:outputText value="#{budgetTransfer.reason}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:center"  width="75px" sortBy="#{budgetTransfer.status}">
					<f:facet name="header"><h:outputText value="สถานะ"/></f:facet>
					<h:outputText value="#{budgetTransfer.status}">
					</h:outputText>
				</rich:column>
				<f:facet name="footer">
					<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
				</f:facet>s
			</rich:dataTable>
		</rich:panel>
		</a4j:region>
		<a4j:region renderRegionOnly="false">
			<rich:panel id="editPanel" style="width:49%; float:left">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="รายละเอียดใบขอโอนงบประมาณ" />
					</h:panelGroup>
				</f:facet>
				<h:outputText value="ปีงบประมาณ" styleClass="label long"/>
				<h:outputText value="#{budgetTransferApprove.editBudgetTransfer.fromBudgetItem.budget.budgetYear}" styleClass="data number" />
				<div id="wrapper"></div>
				<h:outputText value="เลขที่ใบขอโอน" styleClass="label long"/>
				<h:outputText value="#{budgetTransferApprove.editBudgetTransfer.transferNumber}" styleClass="data number" >
					<f:convertNumber pattern="0000"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value="วันที่ทำรายการขอโอน" styleClass="label long"/>
				<h:outputText value="#{budgetTransferApprove.editBudgetTransfer.requestDate}" styleClass="data long">
					<f:convertDateTime pattern="dd/MM/yyyy"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value="วันที่อนุมัติหรือยกเลิก" styleClass="label long"/>
				<h:outputText value="#{budgetTransferApprove.editBudgetTransfer.approveDate}" styleClass="data long">
					<f:convertDateTime pattern="dd/MM/yyyy"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value="สถานะ" styleClass="label long"/>
				<h:outputText value="#{budgetTransferApprove.editBudgetTransfer.status}" styleClass="data long"/>
				<div id="wrapper"></div>
				<h:outputText value="ขอโอนงบประมาณจากหมวด" styleClass="label long" />
				<h:outputText value="#{budgetTransferApprove.editBudgetTransfer.oldFromBudgetItem.category}" styleClass="data" style="width:50%"/>
				<div id="wrapper"></div>
				<h:outputText value="เปลี่ยนเป็นขอโอนจากหมวด" styleClass="label long" />
				<h:selectOneMenu label="เปลี่ยนเป็นขอโอนจากหมวด" value="#{budgetTransferApprove.selectedFromBudgetItemId}" styleClass="input verylong" style="width:50%">
					<f:selectItem itemValue="-1" itemLabel="เลือกหมวดงบประมาณที่ต้องการเปลี่ยน"/>
					<f:selectItems value="#{budgetTransferApprove.budgetItemSelectItemList}"/>
					<a4j:support action="#{budgetTransferApprove.fromBudgetItemComboboxSelected}" event="onchange" reRender="editPanel" ajaxSingle="true" status="showload"/>
				</h:selectOneMenu>
				<h:outputText value="" styleClass="label long" />
				<rich:panel style="width:50%; float:left; margin-bottom:5px">
					<h:outputText value="สถานะการใช้งบ : " />
					<h:outputText value="#{budgetTransferApprove.editFromBudgetItem.fullAvaiableStr}" style="font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<div id="wrapper"></div>
					<h:outputText value="จำนวนเงินคงเหลือ : " />
					<h:outputText value="#{budgetTransferApprove.editFromBudgetItem.availableAmount}" style="font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<div id="wrapper"></div>
				</rich:panel>
				<div id="wrapper"></div>
				<h:outputText value="โอนงบประมาณไปยังหมวด" styleClass="label long"/>
				<h:outputText value="#{budgetTransferApprove.editBudgetTransfer.oldToBudgetItem.category}" styleClass="data" style="width:50%"/>
				<div id="wrapper"></div>
				<h:outputText value="เปลี่ยนเป็นขอโอนไปยังหมวด" styleClass="label long" />
				<h:selectOneMenu label="เปลี่ยนเป็นขอโอนไปยังหมวด" value="#{budgetTransferApprove.selectedToBudgetItemId}" styleClass="input verylong" style="width:50%">
					<f:selectItem itemValue="-1" itemLabel="เลือกหมวดงบประมาณที่ต้องการเปลี่ยน"/>
					<f:selectItems value="#{budgetTransferApprove.budgetItemSelectItemList}"/>
					<a4j:support action="#{budgetTransferApprove.toBudgetItemComboboxSelected}" event="onchange" reRender="editPanel" ajaxSingle="true" status="showload"/>
				</h:selectOneMenu>
				<h:outputText value="" styleClass="label long" />
				<rich:panel style="width:50%; float:left; margin-bottom:5px">
					<h:outputText value="สถานะการใช้งบ : " />
					<h:outputText value="#{budgetTransferApprove.editToBudgetItem.fullAvaiableStr}" style="font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<div id="wrapper"></div>
					<h:outputText value="จำนวนเงินคงเหลือ : " />
					<h:outputText value="#{budgetTransferApprove.editToBudgetItem.availableAmount}" style="font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<div id="wrapper"></div>
				</rich:panel>
				<div id="wrapper"></div>
				<h:outputText value="เหตุผลที่ขอโอน" styleClass="label long"/>
				<h:inputText value="#{budgetTransferApprove.editBudgetTransfer.reason}" styleClass="input medium" style="width:50%" >
					<f:validateLength maximum="100"/>
				</h:inputText>
				<div id="wrapper"></div>
				<h:outputText value="เลขที่เอกสารอ้างอิง" styleClass="label long"/>
				<h:outputText value="#{budgetTransferApprove.editBudgetTransfer.externalTransferNumber}" styleClass="data medium" />
				<div id="wrapper"></div>
				<h:outputText value="จำนวนเงินที่ขอโอน" styleClass="label long"/>
				<h:outputText value="#{budgetTransferApprove.editBudgetTransfer.requestAmount}" styleClass="data number"  >
					<f:convertNumber pattern="#,##0.00"/>
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value="จำนวนเงินที่อนุมัติ" styleClass="label long"/>
				<h:inputText label="จำนวนเงิน" value="#{budgetTransferApprove.editBudgetTransfer.approveAmount}" styleClass="input medium" style="text-align:right" required="true" >
					<f:convertNumber pattern="#,##0.00"/>
					<f:validateDoubleRange minimum="0"/>
				</h:inputText>
				<div id="wrapper"></div>
				<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
				<a4j:commandButton action="#{budgetTransferApprove.approveTransfer}" value="อนุมัติ" reRender="listPanel, editPanel" disabled ="#{!budgetTransferApprove.approveable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
				<a4j:commandButton action="#{budgetTransferApprove.discardTransfer}" value="ยกเลิกใบขอโอน" reRender="listPanel, editPanel" disabled ="#{!budgetTransferApprove.discardable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
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
			<rich:messages layout="table" >
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
