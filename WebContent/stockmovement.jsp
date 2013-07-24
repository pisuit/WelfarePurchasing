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
	<title><h:outputText value="Welfare System : PageName " /></title>
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
		<rich:tabPanel switchType="ajax">
			<rich:tab label="การเคลื่อนไหวของวัสดุ">
				<rich:panel id="headerPanel">
					<h:outputText value="ระหว่างวันทึ่" styleClass="label medium"/>
					<rich:calendar value="#{purchasingMovement.movementStartDate}" datePattern="dd/MM/yyyy" style="float:left; margin-right:5px"/>
					<div id="wrapper"></div>
					<h:outputText value="ถึงวันทึ่" styleClass="label medium"/>
					<rich:calendar value="#{purchasingMovement.movementEndDate}" datePattern="dd/MM/yyyy" style="float:left; margin-right:5px"/>
					<div id="wrapper"></div>
					<h:outputText value="สถานที่จัดเก็บ" styleClass="label medium"/>
					<h:selectOneMenu value="#{purchasingMovement.selectedStorageLocationID}" style="float:left; margin-right:5px; width:150px" >
						<f:selectItems value="#{purchasingMovement.storageLocationSelectItemList}"/>
						<a4j:support action="#{purchasingMovement.storageLocationComboBoxSelected}" event="onchange" reRender="headerPanel,listPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<div id="wrapper"></div>
					<h:outputText value="กลุ่มวัสดุ" styleClass="label medium"/>
					<h:selectOneMenu value="#{purchasingMovement.selectedMaterialGroupID}" style="float:left; margin-right:5px; width:150px" >
						<f:selectItems value="#{purchasingMovement.materialGroupSelectItemList}"/>
						<a4j:support action="#{purchasingMovement.materialGroupComboBoxSelected}" event="onchange" reRender="headerPanel,listPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
					<div id="wrapper"></div>
					<h:outputText value="วัสดุ" styleClass="label medium"/>
					<h:selectOneMenu value="#{purchasingMovement.selectedMaterialID}" style="float:left; margin-right:5px; width:150px" >
						<f:selectItems value="#{purchasingMovement.materialSelectItemList}"/>
						<a4j:support action="#{purchasingMovement.materialComboBoxSelected}" event="onchange" reRender="headerPanel,listPanel" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');"/>
					</h:selectOneMenu>
				<div id="wrapper"></div>
					<a4j:commandButton action="#{purchasingMovement.searchStockMovement}" value="ค้นหา" reRender="headerPanel,listPanel" ajaxSingle="true" disabled ="false"  oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');">
					</a4j:commandButton>
				<div id="wrapper"></div>
				</rich:panel>
				<div id="wrapper"></div>
				<rich:spacer height="5"/>
				<a4j:region renderRegionOnly="false">
				<rich:panel id="listPanel">
					<f:facet name="header">
						<h:panelGroup>
							<h:outputText value="รายการเคลื่อนไหว" />
						</h:panelGroup>
					</f:facet>
					<h:outputText value="จำนวนคงเหลือ" styleClass="label medium"/>
					<h:outputText value="#{purchasingMovement.availableQty}" styleClass="data medium" style="width:150px">
						<f:convertNumber pattern="#,##0.00"/>
					</h:outputText>
					<div id="wrapper"></div>
					<rich:dataTable
						id="tableStockMovementList"
						value="#{purchasingMovement.stockMovementList}" 
						var="movement"  
						rows="20" s
						rowKeyVar="rowNo"
						style="width:100%"	
						rowClasses="odd-row, even-row"			
						>
						<rich:column style="text-align:left" width="100px">
							<f:facet name="header"><h:outputText value="ชื่อวัสดุ"/></f:facet>
							<h:outputText value="#{movement.material.description}">
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" width="100px">
							<f:facet name="header"><h:outputText value="วันที่"/></f:facet>
							<h:outputText value="#{movement.movementDate}">
								<f:convertDateTime pattern="dd/MM/yyyy"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="100px">
							<f:facet name="header"><h:outputText value="จำนวน"/></f:facet>
							<h:outputText value="#{movement.totalQtyWithSign}">
								<f:convertNumber pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:right" width="100px">
							<f:facet name="header"><h:outputText value="ราคา"/></f:facet>
							<h:outputText value="#{movement.totalPriceWithSign}">
								<f:convertNumber pattern="#,##0.00"/>
							</h:outputText>
						</rich:column>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="หมายเหตุ"/></f:facet>
							<h:outputText value="#{movement.remark}">
							</h:outputText>
						</rich:column>
						<f:facet name="footer">
							<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
						</f:facet>
					</rich:dataTable>
				</rich:panel>
				</a4j:region>
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
