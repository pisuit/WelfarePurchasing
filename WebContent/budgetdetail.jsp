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
		value="Welfare Purchasing System : Budget Management - Detail" /></title>
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
						<div style="float: right">
							<h:outputText value="ยินดีต้อนรับ : " />
							<h:outputText value="คุณ#{budgetDetail.welcomeMsg}"
								style="color:blue" rendered="#{budgetDetail.welcomeMsg != null}" />
						</div>
					</rich:panel>
					<a4j:region renderRegionOnly="false">
						<rich:panel id="listPanel" style="width:30%; float:left">
							<f:facet name="header">
								<h:panelGroup>
									<h:outputText value="รายการข้อมูลบประมาณสวัสดิการ" />
								</h:panelGroup>
							</f:facet>
							<h:outputText value="ปีงบประมาณ" styleClass="label short" />
							<h:inputText value="#{budgetDetail.listBudgetYear}"
								styleClass="input short" required="true"
								requiredMessage="กรุณาใส่ปีงบประมาณที่ต้องการแสดงข้อมูล" />
							<a4j:commandButton action="#{budgetDetail.listBudgetItem}"
								value="ค้นหา"
								reRender="listPanel, listSubBudgetItemPanel, editPanel"
								oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
								status="showload">
							</a4j:commandButton>
							<div id="wrapper"></div>
							<rich:spacer height="5" />
							<rich:tree id="budgetItemTree"
								value="#{budgetDetail.budgetItemTree}" var="budgetItem"
								nodeSelectListener="#{budgetDetail.budgetItemTreeNodeSelection}"
								ajaxSubmitSelection="true" switchType="client"
								reRender="listPanel, listSubBudgetItemPanel, editPanel"
								style="width:100%; overflow:scroll; " status="showload">
							</rich:tree>
						</rich:panel>
					</a4j:region>
					<div style="float: left; width: 68%; margin-left: 5px;">
						<a4j:region renderRegionOnly="false">
							<rich:panel id="listSubBudgetItemPanel">
								<f:facet name="header">
									<h:panelGroup>
										<h:outputText
											value="รายการงบประมาณแสดงตามหมวด #{budgetDetail.editBudgetItem.category}" />
									</h:panelGroup>
								</f:facet>
								<h:outputText value="ปีงบประมาณ " styleClass="label medium" />
								<h:outputText
									value="#{budgetDetail.parentBudgetItem.budget.budgetYear}"
									styleClass="data" />
								<a4j:commandButton id="link" value="ดูประวัติการใช้งบประมาณ"
									action="#{budgetDetail.listHistory}"
									disabled="#{!budgetDetail.expenseListable}" reRender="tabPanel"
									style="float:right" ajaxSingle="true" status="showload">
									<rich:componentControl for="modalPanel" attachTo="link"
										operation="show" event="onclick" />
								</a4j:commandButton>
								<div id="wrapper"></div>
								<h:outputText value="รหัสงบประมาณ " styleClass="label medium" />
								<h:outputText
									value="#{budgetDetail.parentBudgetItem.accountCode}"
									styleClass="data" />
								<div id="wrapper"></div>
								<h:outputText value="หมวดงบประมาณ " styleClass="label medium" />
								<h:outputText value="#{budgetDetail.parentBudgetItem.category}"
									styleClass="data" style="width:75%" />
								<div id="wrapper"></div>
								<h:outputText value="วงเงินรวม " styleClass="label medium" />
								<h:outputText
									value="#{budgetDetail.parentBudgetItem.initialAmount}"
									styleClass="data number">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<div id="wrapper"></div>
								<h:outputText value="นำไปตั้งเป็นงบประมาณ"
									styleClass="label medium" />
								<h:outputText value="#{budgetDetail.totalInitialAmount}"
									styleClass="data number">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<div id="wrapper"></div>
								<h:outputText value="กันงบ " styleClass="label medium" />
								<h:outputText
									value="#{budgetDetail.parentBudgetItem.reservedAmount}"
									styleClass="data number">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<div id="wrapper"></div>
								<h:outputText value="ใช้งบ " styleClass="label medium" />
								<h:outputText
									value="#{budgetDetail.parentBudgetItem.expensedAmount}"
									styleClass="data number">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<div id="wrapper"></div>
								<h:outputText value="โอนเข้า " styleClass="label medium" />
								<h:outputText
									value="#{budgetDetail.parentBudgetItem.transferInAmount}"
									styleClass="data number">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<div id="wrapper"></div>
								<h:outputText value="โอนออก" styleClass="label medium" />
								<h:outputText
									value="#{budgetDetail.parentBudgetItem.transferOutAmount}"
									styleClass="data number">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<div id="wrapper"></div>
								<h:outputText value="คงเหลือ " styleClass="label medium" />
								<h:outputText
									value="#{budgetDetail.parentBudgetItem.availableAmount}"
									styleClass="data number">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<div id="wrapper"></div>
								<rich:spacer height="5" />
								<rich:dataTable id="tableSubBudgetItemList"
									value="#{budgetDetail.subBudgetItemList}" var="budgetItem"
									rows="5" rowKeyVar="rowNo" style="width:100%"
									rowClasses="odd-row, even-row">
									<rich:column style="text-align:right" width="20px">
										<f:facet name="header">
											<h:outputText value="" />
										</f:facet>
										<h:outputText value="#{rowNo+1}" />
									</rich:column>
									<rich:column style="text-align:left">
										<f:facet name="header">
											<h:outputText value="หมวดงบประมาณ" />
										</f:facet>
										<h:outputText value="#{budgetItem.category}" />
									</rich:column>
									<rich:column style="text-align:right" width="75px">
										<f:facet name="header">
											<h:outputText value="วงเงินที่ตั้ง" />
										</f:facet>
										<h:outputText value="#{budgetItem.initialAmount}">
											<f:convertNumber pattern="#,##0.00" />
										</h:outputText>
									</rich:column>
									<rich:column style="text-align:right" width="75px">
										<f:facet name="header">
											<h:outputText value="กันงบ" />
										</f:facet>
										<h:outputText value="#{budgetItem.reservedAmount}">
											<f:convertNumber pattern="#,##0.00" />
										</h:outputText>
									</rich:column>
									<rich:column style="text-align:right" width="75px">
										<f:facet name="header">
											<h:outputText value="ใช้งบ" />
										</f:facet>
										<h:outputText value="#{budgetItem.expensedAmount}">
											<f:convertNumber pattern="#,##0.00" />
										</h:outputText>
									</rich:column>
									<rich:column style="text-align:right" width="75px">
										<f:facet name="header">
											<h:outputText value="คงเหลือ" />
										</f:facet>
										<h:outputText value="#{budgetItem.availableAmount}">
											<f:convertNumber pattern="#,##0.00" />
										</h:outputText>
									</rich:column>
									<rich:column style="text-align:center" width="16px">
										<h:graphicImage value="/assets/icons/green-button.png"
											rendered="#{budgetDetail.editable}" width="16" height="16"
											style=" height : 16px;" />
										<h:graphicImage value="/assets/icons/red-button.png"
											rendered="#{!budgetDetail.editable}" width="16" height="16" />
									</rich:column>
									<f:facet name="footer">
										<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
									</f:facet>
								</rich:dataTable>
							</rich:panel>
						</a4j:region>
						<div id="wrapper"></div>
						<rich:spacer height="5px" />
						<a4j:region renderRegionOnly="false">
							<rich:panel id="editPanel">
								<f:facet name="header">
									<h:panelGroup>
										<h:outputText
											value="แก้ไขรายการงบประมาณประจำปี #{budgetDetail.editBudget.budgetYear}" />
									</h:panelGroup>
								</f:facet>
								<h:outputText value="รหัสงบประมาณ" styleClass="label medium" />
								<h:inputText label="รหัสงบประมาณ"
									value="#{budgetDetail.editBudgetItem.accountCode}"
									styleClass="input medium" required="true"
									rendered="#{budgetDetail.editBudgetItem.budgetType != 'M'}">
									<f:validateLength minimum="8" maximum="8" />
								</h:inputText>
								<h:outputText value="#{budgetDetail.editBudgetItem.accountCode}"
									styleClass="data medium"
									rendered="#{budgetDetail.editBudgetItem.budgetType == 'M'}" />
								<div id="wrapper"></div>
								<h:outputText value="หมวดงบประมาณ" styleClass="label medium" />
								<h:inputText value="#{budgetDetail.editBudgetItem.category}"
									styleClass="input medium" required="true"
									rendered="#{budgetDetail.editBudgetItem.budgetType != 'M'}"
									style="width:75%">
									<f:validateLength maximum="100" />
								</h:inputText>
								<h:outputText value="#{budgetDetail.editBudgetItem.category}"
									styleClass="data" style="width:75%"
									rendered="#{budgetDetail.editBudgetItem.budgetType == 'M'}" />
								<div id="wrapper"></div>
								<h:outputText value="จำนวนเงิน" styleClass="label medium" />
								<h:inputText label="จำนวนเงิน"
									value="#{budgetDetail.editBudgetItem.initialAmount}"
									styleClass="input medium" style="text-align:right"
									required="true"
									rendered="#{budgetDetail.editBudgetItem.budgetType != 'M'}">
									<f:validateDoubleRange minimum="0.00" />
									<f:convertNumber pattern="#,##0.00" />
								</h:inputText>
								<h:outputText
									value="#{budgetDetail.editBudgetItem.initialAmount}"
									styleClass="data number"
									rendered="#{budgetDetail.editBudgetItem.budgetType == 'M'}">
									<f:convertNumber pattern="#,##0.00" />
								</h:outputText>
								<div id="wrapper"></div>
								<h:selectBooleanCheckbox
									value="#{budgetDetail.editBudgetItem.controlled}"
									style="margin-left:135px; float:left" />
								<h:outputText value="ควบคุมงบประมาณ" styleClass="label long" />
								<div id="wrapper"></div>
								<!--				<h:selectBooleanCheckbox value="#{budgetDetail.editBudgetItem.monthlyBudget}" style="margin-left:135px; float:left"/>-->
								<!--				<h:outputText value="ควบคุมงบประมาณรายเดือน" styleClass="label long"/>-->
								<!--				<div id="wrapper"></div>-->
								<h:selectBooleanCheckbox
									value="#{budgetDetail.editBudgetItem.purchasingBudget}"
									style="margin-left:135px; float:left" />
								<h:outputText value="ใช้กับการสร้าง PR/PO"
									styleClass="label long" />
								<div id="wrapper"></div>
								<h:selectBooleanCheckbox
									value="#{budgetDetail.editBudgetItem.expenseEntry}"
									style="margin-left:135px; float:left" />
								<h:outputText value="ใช้กับการบันทึกค่าใช้จ่าย"
									styleClass="label long" />
								<div id="wrapper"></div>
								<h:selectBooleanCheckbox
									value="#{budgetDetail.editBudgetItem.nurseryBudget}"
									style="margin-left:135px; float:left" />
								<h:outputText value="ใช้กำหนดค่าอาหารเด็ก(Nursery)"
									styleClass="label long" style="width:250px" />
								<div id="wrapper"></div>
								<h:selectBooleanCheckbox
									value="#{budgetDetail.editBudgetItem.available}"
									style="margin-left:135px; float:left" />
								<h:outputText value="เปิดการเบิกใช้สำหรับงบประมาณหมวดนี้"
									styleClass="label long" style="width:250px" />
								<div id="wrapper"></div>
								<rich:separator height="1" lineType="solid"
									style="margin-bottom:5px" />
								<a4j:commandButton action="#{budgetDetail.newBudgetItem}"
									value="เพิ่มรายการใหม่" reRender="editPanel"
									disabled="#{!budgetDetail.editable || budgetDetail.parentBudgetItem == null }"
									ajaxSingle="true"
									oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
									status="showload">
								</a4j:commandButton>
								<a4j:commandButton action="#{budgetDetail.saveBudgetItem}"
									value="บันทึกรายการงบประมาณเดิม"
									reRender="listPanel, listSubBudgetItemPanel, editPanel"
									disabled="#{!budgetDetail.editable || budgetDetail.editBudgetItem.id == null}"
									oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
									status="showload">
								</a4j:commandButton>
								<a4j:commandButton action="#{budgetDetail.saveSubBudgetItem}"
									value="บันทึกเพิ่มรายการงบประมาณย่อย"
									reRender="listPanel, listSubBudgetItemPanel, editPanel"
									disabled="#{!budgetDetail.editable || budgetDetail.editBudgetItem.id != null || budgetDetail.editBudgetItem.parentBudgetItem.budgetLevel == 'Level_4'}"
									oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
									status="showload">
								</a4j:commandButton>
								<a4j:commandButton action="#{budgetDetail.deleteBudgetItem}"
									value="ลบรายการงบประมาณ"
									reRender="listPanel, listSubBudgetItemPanel, editPanel"
									disabled="#{!budgetDetail.editable || budgetDetail.editBudgetItem.id == null}"
									oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
									status="showload">
								</a4j:commandButton>
							</rich:panel>
						</a4j:region>
					</div>
					<rich:modalPanel id="modalPanel" height="700" width="800"
						resizeable="true" domElementAttachment="form">
						<f:facet name="controls">
							<h:panelGroup>
								<h:graphicImage value="/assets/icons/close.png" id="hideTable"
									style="cursor:pointer" />
								<rich:componentControl for="modalPanel" attachTo="hideTable"
									operation="hide" event="onclick" />
							</h:panelGroup>
						</f:facet>
						<div style="float: left; width: 36%">
							<rich:panel id="controlPanel">
								<f:facet name="header">
									<h:panelGroup>
										<h:outputText value="ตัวควบคุมการแสดงผล" />
									</h:panelGroup>
								</f:facet>
								<h:selectOneRadio id="radio"
									value="#{budgetDetail.selectedView}" layout="pageDirection"
									style="margin-bottom:10px">
									<f:selectItem itemLabel="แสดงรายการทั้งหมด" itemValue="allitem" />
									<f:selectItem itemLabel="แสดงรายการโดยระบุวันที่"
										itemValue="selectday" />
								</h:selectOneRadio>
								<div id="wrapper"></div>
								<h:outputText value="จากวันที่" styleClass="label small" />
								<rich:calendar value="#{budgetDetail.fromDate}"
									label="จากวันที่" style="float:left" required="false"
									locale="th" datePattern="dd/MM/yyyy" styleClass="data">
								</rich:calendar>
								<div id="wrapper"></div>
								<h:outputText value="ถึงวันที่" styleClass="label small" />
								<rich:calendar value="#{budgetDetail.toDate}" label="ถึงวันที่"
									style="float:left" required="false" locale="th"
									datePattern="dd/MM/yyyy" styleClass="data">
								</rich:calendar>
								<rich:separator height="1" lineType="solid"
									style="margin-bottom:5px;margin-top:5px" />
								<a4j:commandButton action="#{budgetDetail.listHistory}"
									value="ตกลง" reRender="tabPanel"
									oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
									status="showload" />
							</rich:panel>
						</div>
						<div style="float: left; width: 63%; margin-left: 5px;">
							<rich:panel id="table">
								<f:facet name="header">
									<h:outputText value="รายการประวัติการใช้งบประมาณ" />
								</f:facet>
								<rich:tabPanel id="tabPanel" style="width:100%"
									switchType="ajax">
									<a4j:support event="ontabchange" status="showload" />
									<rich:tab label="ใบรับวัสดุ" ajaxSingle="true">
										<rich:dataTable id="listGR" value="#{budgetDetail.grList}"
											var="gr" rows="10" rowKeyVar="rowNo" style="width:100%"
											rowClasses="odd-row, even-row" styleClass="footer_right">
											<rich:column style="width:25%">
												<f:facet name="header">
													<h:outputText value="วันที่" />
												</f:facet>
												<h:outputText value="#{gr.date}">
													<f:convertDateTime pattern="dd-MM-yyyy" />
												</h:outputText>
											</rich:column>
											<rich:column style="width:40%">
												<f:facet name="header">
													<h:outputText value="เลขที่ใบรับวัสดุ" />
												</f:facet>
												<h:outputText value="#{gr.number}">
													<f:convertNumber pattern="00000000" />
												</h:outputText>
											</rich:column>
											<rich:column style="text-align:right;width:30%">
												<f:facet name="header">
													<h:outputText value="จำนวนเงินรวม" />
												</f:facet>
												<h:outputText value="#{gr.totalPrice}">
													<f:convertNumber pattern="#,##0.00" />
												</h:outputText>
											</rich:column>
											<f:facet name="header">
												<rich:datascroller renderIfSinglePage="false" />
											</f:facet>
											<f:facet name="footer">
												<h:outputText value="#{budgetDetail.sumTotalGR}">
													<f:convertNumber pattern="#,##0.00" />
												</h:outputText>
											</f:facet>
										</rich:dataTable>
									</rich:tab>
									<rich:tab label="ค่าใช้จ่าย" ajaxSingle="true">
										<rich:dataTable id="listExpense"
											value="#{budgetDetail.expenseList}" var="expense" rows="10"
											rowKeyVar="rowNo" style="width:100%"
											rowClasses="odd-row, even-row" styleClass="footer_right">
											<rich:column style="width:25%">
												<f:facet name="header">
													<h:outputText value="วันที่" />
												</f:facet>
												<h:outputText value="#{expense.date}">
													<f:convertDateTime pattern="dd-MM-yyyy" />
												</h:outputText>
											</rich:column>
											<rich:column style="width:40%">
												<f:facet name="header">
													<h:outputText value="รายละเอียด" />
												</f:facet>
												<h:outputText value="#{expense.detail}" />
											</rich:column>
											<rich:column style="text-align:right;width:30%">
												<f:facet name="header">
													<h:outputText value="จำนวนเงินรวม" />
												</f:facet>
												<h:outputText value="#{expense.totalPrice}">
													<f:convertNumber pattern="#,##0.00" />
												</h:outputText>
											</rich:column>
											<f:facet name="footer">
												<h:outputText value="#{budgetDetail.sumTotalExpense}">
													<f:convertNumber pattern="#,##0.00" />
												</h:outputText>
											</f:facet>
											<f:facet name="header">
												<rich:datascroller renderIfSinglePage="false" />
											</f:facet>
										</rich:dataTable>
									</rich:tab>
									<rich:tab label="โอนงบประมาณ" ajaxSingle="true">
										<h:commandLink
											action="#{budgetDetail.printBudgetTransferReport}"
											value="พิมพ์รายงานการโอนงบประมาณ" immediate="true"
											target="_blank" />
										<rich:dataTable id="listTransfer"
											value="#{budgetDetail.transferList}" var="transfer" rows="10"
											rowKeyVar="rowNo" style="width:100%;margin-top:5px"
											rowClasses="odd-row, even-row">
											<rich:column>
												<f:facet name="header">
													<h:outputText value="วันที่" />
												</f:facet>
												<h:outputText value="#{transfer.approveDate}">
													<f:convertDateTime pattern="dd-MM-yyyy" />
												</h:outputText>
											</rich:column>
											<rich:column style="width:10%">
												<f:facet name="header">
													<h:outputText value="เลขที่ใบขอโอน" />
												</f:facet>
												<h:outputText value="#{transfer.transferNumber}">
													<f:convertNumber pattern="0000" />
												</h:outputText>
											</rich:column>
											<rich:column>
												<f:facet name="header">
													<h:outputText value="โอนจาก" />
												</f:facet>
												<h:outputText value="#{transfer.fromBudgetItem.accountCode}" />
											</rich:column>
											<rich:column>
												<f:facet name="header">
													<h:outputText value="โอนไป" />
												</f:facet>
												<h:outputText value="#{transfer.toBudgetItem.accountCode}" />
											</rich:column>
											<rich:column style="text-align:right">
												<f:facet name="header">
													<h:outputText value="จำนวนเงิน" />
												</f:facet>
												<h:outputText value="#{transfer.approveAmount}">
													<f:convertNumber pattern="#,##0.00" />
												</h:outputText>
											</rich:column>
											<f:facet name="footer">
												<rich:datascroller renderIfSinglePage="false" />
											</f:facet>
										</rich:dataTable>
									</rich:tab>
								</rich:tabPanel>
							</rich:panel>
						</div>
					</rich:modalPanel>
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
