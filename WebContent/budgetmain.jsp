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
	<title><h:outputText
		value="Welfare Purchasing System : Budget Management - Main" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=TIS-620" />
	<link rel="stylesheet" type="text/css"
		href="assets/css/templatestyle.css" />
	<link rel="stylesheet" type="text/css" href="assets/css/facestyle.css" />
	<style type="text/css">
.rich-table-subfootercell {  text-align: right; }
	</style>

	</head>
	<body>

	<div id="page"><jsp:include page="header.jsp" /> <jsp:include
		page="menu.jsp" />
	<div id="contentarea"><h:form>
		<rich:panel>
			<h:outputText value="Status: " />
			<a4j:region>
				<a4j:status id="showload" stopStyle="color:green"
					startStyle="color:red">
					<f:facet name="start">
						<h:outputText value="Please Wait ......." />
					</f:facet>
					<f:facet name="stop">
						<h:outputText value="OK" />
					</f:facet>
				</a4j:status>
			</a4j:region>
			<div style="float: right"><h:outputText value="ยินดีต้อนรับ : " />
			<h:outputText value="คุณ#{budgetMain.welcomeMsg}" style="color:blue"
				rendered="#{budgetMain.welcomeMsg != null}" /></div>
		</rich:panel>
		<rich:tabPanel switchType="ajax">
			<a4j:support event="ontabchange" status="showload" />
			<rich:tab label="สร้างงบประมาณประจำปี" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
					<rich:panel id="createPanel">
						<f:facet name="header">
							<h:panelGroup>
								<h:outputText value="สร้างงบประมาณประจำปี" />
							</h:panelGroup>
						</f:facet>
						<h:outputText value="ปีงบประมาณ" styleClass="label medium" />
						<h:inputText label="ปีงบประมาณ"
							value="#{budgetMain.createBudgetYear}" styleClass="input medium"
							style="text-align:right" required="true">
							<f:converter converterId="javax.faces.Integer" />
							<f:validateLongRange minimum="2543" />
						</h:inputText>
						<div id="wrapper"></div>
						<h:outputText value="คัดลอกข้อมูลจากปีงบประมาณ"
							styleClass="label medium" />
						<h:inputText label="คัดลอกข้อมูลจากปีงบประมาณ"
							value="#{budgetMain.createCopyFromBudgetYear}"
							styleClass="input medium" style="text-align:right"
							required="true">
							<f:converter converterId="javax.faces.Integer" />
							<f:validateLongRange minimum="2543" />
						</h:inputText>
						<div id="wrapper"></div>
						<rich:separator height="1" lineType="solid"
							style="margin-bottom:5px" />
						<a4j:commandButton action="#{budgetMain.createBudget}"
							value="สร้างงบประมาณประจำปี" reRender="listPanel, editPanel"
							oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
							status="showload">
						</a4j:commandButton>
						<a4j:commandButton action="#{budgetMain.copyBudget}"
							value="คัดลอกงบประมาณเดิม" reRender="listPanel, editPanel"
							oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
							status="showload">
						</a4j:commandButton>
						<a4j:status>
							<f:facet name="start">
								<h:graphicImage url="/assets/icons/ajax-loader.gif" />
							</f:facet>
						</a4j:status>
					</rich:panel>
				</a4j:region>
			</rich:tab>
			<rich:tab label="แก้ไขงบประมาณประจำปี">
				<rich:panel id="listPanel" style="width:100%; float:left">
					<f:facet name="header">
						<h:panelGroup>
							<h:outputText value="รายการงบประมาณสวัสดิการ(หมวดหลัก)" />
						</h:panelGroup>
					</f:facet>
					<a4j:region renderRegionOnly="false">
						<h:outputText value="ปีงบประมาณ" styleClass="label long" />
						<h:inputText value="#{budgetMain.listBudgetYear}"
							styleClass="input medium" required="true"
							requiredMessage="กรุณาใส่ปีงบประมาณที่ต้องการแสดงข้อมูล" />
						<a4j:commandButton action="#{budgetMain.listMainBudget}"
							value="ค้นหา" reRender="listPanel, editPanel" style="margin-right:5px"
							oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
							status="showload" />
						<h:commandLink action="#{budgetMain.printReport}" value="พิมพ์รายงานงบประมาณสวัสดิการ" immediate="true" target="_blank" rendered="#{budgetMain.printable}"/>
						<div id="wrapper"></div>
						<rich:panel style="float:left; width:100%">
							<h:outputText value="ปีงบประมาณ" styleClass="label long" />
							<h:outputText
								value="#{budgetMain.editBudget != null ? budgetMain.editBudget.budgetYear: ''}"
								styleClass="data" />
							<div id="wrapper"></div>
							<h:outputText value="แก้ไขรายการงบประมาณ" styleClass="label long" />
							<h:graphicImage value="/assets/icons/green-button.png"
								rendered="#{budgetMain.editBudget != null && budgetMain.editBudget.editable}"
								width="16" height="16" />
							<h:graphicImage value="/assets/icons/red-button.png"
								rendered="#{budgetMain.editBudget != null && !budgetMain.editBudget.editable}"
								width="16" height="16" />
							<div id="wrapper"></div>
							<h:outputText value="ใช้งบประมาณ" styleClass="label long" />
							<h:graphicImage value="/assets/icons/green-button.png"
								rendered="#{budgetMain.editBudget != null && budgetMain.editBudget.available}"
								width="16" height="16" />
							<h:graphicImage value="/assets/icons/red-button.png"
								rendered="#{budgetMain.editBudget != null && !budgetMain.editBudget.available}"
								width="16" height="16" />
							<div id="wrapper"></div>
							<rich:separator height="1" lineType="solid"
								style="margin-bottom:5px" />
							<a4j:commandButton action="#{budgetMain.lockBudget}"
								value="#{budgetMain.editBudget.editable ? 'ปิด':'เปิด'}ให้แก้ไข"
								reRender="listPanel, editPanel"
								disabled="#{budgetMain.editBudget == null || budgetMain.editBudget.available}"
								oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
								status="showload" />
							<a4j:commandButton action="#{budgetMain.useBudget}"
								value="#{budgetMain.editBudget.available ? 'ปิด':'เปิด'}การเบิกใช้"
								reRender="listPanel, editPanel"
								disabled="#{budgetMain.editBudget == null}"
								oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
								status="showload" />
						</rich:panel>
					</a4j:region>
					<rich:spacer height="5" />
					<a4j:region renderRegionOnly="false">
						<rich:dataTable id="tableMainBudgetList"
							value="#{budgetMain.budgetItemList}" var="budgetItem" rows="20"
							rowKeyVar="rowNo"
							style="float:left;width:100%"
							rowClasses="odd-row, even-row"
							>
							<a4j:support event="onRowClick"
								action="#{budgetMain.tableMainBudgetRowClicked}"
								reRender="editPanel" status="showload">
								<f:setPropertyActionListener value="#{budgetItem}"
									target="#{budgetMain.editBudgetItem}" />
							</a4j:support>
							<rich:column style="text-align:right" width="20px">
								<f:facet name="header">
									<h:outputText value="" />
								</f:facet>
								<h:outputText value="#{rowNo+1}" />
							</rich:column>
							<rich:column style="text-align:left" width="50px">
								<f:facet name="header">
									<h:outputText value="รหัสงบประมาณ" />
								</f:facet>
								<h:outputText value="#{budgetItem.accountCode}" />
							</rich:column>
							<rich:column style="text-align:left">
								<f:facet name="header">
									<h:outputText value="หมวดงบประมาณ" />
								</f:facet>
								<h:outputText value="#{budgetItem.category}" />
							</rich:column>
							<rich:column style="text-align:right" width="80px">
								<f:facet name="header">
									<h:outputText value="วงเงินที่ตั้ง" />
								</f:facet>
								<h:outputText value="#{budgetItem.initialAmount}">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<f:facet name="footer">
									<h:outputText value="#{budgetMain.sumInitial}">
										<f:convertNumber pattern="#,##0.00" />
									</h:outputText>
								</f:facet>
							</rich:column>
							<rich:column style="text-align:right" width="80px">
								<f:facet name="header">
									<h:outputText value="กันงบ" />
								</f:facet>
								<h:outputText value="#{budgetItem.reservedAmount}">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<f:facet name="footer">
									<h:outputText value="#{budgetMain.sumReserved}">
										<f:convertNumber pattern="#,##0.00" />
									</h:outputText>
								</f:facet>
							</rich:column>
							<rich:column style="text-align:right" width="80px">
								<f:facet name="header">
									<h:outputText value="ใช้งบ" />
								</f:facet>
								<h:outputText value="#{budgetItem.expensedAmount}">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<f:facet name="footer">
									<h:outputText value="#{budgetMain.sumUsed}">
										<f:convertNumber pattern="#,##0.00" />
									</h:outputText>
								</f:facet>
							</rich:column>
							<rich:column style="text-align:right" width="80px">
								<f:facet name="header">
									<h:outputText value="โอนเข้า" />
								</f:facet>
								<h:outputText value="#{budgetItem.transferInAmount}">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<f:facet name="footer">
									<h:outputText value="#{budgetMain.sumTransferIn}">
										<f:convertNumber pattern="#,##0.00" />
									</h:outputText>
								</f:facet>
							</rich:column>
							<rich:column style="text-align:right" width="80px">
								<f:facet name="header">
									<h:outputText value="โอนออก" />
								</f:facet>
								<h:outputText value="#{budgetItem.transferOutAmount}">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<f:facet name="footer">
									<h:outputText value="#{budgetMain.sumTransferOut}">
										<f:convertNumber pattern="#,##0.00" />
									</h:outputText>
								</f:facet>
							</rich:column>
							<rich:column style="text-align:right" width="80px">
								<f:facet name="header">
									<h:outputText value="คงเหลือ" />
								</f:facet>
								<h:outputText value="#{budgetItem.availableAmount}">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<f:facet name="footer">
									<h:outputText value="#{budgetMain.sumRemain}">
										<f:convertNumber pattern="#,##0.00" />
									</h:outputText>
								</f:facet>
							</rich:column>
							<rich:column style="text-align:center" width="16px">
								<f:facet name="header">
									<h:outputText value="แก้ไข" />
								</f:facet>
								<h:graphicImage value="/assets/icons/green-button.png"
									rendered="#{budgetItem.editable && budgetMain.editBudget.editable}"
									width="16" height="16" />
								<h:graphicImage value="/assets/icons/red-button.png"
									rendered="#{!budgetItem.editable || !budgetMain.editBudget.editable}"
									width="16" height="16" />
							</rich:column>
							<rich:column style="text-align:center" width="16px">
								<f:facet name="header">
									<h:outputText value="ใช้งบ" />
								</f:facet>
								<h:graphicImage value="/assets/icons/green-button.png"
									rendered="#{budgetItem.available && budgetMain.editBudget.available}"
									width="16" height="16" />
								<h:graphicImage value="/assets/icons/red-button.png"
									rendered="#{!budgetItem.available || !budgetMain.editBudget.available}"
									width="16" height="16" />
							</rich:column>
							<f:facet name="footer">
								<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
							</f:facet>
						</rich:dataTable>
					</a4j:region>
					<div id="wrapper"></div>
					<rich:spacer height="5" />
					<a4j:region renderRegionOnly="false">
						<rich:panel id="editPanel" style="float:left; width:100%">
							<f:facet name="header">
								<h:panelGroup>
									<h:outputText
										value="แก้ไขรายการงบประมาณประจำปี #{budgetMain.editBudget.budgetYear}" />
								</h:panelGroup>
							</f:facet>
							<h:outputText value="ปีงบประมาณ" styleClass="label medium" />
							<h:outputText value="#{budgetMain.editBudget.budgetYear}"
								styleClass="data number" />
							<div id="wrapper"></div>
							<h:outputText value="รหัสงบประมาณ" styleClass="label medium" />
							<h:inputText label="รหัสงบประมาณ"
								value="#{budgetMain.editBudgetItem.accountCode}"
								styleClass="input medium" required="true">
								<f:validateLength minimum="8" maximum="8" />
							</h:inputText>
							<div id="wrapper"></div>
							<h:outputText value="หมวดงบประมาณ" styleClass="label medium" />
							<h:inputText label="หมวดงบประมาณ"
								value="#{budgetMain.editBudgetItem.category}"
								styleClass="input medium" style="width:70%" required="true">
								<f:validateLength maximum="100" />
							</h:inputText>
							<div id="wrapper"></div>
							<h:outputText value="จำนวนเงิน" styleClass="label medium" />
							<h:inputText label="จำนวนเงิน"
								value="#{budgetMain.editBudgetItem.initialAmount}"
								styleClass="input medium" style="text-align:right"
								required="true">
								<f:convertNumber pattern="#,##0.00" />
								<f:validateDoubleRange minimum="1.00" />
							</h:inputText>
							<div id="wrapper"></div>
							<rich:separator height="1" lineType="solid"
								style="margin-bottom:5px" />
							<a4j:commandButton action="#{budgetMain.newBudgetItem}"
								value="เพิ่ม" reRender="tableMainBudgetList, editPanel"
								disabled="#{!budgetMain.editable || budgetMain.editBudgetItem.budget == null }"
								ajaxSingle="true"
								oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
								status="showload" />
							<a4j:commandButton action="#{budgetMain.saveBudgetItem}"
								value="บันทึก" reRender="tableMainBudgetList, editPanel"
								disabled="#{!budgetMain.editable}"
								oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
								status="showload" />
							<a4j:commandButton action="#{budgetMain.deleteBudgetItem}"
								value="ลบ" reRender="tableMainBudgetList, editPanel"
								disabled="#{!budgetMain.editable}"
								oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
								status="showload" />
							<a4j:commandButton action="#{budgetMain.lockBudgetItem}"
								value="#{budgetMain.editBudgetItem.editable ? 'ปิด':'เปิด'}การบันทึกงบประมาณ"
								reRender="tableMainBudgetList, editPanel"
								disabled="#{!budgetMain.editable}"
								oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
								style="margin-right:10px" status="showload" />
							<a4j:status>
								<f:facet name="start">
									<h:graphicImage url="/assets/icons/ajax-loader.gif" />
								</f:facet>
							</a4j:status>
						</rich:panel>
					</a4j:region>
				</rich:panel>
			</rich:tab>
		</rich:tabPanel>
		<rich:modalPanel id="mpErrors" resizeable="true">
			<f:facet name="header">
				<h:outputText value="Error" />
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<h:graphicImage value="/assets/icons/close.png" id="hideError"
						style="cursor:pointer" />
					<rich:componentControl for="mpErrors" attachTo="hideError"
						operation="hide" event="onclick" />
				</h:panelGroup>
			</f:facet>
			<rich:messages layout="table">
				<f:facet name="errorMarker">
					<h:graphicImage url="/assets/icons/critical.png" />
				</f:facet>
			</rich:messages>
		</rich:modalPanel>
		<rich:modalPanel id="mpStatus" autosized="true" width="400"
			height="300">
			<a4j:status onstop="#{rich:component('mpStatus')}.hide()">
				<f:facet name="start">
					<h:graphicImage value="/assets/icons/loading.gif" />
				</f:facet>
			</a4j:status>

		</rich:modalPanel>
	</h:form></div>
	<div id="content_bottom">
	<div id="content_bottom_left"></div>
	<div id="content_bottom_center"></div>
	<div id="content_bottom_right"></div>
	</div>
	<jsp:include page="footer.jsp" /></div>
	</body>
</f:view>
</html>
