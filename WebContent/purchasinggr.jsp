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
		<h:outputText value="ยินดีต้อนรับ : "/>
		<h:outputText value="คุณ#{purchasingGR.welcomeMsg}" style="color:blue" rendered="#{purchasingGR.welcomeMsg != null}"/>
		</div>
		</rich:panel>
		<a4j:region renderRegionOnly="false">
				<rich:panel id="controlPanel" style="width:100%">
					<f:facet name="header"><h:outputText value="ใบรับวัสดุ" /> </f:facet>
					<h:outputText value="ประเภท" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingGR.selectedGRType}" style="float:left; margin-right:5px; width:150px" >
						<f:selectItems value="#{purchasingGR.grTypeSelectItemList}"/>
						<a4j:support action="#{purchasingGR.grTypeComboBoxSelected}" event="onchange" reRender="controlPanel, headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<h:outputText value="หมายเลขใบจัดซื้อ" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingGR.selectedPoID}" style="float:left; margin-right:5px; width:100px" disabled="#{purchasingGR.grNoPo}">
						<f:selectItems value="#{purchasingGR.poSelectItemList}"/>
						<a4j:support action="#{purchasingGR.poComboBoxSelected}" event="onchange" reRender="controlPanel, headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
					</h:selectOneMenu>
					<h:outputText value="งบประมาณ" style="float:left; margin-right:5px" />
					<h:selectOneMenu label="งบประมาณ" value="#{purchasingGR.selectedBudgetItemID}" styleClass="input long" disabled="#{!purchasingGR.grNoPo}">
						<f:selectItems value="#{purchasingGR.budgetItemSelectItemList}"/>
						<a4j:support action="#{purchasingGR.budgetItemComboBoxSelected}" event="onchange" ajaxSingle="true" reRender="availableamount"/>
					</h:selectOneMenu>
					<h:panelGroup id="availableamount">
						<h:outputText value="จำนวนเงินที่เหลือ " style="margin-right:3px;color:blue" rendered="#{purchasingGR.selectedBudgetItem != null}"/>
						<h:outputText value="#{purchasingGR.selectedBudgetItem.availableAmount}" rendered="#{purchasingGR.selectedBudgetItem != null}" style="color:blue">
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</h:panelGroup>
					<div id="wrapper"></div>
					<rich:panel>
						<a4j:commandButton action="#{purchasingGR.newGR}" value="เพิ่มรายการใหม่" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingGR.grCreatable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
						</a4j:commandButton>
						<a4j:commandButton action="#{purchasingGR.saveGR}" value="บันทึกใบรับ"  reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingGR.grSavable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
						</a4j:commandButton>
						<a4j:commandButton action="#{purchasingGR.deleteGR}" value="ยกเลิกใบรับ" reRender="controlPanel, headerPanel, itemPanel,listPanel"  disabled ="#{!purchasingGR.grDeletable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
						</a4j:commandButton>
						<a4j:commandButton action="#{purchasingGR.closeGR}" value="ปิดใบรับ" reRender="controlPanel, headerPanel, itemPanel,listPanel"  disabled ="#{!purchasingGR.grClosable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
						</a4j:commandButton>
						<h:commandLink action="#{purchasingGR.printGR}" value="พิมพ์ใบรับวัสดุ" immediate="true" target="_blank" rendered="#{purchasingGR.grPrintable}"/>
						<a4j:commandButton id="link" value="แสดงรายการใบรับวัสดุ" ajaxSingle="true" disabled ="false"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" style="float:right">
						<rich:componentControl for="modalPanel" attachTo="link" operation="show" event="onclick"/>
					</a4j:commandButton>
					</rich:panel>
				</rich:panel>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="headerPanel" style="width:100%">
					<f:facet name="header"><h:outputText value="ส่วนหัว" /></f:facet>
					<h:outputText value="เลขที่ใบรับ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.grNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<h:outputText value="วันที่บันทึก" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.postingDate}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
					<h:outputText value="สถานะ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.status}" style="float:left; margin-right:5px;font-weight:bold">
					</h:outputText>
					<h:outputText value="จำนวนเงินรวม" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.totalPrice}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<h:outputText value="เลขที่ใบสั่งซื้อ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.purchaseOrder.poNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<div id="wrapper"></div>
					<h:outputText value="วันที่รับวัสดุ" styleClass="label medium"/>
					<rich:calendar value="#{purchasingGR.editGR.receivedDate}" label="วันที่รับวัสดุ" style="float:left"  locale="th" datePattern="dd/MM/yyyy">
					</rich:calendar>
					<div id="wrapper"></div>					
					<h:outputText value="ใบกำกับสินค้า/ภาษี" styleClass="label medium"/>
					<h:inputText id="invoiceNumber" value="#{purchasingGR.editGR.invoiceNumber}" label="ใบกำกับสินค้า/ภาษี" styleClass="input medium" required="false">
					</h:inputText>
					<div id="wrapper"></div>
					<h:outputText value="ส่วนลดรวม" styleClass="label medium"/>
					<h:inputText id="totalDiscountAmount" value="#{purchasingGR.editGR.totalDiscountAmount}" label="ส่วนลดรวม" styleClass="input medium" required="false">	
					</h:inputText>
					<a4j:commandButton action="#{purchasingGR.distributeDiscount}" value="กระจายส่วนลด" reRender="controlPanel, headerPanel, itemPanel,listPanel, warningText" status="showload"/>
					<div id="wrapper"></div>					
					<h:outputText value="เหตุผลและความจำเป็น" styleClass="label medium"/>
					<h:inputTextarea value="#{purchasingGR.editGR.reason}" label="เหตุผลและความจำเป็น" styleClass="input" style="width:70%" rows="3"  >
						<f:validateLength maximum="255"/>
					</h:inputTextarea>
					<div id="wrapper"></div>					
					<h:outputText value="ชื่อผู้รับวัสดุ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.recipientName}" label="ชื่อผู้รับวัสดุ" suggestionValues="#{purchasingGR.recipientNameList}"  styleClass="input medium" style="float:left" >
						<f:validateLength maximum="50"/>
					</rich:comboBox>										
					<h:outputText value="ตำแหน่งผู้รับวัสดุ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.recipientPos}" label="ตำแหน่งผู้รับวัสดุ" suggestionValues="#{purchasingGR.recipientPosList}" styleClass="input medium" style="float:left" >
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="ชื่อผู้บันทึก" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.entryName}" label="ชื่อผู้บันทึก" suggestionValues="#{purchasingGR.entryNameList}"  styleClass="input medium" style="float:left" >
						<f:validateLength maximum="50"/>
					</rich:comboBox>
					<h:outputText value="ตำแหน่งผู้บันทึก" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.entryPos}" label="ตำแหน่งผู้บันทึก" suggestionValues="#{purchasingGR.entryPosList}" styleClass="input medium" style="float:left" >
						<f:validateLength maximum="50"/>
					</rich:comboBox>					
					<div id="wrapper"></div>			
				</rich:panel>
				</a4j:region>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="itemPanel">
					<f:facet name="header"><h:outputText value="รายการวัสดุ" /></f:facet>
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
							<f:facet name="header"><h:outputText value="ชื่อวัสดุ"/></f:facet>
							<h:outputText value="#{grItem.material.description}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="ชื่อพัสดุอื่น"/></f:facet>
							<h:outputText value="#{grItem.otherMaterial}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="70px">
							<f:facet name="header"><h:outputText value="จำนวนที่รับ"/></f:facet>
							<h:outputText value="#{grItem.receivedQty}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="70px">
							<f:facet name="header"><h:outputText value="จำนวนที่สั่งซื้อ"/></f:facet>
							<h:outputText value="#{grItem.orderQty}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" width="80px">
							<f:facet name="header"><h:outputText value="หน่วย"/></f:facet>
							<h:outputText value="#{grItem.receiveUnit}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="100px">
							<f:facet name="header"><h:outputText value="ราคาต่อหน่วยที่รับ"/></f:facet>
							<h:outputText value="#{grItem.unitPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="ราคาต่อหน่วยที่สั่งซื้อ"/></f:facet>
							<h:outputText value="#{grItem.orderUnitPrice}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" >
							<f:facet name="header"><h:outputText value="ส่วนลด"/></f:facet>
							<h:outputText value="#{grItem.discountAmount}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" >
							<f:facet name="header"><h:outputText value="ราคาสุทธิ"/></f:facet>
							<h:outputText value="#{grItem.netPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="สถานะ"/></f:facet>
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
					<rich:panel id="pricePanel" header="ข้อมูลเพิ่มเติม">
						<h:outputText value="ราคารวมก่อนหักส่วนลด:" styleClass="label custom"/>
						<h:outputText value="#{purchasingGR.editGRItem.totalprice}" >
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
						<div id="wrapper"></div>
						<h:outputText value="ส่วนลด:" styleClass="label custom"/>
						<h:outputText value="#{purchasingGR.editGRItem.discountAmount}" >
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
						<div id="wrapper"></div>
						<h:outputText value="ราคาสุทธิ:" styleClass="label custom"/>
						<h:outputText value="#{purchasingGR.editGRItem.netPrice}" >
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
						<div id="wrapper"></div>
						<h:outputText value="ราคาเฉลี่ยต่อหน่วย:" styleClass="label custom"/>
						<h:outputText value="#{purchasingGR.editGRItem.avgPrice}" >
							<f:convertNumber pattern="#,##0.00"/>
						</h:outputText>
					</rich:panel>
					</div>
					<div style="float:left;width:70%">
						<h:outputText value="กลุ่มวัสดุ" styleClass="label medium" />
							<h:selectOneMenu label="กลุ่มวัสดุ" value="#{purchasingGR.selectedMaterialGroupID}" disabled="#{!purchasingGR.grNoPo}" styleClass="input medium" style="width:70%">
							<f:selectItems value="#{purchasingGR.materialGroupSelectItemList}"/>
							<a4j:support action="#{purchasingGR.materialGroupComboBoxSelected}" event="onchange" reRender="material, otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="วัสดุ" styleClass="label medium" />
							<h:selectOneMenu id="material" label="วัสดุ" value="#{purchasingGR.selectedMaterialID}" disabled="#{!purchasingGR.grNoPo}" styleClass="input medium" style="width:70%" >
							<f:selectItems value="#{purchasingGR.materialSelectItemList}"/>
							<a4j:support action="#{purchasingGR.materialComboBoxSelected}" event="onchange" reRender="otherMaterial, receiveUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="ชื่อพัสดุอื่น" styleClass="label medium"/>
						<h:inputText id="otherMaterial" value="#{purchasingGR.editGRItem.otherMaterial}" label="ชื่อพัสดุอื่น" disabled="#{!purchasingGR.grNoPo}" styleClass="input verylong" style="width:70%" required="false">							
							<f:validateLength maximum="80"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="จำนวนที่สั่งซื้อ" styleClass="label medium"/>
						<h:inputText id="orderQty" value="#{purchasingGR.editGRItem.orderQty}" label="จำนวนที่สั่งซื้อ" disabled="true" styleClass="input medium" style="text-align:right" >							
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="จำนวนที่รับ" styleClass="label medium"/>
						<h:inputText id="receivedQty" value="#{purchasingGR.editGRItem.receivedQty}" label="จำนวนที่รับ" styleClass="input medium" style="text-align:right"  disabled="false">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>				
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="หน่วย" styleClass="label medium"/>
						<rich:comboBox id="receiveUnit" value="#{purchasingGR.editGRItem.receiveUnit}" label="หน่วย" suggestionValues="#{purchasingGR.orderUnitList}"  styleClass="input medium" style="float:left" disabled="#{!purchasingGR.grNoPo}" >
							<f:validateLength maximum="25"/>
						</rich:comboBox>
						<div id="wrapper"></div>					
						<h:outputText value="ราคาต่อหน่วย" styleClass="label medium"/>
						<h:inputText id="orderUnitPrice" value="#{purchasingGR.editGRItem.unitPrice}" label="ราคาต่อหน่วย" styleClass="input medium" style="text-align:right"  disabled="false">
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>						
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="ส่วนลด" styleClass="label medium"/>
						<h:inputText id="discountAmount" value="#{purchasingGR.editGRItem.discountAmount}" label="ส่วนลด" styleClass="input medium" style="text-align:right" required="false" disabled="#{!purchasingGR.itemDiscountable}">
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>
						<h:outputText value="สถานที่จัดเก็บ" styleClass="label medium" />
						<h:selectOneMenu id="storageLocation" label="สถานที่จัดเก็บ" value="#{purchasingGR.storageLocation}" disabled="true" styleClass="input medium" style="width:70%">
							<f:selectItems value="#{purchasingGR.storageLocationSelectItemList}"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>					
						<h:outputText value="งบประมาณ" styleClass="label medium" />
						<h:outputText value="#{purchasingGR.editGRItem.budgetItem.category}" styleClass="input verylong" style="width:50%">
						</h:outputText>
						</div>
						<div id="wrapper"></div>
					<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
					<a4j:commandButton action="#{purchasingGR.newGRItem}" value="เพิ่มรายการใหม่" reRender="itemPanel" disabled ="#{!purchasingGR.grItemNewable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.saveGRItem}" value="บันทึกรายการ" reRender="itemPanel" disabled ="#{!purchasingGR.grItemEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.deleteGRItem}" value="ลบรายการ" reRender="itemPanel" disabled ="#{!purchasingGR.grItemDeletable }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
					</a4j:commandButton>
					</rich:panel>
					</a4j:region>
					</rich:panel>
					<rich:modalPanel id="modalPanel" height="700" width="800" resizeable="true" >
					<f:facet name="header">					
						<h:outputText value="รายการใบรับวัสดุ" />					
					</f:facet>
					<f:facet name="controls">
						<h:panelGroup>
							<h:graphicImage value="/assets/icons/close.png" id="hideTable" style="cursor:pointer" />
							<rich:componentControl for="modalPanel" attachTo="hideTable" operation="hide" event="onclick" />
						</h:panelGroup>
					</f:facet>
					<a4j:region renderRegionOnly="false">
				<rich:panel id="listPanel" style="width:100%">
				<h:outputText value="GR-FULL : ใบรับพัสดุนี้เป็นการรับพัสดุแบบเต็มจำนวน    /    GR-PAR : ใบรับพัสดุนี้เป็นการรับพัสดุแบบบางส่วน  " />
				<div id="wrapper"></div>
				<h:outputText value="DELETED : ใบรับพัสดุนี้ถูกยกเลิก    /    CLOSED : ใบรับพัสดุถูกปิดและไม่สามารถทำการยกเลิก " />
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
						<f:facet name="header"><h:outputText value="เลขที่"/></f:facet>
						<h:outputText value="#{gr.grNumber}">
							<f:convertNumber  pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="วันที่รับวัสดุ"/></f:facet>
						<h:outputText value="#{gr.receivedDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="วันที่บันทึก"/></f:facet>
						<h:outputText value="#{gr.postingDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="75px">
						<f:facet name="header"><h:outputText value="เลขที่ใบกำกับ"/></f:facet>
						<h:outputText value="#{gr.invoiceNumber}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" >
						<f:facet name="header"><h:outputText value="เหตุผล"/></f:facet>
						<h:outputText value="#{gr.reason}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:center" width="75px">
						<f:facet name="header"><h:outputText value="เลขที่ใบสั่งซื้อ"/></f:facet>
						<h:outputText value="#{gr.purchaseOrder.poNumber}">
							<f:convertNumber pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="50px">
						<f:facet name="header"><h:outputText value="สถานะ"/></f:facet>
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
			<rich:tab label="รายการใบรับวัสดุ" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
				<rich:panel id="listPanel" style="width:100%">
				<h:outputText value="GR-FULL : ใบรับพัสดุนี้เป็นการรับพัสดุแบบเต็มจำนวน    /    GR-PAR : ใบรับพัสดุนี้เป็นการรับพัสดุแบบบางส่วน  " />
				<div id="wrapper"></div>
				<h:outputText value="CANCELLED : ใบรับพัสดุนี้ถูกยกเลิก    /    CLOSED : ใบรับพัสดุถูกปิดและไม่สามารถทำการยกเลิก " />
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
						<f:facet name="header"><h:outputText value="เลขที่"/></f:facet>
						<h:outputText value="#{gr.grNumber}">
							<f:convertNumber  pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="วันที่รับวัสดุ"/></f:facet>
						<h:outputText value="#{gr.receivedDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="วันที่บันทึก"/></f:facet>
						<h:outputText value="#{gr.postingDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="75px">
						<f:facet name="header"><h:outputText value="เลขที่ใบกำกับ"/></f:facet>
						<h:outputText value="#{gr.invoiceNumber}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" >
						<f:facet name="header"><h:outputText value="เหตุผล"/></f:facet>
						<h:outputText value="#{gr.reason}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:center" width="75px">
						<f:facet name="header"><h:outputText value="เลขที่ใบสั่งซื้อ"/></f:facet>
						<h:outputText value="#{gr.purchaseOrder.poNumber}">
							<f:convertNumber pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:left" width="50px">
						<f:facet name="header"><h:outputText value="สถานะ"/></f:facet>
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
			<rich:tab label="แก้ไขใบรับวัสดุ" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
				<rich:panel id="controlPanel" style="width:100%">
					<h:outputText value="ประเภทการรับ" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingGR.selectedGRType}" style="float:left; margin-right:5px; width:150px" >
						<f:selectItems value="#{purchasingGR.grTypeSelectItemList}"/>
						<a4j:support action="#{purchasingGR.grTypeComboBoxSelected}" event="onchange" reRender="controlPanel, headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<h:outputText value="หมายเลขใบจัดซื้อ" style="float:left; margin-right:5px"/>
					<h:selectOneMenu value="#{purchasingGR.selectedPoID}" style="float:left; margin-right:5px; width:100px" >
						<f:selectItems value="#{purchasingGR.poSelectItemList}"/>
						<a4j:support action="#{purchasingGR.poComboBoxSelected}" event="onchange" reRender="controlPanel, headerPanel, itemPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<a4j:commandButton action="#{purchasingGR.newGR}" value="เพิ่มรายการใหม่" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingGR.grCreatable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.saveGR}" value="บันทึกใบรับ"  reRender="controlPanel, headerPanel, itemPanel,listPanel" disabled ="#{!purchasingGR.grSavable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.deleteGR}" value="ยกเลิกใบรับ" reRender="controlPanel, headerPanel, itemPanel,listPanel"  disabled ="#{!purchasingGR.grDeletable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.closeGR}" value="ปิดใบรับ" reRender="controlPanel, headerPanel, itemPanel,listPanel"  disabled ="#{!purchasingGR.grClosable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.printGR}" value="พิมพ์ใบรับ" reRender="controlPanel, headerPanel, itemPanel,listPanel" ajaxSingle="true" disabled ="#{!purchasingGR.grPrintable}"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
				</rich:panel>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<rich:panel id="headerPanel" style="width:100%">
					<f:facet name="header"><h:outputText value="Header" /></f:facet>
					<h:outputText value="เลขที่ใบรับ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.grNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<h:outputText value="วันที่บันทึก" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.postingDate}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertDateTime pattern="dd/MM/yyyy"/>
					</h:outputText>
					<h:outputText value="สถานะ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.status}" style="float:left; margin-right:5px;font-weight:bold">
					</h:outputText>
					<h:outputText value="จำนวนเงินรวม" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.totalPrice}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<h:outputText value="เลขที่ใบสั่งซื้อ" style="float:left; margin-right:5px"/>
					<h:outputText value="#{purchasingGR.editGR.purchaseOrder.poNumber}" style="float:left; margin-right:5px;font-weight:bold">
						<f:convertNumber pattern="00000000"/>
					</h:outputText>
					<div id="wrapper"></div>
					<h:outputText value="วันที่รับวัสดุ" styleClass="label medium"/>
					<rich:calendar value="#{purchasingGR.editGR.receivedDate}" label="วันที่รับวัสดุ" style="float:left"  locale="th" datePattern="dd/MM/yyyy">
					</rich:calendar>
					<div id="wrapper"></div>					
					<h:outputText value="ใบกำกับสินค้า/ภาษี" styleClass="label medium"/>
					<h:inputText id="invoiceNumber" value="#{purchasingGR.editGR.invoiceNumber}" label="ใบกำกับสินค้า/ภาษี" styleClass="input medium" required="false">
					</h:inputText>
					<div id="wrapper"></div>					
					<h:outputText value="เหตุผลและความจำเป็น" styleClass="label medium"/>
					<h:inputTextarea value="#{purchasingGR.editGR.reason}" label="เหตุผลและความจำเป็น" styleClass="input" style="width:70%" rows="3"  >
						<f:validateLength maximum="255"/>
					</h:inputTextarea>
					<div id="wrapper"></div>					
					<h:outputText value="ชื่อผู้รับวัสดุ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.recipientName}" label="ชื่อผู้รับวัสดุ" suggestionValues="#{purchasingGR.recipientNameList}"  styleClass="input medium" style="float:left" >
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="ตำแหน่งผู้รับวัสดุ" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.recipientPos}" label="ตำแหน่งผู้รับวัสดุ" suggestionValues="#{purchasingGR.recipientPosList}" styleClass="input medium" style="float:left" >
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<div id="wrapper"></div>					
					<h:outputText value="ชื่อผู้บันทึก" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.entryName}" label="ชื่อผู้บันทึก" suggestionValues="#{purchasingGR.entryNameList}"  styleClass="input medium" style="float:left" >
						<f:validateLength maximum="25"/>
					</rich:comboBox>
					<h:outputText value="ตำแหน่งผู้บันทึก" styleClass="label medium"/>
					<rich:comboBox value="#{purchasingGR.editGR.entryPos}" label="ตำแหน่งผู้บันทึก" suggestionValues="#{purchasingGR.entryPosList}" styleClass="input medium" style="float:left" >
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
							<f:facet name="header"><h:outputText value="ชื่อวัสดุ"/></f:facet>
							<h:outputText value="#{grItem.material.description}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="ชื่อพัสดุอื่น"/></f:facet>
							<h:outputText value="#{grItem.otherMaterial}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="จำนวนที่รับ"/></f:facet>
							<h:outputText value="#{grItem.receivedQty}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="จำนวนที่สั่งซื้อ"/></f:facet>
							<h:outputText value="#{grItem.orderQty}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="หน่วย"/></f:facet>
							<h:outputText value="#{grItem.receiveUnit}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="100px">
							<f:facet name="header"><h:outputText value="ราคาต่อหน่วย"/></f:facet>
							<h:outputText value="#{grItem.unitPrice}">
								<f:convertNumber  pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="สถานที่จัดเก็บ"/></f:facet>
							<h:outputText value="#{grItem.storageLocation}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right"  width="50px">
							<f:facet name="header"><h:outputText value="สถานะ"/></f:facet>
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
						<h:outputText value="กลุ่มวัสดุ" styleClass="label medium" />
						<h:selectOneMenu label="กลุ่มวัสดุ" value="#{purchasingGR.selectedMaterialGroupID}" disabled="#{purchasingGR.editGRItem.id != null}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingGR.materialGroupSelectItemList}"/>
							<a4j:support action="#{purchasingGR.materialGroupComboBoxSelected}" event="onchange" reRender="material, otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="วัสดุ" styleClass="label medium" />
						<h:selectOneMenu id="material" label="วัสดุ" value="#{purchasingGR.selectedMaterialID}" disabled="#{purchasingGR.editGRItem.id != null}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingGR.materialSelectItemList}"/>
							<a4j:support action="#{purchasingGR.materialComboBoxSelected}" event="onchange" reRender="otherMaterial, orderUnit, orderUnitPrice" ajaxSingle="true"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>
						<h:outputText value="ชื่อพัสดุอื่น" styleClass="label medium"/>
						<h:inputText id="otherMaterial" value="#{purchasingGR.editGRItem.otherMaterial}" label="ชื่อพัสดุอื่น" disabled="#{purchasingGR.editGRItem.id != null}" styleClass="input verylong" style="width:50%" required="false">
							<f:validateLength maximum="80"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="จำนวนที่สั่งซื้อ" styleClass="label medium"/>
						<h:inputText id="orderQty" value="#{purchasingGR.editGRItem.orderQty}" label="จำนวนที่สั่งซื้อ" disabled="#{purchasingGR.editGRItem.id != null}" styleClass="input medium" style="text-align:right" >
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="จำนวนที่รับ" styleClass="label medium"/>
						<h:inputText id="receivedQty" value="#{purchasingGR.editGRItem.receivedQty}" label="จำนวนที่รับ" styleClass="input medium" style="text-align:right" >
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>					
						<h:outputText value="หน่วย" styleClass="label medium"/>
						<rich:comboBox value="#{purchasingGR.editGRItem.receiveUnit}" label="หน่วย" suggestionValues="#{purchasingGR.orderUnitList}"  styleClass="input medium" style="float:left" disabled="#{purchasingGR.editGRItem != null && purchasingGR.editGRItem.material != null }" >
							<f:validateLength maximum="25"/>
						</rich:comboBox>
						<div id="wrapper"></div>					
						<h:outputText value="ราคาต่อหน่วย" styleClass="label medium"/>
						<h:inputText id="orderUnitPrice" value="#{purchasingGR.editGRItem.unitPrice}" label="ราคาต่อหน่วย" styleClass="input medium" style="text-align:right" >
							<f:validateDoubleRange minimum="1"/>
							<f:convertNumber pattern="#,##0.00"/>
						</h:inputText>
						<div id="wrapper"></div>
						<h:outputText value="สถานที่จัดเก็บ" styleClass="label medium" />
						<h:selectOneMenu id="storageLocation" label="สถานที่จัดเก็บ" value="#{purchasingGR.storageLocation}" styleClass="input medium" style="width:50%">
							<f:selectItems value="#{purchasingGR.storageLocationSelectItemList}"/>
						</h:selectOneMenu>
						<div id="wrapper"></div>					
						<h:outputText value="งบประมาณ" styleClass="label medium" />
						<h:outputText value="#{purchasingGR.editGRItem.budgetItem.category}" styleClass="input verylong" style="width:50%">
						</h:outputText>
						<div id="wrapper"></div>
					<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
					<a4j:commandButton action="#{purchasingGR.newGRItem}" value="เพิ่มรายการใหม่" reRender="itemPanel" disabled ="#{!purchasingGR.grItemNewable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.saveGRItem}" value="บันทึกรายการ" reRender="itemPanel" disabled ="#{!purchasingGR.grItemEditable}" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
					<a4j:commandButton action="#{purchasingGR.deleteGRItem}" value="ลบรายการ" reRender="itemPanel" disabled ="#{!purchasingGR.grItemDeletable }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
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
