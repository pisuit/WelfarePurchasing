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
	<title><h:outputText value="Welfare System : Vendor " /></title>
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
		<h:outputText value="คุณ#{purchasingVendor.welcomeMsg}" style="color:blue" rendered="#{purchasingVendor.welcomeMsg != null}"/>
		</div>
		</rich:panel>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="listPanel" style="float:left;width:100%">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="รายชื่อร้านค้า" />
				</h:panelGroup>
			</f:facet>
			<rich:dataTable
				id="tableVendorList"
				value="#{purchasingVendor.venderList}" 
				var="vendor"  
				rows="20" 
				rowKeyVar="rowNo"
				style="width:100%"		
				rowClasses="odd-row, even-row"						
				>
				<a4j:support event="onRowClick" action="#{purchasingVendor.vendorTableRowClicked}" reRender="editPanel" status="showload">
					<f:setPropertyActionListener value="#{vendor}" target="#{purchasingVendor.editVendor}" />
				</a4j:support>
				<rich:column style="text-align:right"  width="50px" sortBy="#{vendor.vendorNumber}">
					<f:facet name="header"><h:outputText value="เลยที่"/></f:facet>
					<h:outputText value="#{vendor.vendorNumber}">
						<f:convertNumber  pattern="0000"/>
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" sortBy="#{vendor.name}">
					<f:facet name="header"><h:outputText value="ชื่อร้านค้า"/></f:facet>
					<h:outputText value="#{vendor.name}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" width="50px">
					<f:facet name="header"><h:outputText value="เลขที่ผู้เสียภาษี"/></f:facet>
					<h:outputText value="#{vendor.taxID}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" width="100px">
					<f:facet name="header"><h:outputText value="ที่อยู่เลขที่"/></f:facet>
					<h:outputText value="#{vendor.address1}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" width="75px">
					<f:facet name="header"><h:outputText value="ที่อยู่ 1"/></f:facet>
					<h:outputText value="#{vendor.address2}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:left" width="75px">
					<f:facet name="header"><h:outputText value="ที่อยู่ 2"/></f:facet>
					<h:outputText value="#{vendor.address3}">
					</h:outputText>
				</rich:column>
				<rich:column style="text-align:center" width="100px" sortBy="#{vendor.status}">
					<f:facet name="header"><h:outputText value="สถานะ"/></f:facet>
					<h:outputText value="#{vendor.status}">
					</h:outputText>
				</rich:column>
				<f:facet name="footer">
					<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
				</f:facet>
			</rich:dataTable>
		</rich:panel>
		</a4j:region>
		<div id="wrapper"></div>
		<rich:spacer height="5"/>
		<a4j:region renderRegionOnly="false">
		<rich:panel id="editPanel" style="float:left;width:100%">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="แก้ไขข้อมูลร้านค้า" />
				</h:panelGroup>
			</f:facet>
			<h:outputText value="ชื่อร้านค้า" styleClass="label medium" />
			<h:inputText label="ชื่อร้านค้า" value="#{purchasingVendor.editVendor.name}" styleClass="input medium" style="width:50%" required="true" maxlength="50">
				<f:validateLength maximum="50"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="เลขที่ผู้เสียภาษี" styleClass="label medium" />
			<h:inputText label="เลขที่ผู้เสียภาษี" value="#{purchasingVendor.editVendor.taxID}" styleClass="input medium" maxlength="15">
				<f:validateLength maximum="15"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="ที่อยู่เลขที่" styleClass="label medium" />
			<h:inputText label="ที่อยู่เลขที่" value="#{purchasingVendor.editVendor.address1}" styleClass="input medium" style="width:50%" maxlength="50" >
				<f:validateLength maximum="50"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="ที่อยู่ 1" styleClass="label medium" />
			<h:inputText label="ที่อยู่ 1" value="#{purchasingVendor.editVendor.address2}" styleClass="input medium" style="width:50%" maxlength="25">
				<f:validateLength maximum="25"/>
			</h:inputText>
			<div id="wrapper"></div>
			<h:outputText value="ที่อยู่ 2" styleClass="label medium" />
			<h:inputText label="ที่อยู่ 2" value="#{purchasingVendor.editVendor.address3}" styleClass="input medium" style="width:50%" maxlength="25">
				<f:validateLength maximum="25"/>
			</h:inputText>
			<div id="wrapper"></div>
			<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />
			<a4j:commandButton action="#{purchasingVendor.newVendor}" value="เพิ่มรายการใหม่" reRender="editPanel" disabled ="#{!purchasingVendor.vendorNewable}" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{purchasingVendor.saveVendor}" value="บันทึกรายการ" reRender="listPanel,editPanel" disabled ="#{!purchasingVendor.vendorEditable }" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
			<a4j:commandButton action="#{purchasingVendor.deleteVendor}" value="ลบรายการ" reRender="listPanel,editPanel" disabled ="#{!purchasingVendor.vendorDeletable }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload">
			</a4j:commandButton>
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
