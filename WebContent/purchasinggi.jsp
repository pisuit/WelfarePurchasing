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
			<rich:tab label="รายการใบเบิก" ajaxSingle="true">
				<rich:dataTable
					id="tableGRList"
					value="#{purchasingGI.giList}" 
					var="gi"  
					rows="20" 
					rowKeyVar="rowNo"
					style="width:100%"		
					rowClasses="odd-row, even-row"		
					>
					<a4j:support event="onRowClick" action="#{purchasingGI.giList}" reRender="controlPanel, headerPanel, itemPanel">
						<f:setPropertyActionListener value="#{gi}" target="#{purchasingGI.editGI}" />
					</a4j:support>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="เลขที่"/></f:facet>
						<h:outputText value="#{gi.giNumber}">
							<f:convertNumber  pattern="00000000"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="วันที่เบิก"/></f:facet>
						<h:outputText value="#{gi.issuedDate}">
							<f:convertDateTime pattern="dd/MM/yyyy"/>
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  width="50px">
						<f:facet name="header"><h:outputText value="ผู้เบิก"/></f:facet>
						<h:outputText value="#{gi.issuerName}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  >
						<f:facet name="header"><h:outputText value="เหตุผล"/></f:facet>
						<h:outputText value="#{gi.reason}">
						</h:outputText>
					</rich:column>
					<rich:column style="text-align:right"  >
						<f:facet name="header"><h:outputText value="สถานะ"/></f:facet>
						<h:outputText value="#{gi.status}">
						</h:outputText>
					</rich:column>
					<f:facet name="footer">
						<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
					</f:facet>
				</rich:dataTable>
			</rich:tab>
			<rich:tab ajaxSingle="true" label="แก้ไขใบเบิก">
				<a4j:region renderRegionOnly="false">
				<rich:panel id="controlPanel">
					<h:outputText value="ประเภทการเบิก" style="float:left; margin-right: 5px"/>
					<h:selectOneMenu value="#{purchasingGI.selectedGITypeID}">
						<f:selectItems value="#{purchasingGI.giTypeSelectItemList}"/>
					</h:selectOneMenu>
					<a4j:commandButton action="#{purchasingGI.newGI}" value="เพิ่มรายการใหม่" reRender="controlPanel, headerPanel, itemPanel" />
					<a4j:commandButton action="#{purchasingGI.saveGI}" value="บันทึกใบเบิก" reRender="controlPanel, headerPanel, itemPanel"/>
					<a4j:commandButton action="#{purchasingGI.ca
					ncelGI}" value="ยกเลิกใบเยิก" reRender="controlPanel, headerPanel, itemPanel"/>
					<div id="wrapper"></div>
				</rich:panel>
				<rich:panel>
				</rich:panel>
				</a4j:region>
				<a4j:region renderRegionOnly="false">
				</a4j:region>
				<a4j:region renderRegionOnly="false">
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
