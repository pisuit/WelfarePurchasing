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
		value="Welfare System : Manage Purchase Requisition " /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=TIS-620" />
	<link rel="stylesheet" type="text/css"
		href="assets/css/templatestyle.css" />
	<link rel="stylesheet" type="text/css" href="assets/css/facestyle.css" />
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
			<h:outputText value="คุณ#{purchasingPR.welcomeMsg}"
				style="color:blue" rendered="#{purchasingPR.welcomeMsg != null}" />
			</div>
		</rich:panel>
		<a4j:region renderRegionOnly="false">
			<rich:panel id="controlPanel" style="width:100%">
				<f:facet name="header">
					<h:outputText value="ใบแจ้งการจัดหา" />
				</f:facet>
				<h:outputText value="ประเภท" style="margin-right:5px; float:left;" />
				<h:selectOneMenu value="#{purchasingPR.selectedPRType}"
					style="float:left; margin-right:5px; width:150px">
					<f:selectItems value="#{purchasingPR.prTypeSelectItemList}" />
					<a4j:support action="#{purchasingPR.prTypeComboBoxSelected}"
						event="onchange" reRender="controlPanel, headerPanel, itemPanel"
						ajaxSingle="true"
						oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" />
				</h:selectOneMenu>
				<h:outputText value="งบประมาณ" style="float:left; margin-right:5px" />
				<h:selectOneMenu label="งบประมาณ"
					value="#{purchasingPR.selectedBudgetItemIDMain}"
					styleClass="input verylong" style="margin-right:5px"
					disabled="#{purchasingPR.prMedical}">
					<f:selectItems value="#{purchasingPR.budgetItemSelectItemList}" />
					<a4j:support action="#{purchasingPR.budgetItemComboBoxSelected}"
						event="onchange" ajaxSingle="true" reRender="availableamount" />
				</h:selectOneMenu>
				<h:panelGroup id="availableamount">
					<h:outputText value="จำนวนเงินที่เหลือ "
						style="margin-right:3px;color:blue"
						rendered="#{purchasingPR.selectedBudgetItem != null}" />
					<h:outputText
						value="#{purchasingPR.selectedBudgetItem.availableAmount}"
						rendered="#{purchasingPR.selectedBudgetItem != null}"
						style="color:blue">
						<f:convertNumber pattern="#,##0.00" />
					</h:outputText>
				</h:panelGroup>
				<div id="wrapper"></div>
				<rich:panel>
					<a4j:commandButton action="#{purchasingPR.newPR}"
						value="เพิ่มรายการใหม่"
						reRender="controlPanel, headerPanel, itemPanel,listPanel"
						disabled="#{!purchasingPR.prCreatable}"
						oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
						status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.savePR}"
						value="บันทึกใบแจ้งจัดหา"
						reRender="controlPanel, headerPanel, itemPanel,listPanel"
						disabled="#{!purchasingPR.prEditable}"
						oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
						status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.deletePR}"
						value="ยกเลิกใบแจ้งจัดหา"
						reRender="controlPanel, headerPanel, itemPanel,listPanel"
						disabled="#{!purchasingPR.prDeletable}"
						oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
						status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.copyPR}"
						value="คัดลอกข้อมูลใบแจ้งจัดหา"
						reRender="controlPanel, headerPanel, itemPanel,listPanel"
						disabled="#{!purchasingPR.prCopyable}"
						oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
						status="showload">
					</a4j:commandButton>
					<h:commandLink action="#{purchasingPR.printPR}"
						value="พิมพ์ใบแจ้งจัดหา" immediate="true" target="_blank"
						rendered="#{purchasingPR.prPrintable}" />
					<a4j:commandButton id="link" value="แสดงรายการใบแจ้งจัดหา"
						ajaxSingle="true" disabled="false"
						oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
						style="float:right">
						<rich:componentControl for="modalPanel" attachTo="link"
							operation="show" event="onclick" />
					</a4j:commandButton>
				</rich:panel>
			</rich:panel>
			<div id="wrapper"></div>
			<rich:spacer height="5" />
			<rich:panel id="headerPanel" style="width:100%">
				<f:facet name="header">
					<h:outputText value="ส่วนหัว" />
				</f:facet>
				<h:outputText value="เลขที่ใบแจ้งจัดหา"
					style="float:left; margin-right:5px" />
				<h:outputText value="#{purchasingPR.editPR.prNumber}"
					style="float:left; margin-right:5px;font-weight:bold">
					<f:convertNumber pattern="00000000" />
				</h:outputText>
				<h:outputText value="วันที่สร้าง"
					style="float:left; margin-right:5px" />
				<h:outputText value="#{purchasingPR.editPR.postingDate}"
					style="float:left; margin-right:5px;font-weight:bold">
					<f:convertDateTime pattern="dd/MM/yyyy" />
				</h:outputText>
				<h:outputText value="สถานะ" style="float:left; margin-right:5px" />
				<h:outputText value="#{purchasingPR.editPR.status}"
					style="float:left; margin-right:5px;font-weight:bold">
				</h:outputText>
				<h:outputText value="จำนวนเงินรวม"
					style="float:left; margin-right:5px" />
				<h:outputText value="#{purchasingPR.editPR.totalPrice}"
					style="float:left; margin-right:5px;font-weight:bold">
					<f:convertNumber pattern="#,##0.00" />
				</h:outputText>
				<div id="wrapper"></div>
				<h:outputText value="เหตุผลและความจำเป็น" styleClass="label medium" />
				<h:inputTextarea value="#{purchasingPR.editPR.reason}"
					label="เหตุผลและความจำเป็น" styleClass="input" style="width:70%"
					rows="3" required="true">
					<f:validateLength maximum="255" />
				</h:inputTextarea>
				<div id="wrapper"></div>
				<h:outputText value="เลขที่เอกสารขออนุมัติ"
					styleClass="label medium" />
				<h:inputText value="#{purchasingPR.editPR.referenceDocNumber}"
					label="เเลขที่เอกสารขออนุมัติ" styleClass="input medium">
					<f:validateLength maximum="50" />
				</h:inputText>
				<div id="wrapper"></div>
				<h:outputText value="ชื่อผู้แจ้งจัดหา" styleClass="label medium" />
				<rich:comboBox value="#{purchasingPR.editPR.requisitionerName}"
					label="ชื่อผู้แจ้งจัดหา"
					suggestionValues="#{purchasingPR.requisitionerNameList}"
					styleClass="input medium" style="float:left" required="true">
					<f:validateLength maximum="50" />
				</rich:comboBox>
				<h:outputText value="ตำแหน่งผู้แจ้งจัดหา" styleClass="label medium" />
				<rich:comboBox value="#{purchasingPR.editPR.requisitionerPos}"
					label="ตำแหน่งผู้แจ้งจัดหา"
					suggestionValues="#{purchasingPR.requisitionerPosList}"
					styleClass="input medium" style="float:left" required="true">
					<f:validateLength maximum="50" />
				</rich:comboBox>
				<div id="wrapper"></div>
				<h:outputText value="ชื่อผู้ตรวสอบ" styleClass="label medium" />
				<rich:comboBox value="#{purchasingPR.editPR.inspectorName}"
					label="ชื่อผู้ตรวจสอบ"
					suggestionValues="#{purchasingPR.inspectorNameList}"
					styleClass="input medium" style="float:left" required="true">
					<f:validateLength maximum="50" />
				</rich:comboBox>
				<h:outputText value="ตำแหน่งผู้ตรวจสอบ" styleClass="label medium" />
				<rich:comboBox value="#{purchasingPR.editPR.inspectorPos}"
					label="ตำแหน่งผู้ตรวจสอบ"
					suggestionValues="#{purchasingPR.inspectorPosList}"
					styleClass="input medium" style="float:left" required="true">
					<f:validateLength maximum="50" />
				</rich:comboBox>
				<div id="wrapper"></div>
				<h:outputText value="ชื่อผู้อนุมัติ" styleClass="label medium" />
				<rich:comboBox value="#{purchasingPR.editPR.approverName}"
					label="ชื่อผู้อนุมัติ"
					suggestionValues="#{purchasingPR.approverNameList}"
					styleClass="input medium" style="float:left" required="true">
					<f:validateLength maximum="50" />
				</rich:comboBox>
				<h:outputText value="ตำแหน่งอนุมัติ" styleClass="label medium" />
				<rich:comboBox value="#{purchasingPR.editPR.approverPos}"
					label="ตำแหน่งอนุมัติ"
					suggestionValues="#{purchasingPR.approverPosList}"
					styleClass="input medium" style="float:left" required="true">
					<f:validateLength maximum="50" />
				</rich:comboBox>
				<div id="wrapper"></div>
			</rich:panel>
		</a4j:region>
		<div id="wrapper"></div>
		<rich:spacer height="5" />
		<a4j:region renderRegionOnly="false">
			<rich:panel id="itemPanel" style="width:100%">
				<f:facet name="header">
					<h:outputText value="รายการวัสดุ" />
				</f:facet>
				<a4j:region renderRegionOnly="false">
					<rich:dataTable id="tablePRItemList"
						value="#{purchasingPR.prItemList}" var="prItem" rows="20"
						rowKeyVar="rowNo" style="width:100%"
						rowClasses="odd-row, even-row">
						<a4j:support event="onRowClick"
							action="#{purchasingPR.prItemTableRowClicked}"
							reRender="itemEditPanel" status="showload">
							<f:setPropertyActionListener value="#{prItem}"
								target="#{purchasingPR.editPRItem}" />
						</a4j:support>
						<rich:column style="text-align:left">
							<f:facet name="header">
								<h:outputText value="ชื่อวัสดุ" />
							</f:facet>
							<h:outputText value="#{prItem.material.description}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left">
							<f:facet name="header">
								<h:outputText value="ชื่อพัสดุอื่น" />
							</f:facet>
							<h:outputText value="#{prItem.otherMaterial}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="100px">
							<f:facet name="header">
								<h:outputText value="จำนวน" />
							</f:facet>
							<h:outputText value="#{prItem.quantity}">
								<f:convertNumber pattern="#,##0.00" />
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left">
							<f:facet name="header">
								<h:outputText value="หน่วย" />
							</f:facet>
							<h:outputText value="#{prItem.orderUnit}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="100px">
							<f:facet name="header">
								<h:outputText value="ราคาต่อหน่วย" />
							</f:facet>
							<h:outputText value="#{prItem.unitPrice}">
								<f:convertNumber pattern="#,##0.00" />
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right">
							<f:facet name="header">
								<h:outputText value="ราคารวม" />
							</f:facet>
							<h:outputText value="#{prItem.totalPrice}">
								<f:convertNumber pattern="#,##0.00" />
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="50px">
							<f:facet name="header">
								<h:outputText value="วันที่ต้องการใช้" />
							</f:facet>
							<h:outputText value="#{prItem.deliveryDate}">
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="50px">
							<f:facet name="header">
								<h:outputText value="สถานะ" />
							</f:facet>
							<h:outputText value="#{prItem.status}">
							</h:outputText>
						</rich:column>
						<f:facet name="footer">
							<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
						</f:facet>
					</rich:dataTable>
				</a4j:region>
				<div id="wrapper"></div>
				<rich:spacer height="5px" />
				<rich:panel id="itemEditPanel" style="width:100%">
					<h:outputText value="กลุ่มวัสดุ" styleClass="label medium" />
					<h:selectOneMenu label="กลุ่มวัสดุ"
						value="#{purchasingPR.selectedMaterialGroupID}"
						styleClass="input medium" style="width:50%">
						<f:selectItems value="#{purchasingPR.materialGroupSelectItemList}" />
						<a4j:support
							action="#{purchasingPR.materialGroupComboBoxSelected}"
							event="onchange"
							reRender="material, otherMaterial, orderUnit, orderUnitPrice"
							ajaxSingle="true" status="showload" />
					</h:selectOneMenu>
					<div id="wrapper"></div>
					<h:outputText value="วัสดุ" styleClass="label medium" />
					<h:selectOneMenu id="material" label="วัสดุ"
						value="#{purchasingPR.selectedMaterialID}"
						styleClass="input medium" style="width:50%">
						<f:selectItems value="#{purchasingPR.materialSelectItemList}" />
						<a4j:support action="#{purchasingPR.materialComboBoxSelected}"
							event="onchange"
							reRender="otherMaterial, orderUnit, orderUnitPrice"
							ajaxSingle="true" />
					</h:selectOneMenu>
					<div id="wrapper"></div>
					<h:outputText value="ชื่อพัสดุอื่น" styleClass="label medium" />
					<h:inputText id="otherMaterial"
						value="#{purchasingPR.editPRItem.otherMaterial}"
						label="ชื่อพัสดุอื่น" styleClass="input verylong"
						style="width:50%" required="false">
						<f:validateLength maximum="80" />
					</h:inputText>
					<div id="wrapper"></div>
					<h:outputText value="วันที่ต้องการใช้งาน" styleClass="label medium" />
					<rich:calendar value="#{purchasingPR.editPRItem.deliveryDate}"
						label="วันที่ต้องการใช้งาน" style="float:left" required="true"
						locale="th" datePattern="dd/MM/yyyy">
					</rich:calendar>
					<div id="wrapper"></div>
					<h:outputText value="จำนวน" styleClass="label medium" />
					<h:inputText id="quantity"
						value="#{purchasingPR.editPRItem.quantity}" label="จำนวน"
						styleClass="input medium" style="text-align:right" required="true">
						<f:validateDoubleRange minimum="1" />
						<f:convertNumber pattern="#,##0.00" />
					</h:inputText>
					<div id="wrapper"></div>
					<h:outputText value="หน่วย" styleClass="label medium" />
					<rich:comboBox id="orderUnit"
						value="#{purchasingPR.editPRItem.orderUnit}" label="หน่วย"
						suggestionValues="#{purchasingPR.orderUnitList}"
						styleClass="input medium" listStyle="text-align:left"
						required="true">
						<f:validateLength maximum="25" />
					</rich:comboBox>
					<div id="wrapper"></div>
					<h:outputText value="ราคาต่อหน่วย" styleClass="label medium" />
					<h:inputText id="orderUnitPrice"
						value="#{purchasingPR.editPRItem.unitPrice}" label="ราคาต่อหน่วย"
						styleClass="input medium" style="text-align:right"
						required="#{!purchasingPR.priceInputTextDisabled}"
						disabled="#{purchasingPR.priceInputTextDisabled}">
						<f:validateDoubleRange minimum="0.1" />
						<f:convertNumber pattern="#,##0.00" />
					</h:inputText>
					<div id="wrapper"></div>
					<h:outputText value="งบประมาณ" styleClass="label medium" />
					<h:selectOneMenu label="งบประมาณ"
						value="#{purchasingPR.selectedBudgetItemID}"
						styleClass="input verylong" style="width:50%"
						disabled="#{!purchasingPR.prMedical}">
						<f:selectItems value="#{purchasingPR.budgetItemSelectItemList}" />
						<a4j:support action="#{purchasingPR.prItemBudgetItemComboBoxSelected}"
							event="onchange" ajaxSingle="true" reRender="availableamount2" />
					</h:selectOneMenu>
					<h:panelGroup id="availableamount2">
						<h:outputText value="จำนวนเงินที่เหลือ "
							style="margin-right:3px;color:blue"
							rendered="#{purchasingPR.selectedBudgetItem != null}" />
						<h:outputText
							value="#{purchasingPR.selectedBudgetItem.availableAmount}"
							rendered="#{purchasingPR.selectedBudgetItem != null}"
							style="color:blue">
							<f:convertNumber pattern="#,##0.00" />
						</h:outputText>
					</h:panelGroup>

					<div id="wrapper"></div>
					<rich:separator height="1" lineType="solid"
						style="margin-bottom:5px" />
					<a4j:commandButton action="#{purchasingPR.newPrItem}"
						value="เพิ่มรายการใหม่" reRender="itemPanel"
						disabled="#{!purchasingPR.prItemCreatable}" ajaxSingle="true"
						oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
						status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.savePrItem}"
						value="บันทึกรายการ" reRender="itemPanel"
						disabled="#{!purchasingPR.prItemEditable}"
						oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
						status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.deletePrItem}"
						value="ลบรายการ" reRender="itemPanel"
						disabled="#{!purchasingPR.prItemDeletable }" ajaxSingle="true"
						oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"
						status="showload">
					</a4j:commandButton>
				</rich:panel>
			</rich:panel>
		</a4j:region>
		<rich:modalPanel id="modalPanel" height="700" width="800"
			resizeable="true">
			<f:facet name="header">
				<h:outputText value="รายการใบแจ้งจัดหา" />
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<h:graphicImage value="/assets/icons/close.png" id="hideTable"
						style="cursor:pointer" />
					<rich:componentControl for="modalPanel" attachTo="hideTable"
						operation="hide" event="onclick" />
				</h:panelGroup>
			</f:facet>
			<a4j:region renderRegionOnly="false">
				<rich:panel id="listPanel" style="width:100%">
					<h:outputText
						value="OPEN : ใบแจ้งสามารถนำไปสร้างเป็นเอกสารจัดหาได้ / CLOSED : ใบแจ้งถูกนำไปสร้างเป็นเอกสารจัดหารแล้ว / DELETED : ใบแจ้งถูกลบไม่สามารถนำไปดำเนินการต่อได้" />
					<div id="wrapper"></div>
					<rich:spacer height="5" />
					<rich:dataTable id="tablePRList" value="#{purchasingPR.prList}"
						var="pr" rows="20" rowKeyVar="rowNo" style="width:100%"
						rowClasses="odd-row, even-row">
						<a4j:support event="onRowClick"
							action="#{purchasingPR.prTableRowClicked}"
							reRender="controlPanel, headerPanel, itemPanel" status="showload">
							<f:setPropertyActionListener value="#{pr}"
								target="#{purchasingPR.editPR}" />
						</a4j:support>
						<rich:componentControl for="modalPanel" operation="hide"
							event="onclick" />
						<rich:column style="text-align:right" width="50px">
							<f:facet name="header">
								<h:outputText value="เลยที่ใบแจ้งจัดหา" />
							</f:facet>
							<h:outputText value="#{pr.prNumber}">
								<f:convertNumber pattern="00000000" />
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="50px">
							<f:facet name="header">
								<h:outputText value="วันที่สร้าง" />
							</f:facet>
							<h:outputText value="#{pr.postingDate}">
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" >
							<f:facet name="header">
								<h:outputText value="เหตุผล" />
							</f:facet>
							<h:outputText value="#{pr.reason}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="100px">
							<f:facet name="header">
								<h:outputText value="ราคารวม" />
							</f:facet>
							<h:outputText value="#{pr.totalPrice}">
								<f:convertNumber pattern="#,##0.00" />
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" width="50px">
							<f:facet name="header">
								<h:outputText value="สถานะ" />
							</f:facet>
							<h:outputText value="#{pr.status}">
							</h:outputText>
						</rich:column>
						<%--<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>--%>
					</rich:dataTable>
					<rich:datascroller renderIfSinglePage="false" align="center"
						for="tablePRList" />
				</rich:panel>
			</a4j:region>
		</rich:modalPanel>
		<%--<rich:tabPanel style="width:100%" switchType="ajax">
			<rich:tab label="รายการใบแจ้งจัดหา" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
				<rich:panel id="listPanel" style="width:100%">
				<h:outputText value="OPEN : ใบแจ้งสามารถนำไปสร้างเป็นเอกสารจัดหาได้ / CLOSED : ใบแจ้งถูกนำไปสร้างเป็นเอกสารจัดหารแล้ว / DELETED : ใบแจ้งถูกลบไม่สามารถนำไปดำเนินการต่อได้" />
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:dataTable
					id="tablePRList"
					value="#{purchasingPR.prList}" 
					var="pr"  
					rows="20" 
					rowKeyVar="rowNo"
					onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
					onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
					style="width:100%"				
					>
					<a4j:support event="onRowClick" action="#{purchasingPR.prTableRowClicked}" reRender="controlPanel, headerPanel, itemPanel">
						<f:setPropertyActionListener value="#{pr}" target="#{purchasingPR.editPR}" />
					</a4j:support>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="เลยที่ใบแจ้งจัดหา"/></f:facet>
						<h:outputText value="#{pr.prNumber}">
							<f:convertNumber  pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="วันที่สร้าง"/></f:facet>
						<h:outputText value="#{pr.postingDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="100px">
						<f:facet name="header"><h:outputText value="เหตุผล"/></f:facet>
						<h:outputText value="#{pr.reason}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right" >
						<f:facet name="header"><h:outputText value="ราคารวม"/></f:facet>
						<h:outputText value="#{pr.totalPrice}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="50px">
						<f:facet name="header"><h:outputText value="สถานะ"/></f:facet>
						<h:outputText value="#{pr.status}">
						</h:outputText>
					</rich:column>
					<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>
				</rich:dataTable>
				</rich:panel>
				</a4j:region>
			</rich:tab>
			<rich:tab label="แก้ไขใบแจ้งจัดหา" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
				<rich:panel id="controlPanel" style="width:100%">
					<a4j:commandButton action="#{purchasingPR.newPR}" value="เพิ่มรายการใหม่" reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingPR.prCreatable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.savePR}" value="บันทึกใบแจ้งจัดหา"  reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingPR.prEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.deletePR}" value="ยกเลิกใบแจ้งจัดหา" reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingPR.prDeletable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.printPR}" value="พิมพ์ใบแจ้งจัดหา" reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingPR.prPrintable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
				</rich:panel>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="headerPanel" style="width:100%">
					<f:facet name="header">
						<h:outputText value="Header" />
					</f:facet>
					<h:outputText value="เลขที่ใบแจ้งจัดหา" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPR.editPR.prNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<h:outputText value="วันที่สร้าง" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPR.editPR.postingDate}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
					<h:outputText value="สถานะ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPR.editPR.status}" style="float:left; margin-right:5px;font-weight:bold">
					</h:outputText>
					<h:outputText value="จำนวนเงินรวม" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingPR.editPR.totalPrice}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<div id="wrapper"></div>
					<h:outputText value="เหตุผลและความจำเป็น" styleClass="label medium"/>
					<h:inputTextarea value="#{purchasingPR.editPR.reason}" label="เหตุผลและความจำเป็น" styleClass="input" style="width:70%" rows="3" required="true" >
						<f:validateLength maximum="255"/>
					</h:inputTextarea>
					<div id="wrapper"></div>					
					<h:outputText value="เลขที่เอกสารขออนุมัติ" styleClass="label medium"/>
					<h:inputText value="#{purchasingPR.editPR.referenceDocNumber}" label="เเลขที่เอกสารขออนุมัติ" styleClass="input medium" required="true">
						<f:validateLength maximum="50"/>
					</h:inputText>
					<div id="wrapper"></div>					
					<h:outputText value="ชื่อผู้แจ้งจัดหา" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPR.editPR.requisitionerName}" label="ชื่อผู้แจ้งจัดหา" suggestionValues="#{purchasingPR.requisitionerNameList}"  styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="ตำแหน่งผู้แจ้งจัดหา" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPR.editPR.requisitionerPos}" label="ตำแหน่งผู้แจ้งจัดหา" suggestionValues="#{purchasingPR.requisitionerPosList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="ชื่อผู้ตรวสอบ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPR.editPR.inspectorName}" label="ชื่อผู้ตรวจสอบ" suggestionValues="#{purchasingPR.inspectorNameList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="ตำแหน่งผู้ตรวจสอบ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPR.editPR.inspectorPos}" label="ตำแหน่งผู้ตรวจสอบ" suggestionValues="#{purchasingPR.inspectorPosList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="ชื่อผู้อนุมัติ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPR.editPR.approverName}" label="ชื่อผู้อนุมัติ" suggestionValues="#{purchasingPR.approverNameList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="ตำแหน่งอนุมัติ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingPR.editPR.approverPos}" label="ตำแหน่งอนุมัติ" suggestionValues="#{purchasingPR.approverPosList}" styleClass="input medium" style="float:left" required="true">
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
				</rich:panel>
				</a4j:region>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<a4j:region renderRegionOnly="false">
				<rich:panel id="itemPanel" style="width:100%">
					<f:facet name="header">
						<h:outputText value="PR Item" />
					</f:facet>
					<a4j:region renderRegionOnly="false">
					<rich:dataTable
						id="tablePRItemList"
						value="#{purchasingPR.prItemList}" 
						var="prItem"  
						rows="20" 
						rowKeyVar="rowNo"
						onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
						onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
						style="width:100%"				
						>
						<a4j:support event="onRowClick" action="#{purchasingPR.prItemTableRowClicked}" reRender="itemEditPanel">
							<f:setPropertyActionListener value="#{prItem}" target="#{purchasingPR.editPRItem}" />
						</a4j:support>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="ชื่อวัสดุ"/></f:facet>
							<h:outputText value="#{prItem.material.description}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="ชื่อพัสดุอื่น"/></f:facet>
							<h:outputText value="#{prItem.otherMaterial}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="จำนวน"/></f:facet>
							<h:outputText value="#{prItem.quantity}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="หน่วย"/></f:facet>
							<h:outputText value="#{prItem.orderUnit}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="ราคาต่อหน่วย"/></f:facet>
							<h:outputText value="#{prItem.unitPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="วันที่ต้องการใช้"/></f:facet>
							<h:outputText value="#{prItem.deliveryDate}">
								<f:convertDateTime pattern="dd/MM/yyyy"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="สถานะ"/></f:facet>
							<h:outputText value="#{prItem.status}">
							</h:outputText>
						</rich:column>
						<f:facet name="footer">
							<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
						</f:facet>
					</rich:dataTable>
					</a4j:region>
					<div id="wrapper"></div>
					<rich:spacer height="5px"/>
					<rich:panel id="itemEditPanel" style="width:100%">
						<h:outputText value="กลุ่มวัสดุ" styleClass="label medium" />
						<h:selectOneMenu label="กลุ่มวัสดุ" value="#{purchasingPR.selectedMaterialGroupID}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingPR.materialGroupSelectItemList}"/>
							<a4j:support action="#{purchasingPR.materialGroupComboBoxSelected}" event="onchange" reRender="material, otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="วัสดุ" styleClass="label medium" />
						<h:selectOneMenu id="material" label="วัสดุ" value="#{purchasingPR.selectedMaterialID}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingPR.materialSelectItemList}"/>
							<a4j:support action="#{purchasingPR.materialComboBoxSelected}" event="onchange" reRender="otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="ชื่อพัสดุอื่น" styleClass="label medium"/>
						<h:inputText id="otherMaterial" value="#{purchasingPR.editPRItem.otherMaterial}" label="ชื่อพัสดุอื่น" styleClass="input verylong" style="width:50%" required="false">
							<f:validateLength maximum="80"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="วันที่ต้องการใช้งาน" styleClass="label medium"/>
						<rich:calendar value="#{purchasingPR.editPRItem.deliveryDate}" label="วันที่ต้องการใช้งาน" style="float:left" required="true" locale="th" datePattern="dd/MM/yyyy">
						</rich:calendar>
						<div id="wrapper"></div>					
						<h:outputText value="จำนวน" styleClass="label medium"/>
						<h:inputText id="quantity" value="#{purchasingPR.editPRItem.quantity}" label="จำนวน" styleClass="input medium" style="text-align:right" required="true">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="หน่วย" styleClass="label medium"/>
						<rich:comboBox id="orderUnit" value="#{purchasingPR.editPRItem.orderUnit}" label="หน่วย" suggestionValues="#{purchasingPR.orderUnitList}" styleClass="input medium" listStyle="text-align:left" required="true">
							<f:validateLength maximum="25"/>
						</rich:comboBox>
						<div id="wrapper"></div>					
						<h:outputText value="ราคาต่อหน่วย" styleClass="label medium"/>
						<h:inputText id="orderUnitPrice" value="#{purchasingPR.editPRItem.unitPrice}" label="ราคาต่อหน่วย" styleClass="input medium" style="text-align:right" required="true">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="งบประมาณ" styleClass="label medium" />
						<h:selectOneMenu label="งบประมาณ" value="#{purchasingPR.selectedBudgetItemID}" styleClass="input verylong" style="width:50%">
							<f:selectItems value="#{purchasingPR.budgetItemSelectItemList}"/>
							<a4j:support action="#{purchasingPR.budgetItemComboBoxSelected}" event="onchange" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
					<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
					<a4j:commandButton action="#{purchasingPR.newPrItem}" value="เพิ่มรายการใหม่" reRender="itemPanel" disabled ="#{!purchasingPR.prItemCreatable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.savePrItem}" value="บันทึกรายการ" reRender="itemPanel" disabled ="#{!purchasingPR.prItemEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingPR.deletePrItem}" value="ลบรายการ" reRender="itemPanel" disabled ="#{!purchasingPR.prItemDeletable }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					</rich:panel>
				</rich:panel>
				</a4j:region>			
			</rich:tab>
		</rich:tabPanel>--%>
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
