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
		<a4j:region renderRegionOnly="false">
		<rich:panel id="listPanel">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Minimum Stock List" />
				</h:panelGroup>
			</f:facet>
			<rich:dataTable
			id="minimumStockList"
			value=""
			rows="20"
			rowKeyVar="rowNo"
			style="width:100%"
			rowClasses="odd-row, even-row"
			>
				<%--<a4j:support event="onRowClick" action="" reRender="" >
					<f:setPropertyActionListener value="" target="" />
				</a4j:support>--%>
				<rich:column style="text-align:right" width="20px">
					<f:facet name="header"><h:outputText value="ลำดับที่" /></f:facet>
					<h:outputText value="#{rowNo+1}" />
				</rich:column>
				<rich:column style="text-align:left">
					<f:facet name="header"><h:outputText value="ชื่อวัสดุคงคลัง" /></f:facet>
					<h:outputText value="หมูสด" />
				</rich:column>
				<rich:column style="text-align:left" width="100px">
					<f:facet name="header"><h:outputText value="จำนวนคงเหลือ" /></f:facet>
					<h:outputText value="1" />			
				</rich:column>
				<rich:column style="text-align:left" width="100px">
					<f:facet name="header"><h:outputText value="จำนวนต่ำสุด" /></f:facet>
					<h:outputText value="5" />
				</rich:column>
				<rich:column style="text-align:left" width="100px">
					<f:facet name="header"><h:outputText value="หน่วย(เบิก)" /></f:facet>
					<h:outputText value="3" />
				</rich:column>
			</rich:dataTable>
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
