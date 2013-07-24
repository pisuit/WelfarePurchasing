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
	<title><h:outputText value="Welfare Budget System : Administration" /></title>
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
		<h:outputText value="คุณ#{budgetAdmin.welcomeMsg}" style="color:blue" rendered="#{budgetAdmin.welcomeMsg != null}"/>
		</div>
		</rich:panel>
		<rich:tabPanel switchType="ajax" >
		<a4j:support event="ontabchange" status="showload"/>
			<rich:tab label="กำหนดชื่อผู้ใช้และสิทธิเข้าใช้งาน" ajaxSingle="true">
				<a4j:region renderRegionOnly="false">
					<rich:panel id="listPanel" style="width:40%; float:left">
						<f:facet name="header">
							<h:panelGroup>
								<h:outputText value="รายชื่อผู้ใช้งาน" />
							</h:panelGroup>
						</f:facet>
						<rich:dataTable
							id="tableUserList"
							value="#{budgetAdmin.userList}" 
							var="user"  
							rows="20" 
							rowKeyVar="rowNo"
							style="width:100%"
							rowClasses="odd-row, even-row"				
							>
						<a4j:support event="onRowClick" action="#{budgetAdmin.userTableRowClicked}" reRender="editPanel" status="showload">
							<f:setPropertyActionListener value="#{user}" target="#{budgetAdmin.editUser}" />
						</a4j:support>
						<rich:column style="text-align:right"  width="20px">
							<f:facet name="header"><h:outputText value=""/></f:facet>
							<h:outputText value="#{rowNo+1}"/>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="ชื่อผู้ใช้งาน"/></f:facet>
							<h:outputText value="#{user.username}"/>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="ชื่อ - นามสกุล"/></f:facet>
							<h:outputText value="#{user.fullName}"/>
						</rich:column>
						<f:facet name="footer">
							<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
						</f:facet>
					</rich:dataTable>
				</rich:panel>
			</a4j:region>
			<div style="float:left;width:40%;margin-left:5px;">
			<a4j:region renderRegionOnly="false">
				<rich:panel id="editPanel">
					<f:facet name="header">
						<h:panelGroup>
							<h:outputText value="กำหนดชื่อผู้ใช้และสิทธิเข้าใช้งาน" />
						</h:panelGroup>
					</f:facet>
					<h:outputText value="ชื่อผู้ใช้งาน" styleClass="label medium"/>
					<h:inputText value="#{budgetAdmin.editUser.username}" styleClass="input medium" required="true" size="5" requiredMessage="กรุณาใสชื่อผู้ใช้งาน"/>									
					<div id="wrapper"></div>
					<h:outputText value="ชื่อ" styleClass="label medium"/>
					<h:inputText value="#{budgetAdmin.editUser.firstName}" styleClass="input medium" required="true" size="5" requiredMessage="กรุณาใสชื่อผู้ใช้งาน"/>
					<div id="wrapper"></div>
					<h:outputText value="นามสกุล" styleClass="label medium"/>
					<h:inputText value="#{budgetAdmin.editUser.lastName}" styleClass="input medium" required="true" size="5" requiredMessage="กรุณาใสชื่อผู้ใช้งาน"/>
					<div id="wrapper"></div>
					<h:outputText value="สิทธิการใช้งาน" styleClass="label medium"/>
					<h:selectManyCheckbox value="#{budgetAdmin.selectedUserAuthorizationList}" layout="pageDirection" style="border:1px dashed red">
						<f:selectItems value="#{budgetAdmin.authorizationSelectItemList}"/>
					</h:selectManyCheckbox>																		
				<rich:separator height="1" lineType="solid" style="margin-bottom:5px; margin-top:5px" />			
				<a4j:commandButton action="#{budgetAdmin.newUser}" value="เพิ่มผู้ใช้งาน" reRender="listPanel, editPanel" disabled="#{budgetAdmin.editUser.id == null }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
				<a4j:commandButton action="#{budgetAdmin.saveUser}" value="บันทึกผู้ใช้งาน" reRender="listPanel, editPanel" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload" />
				<a4j:commandButton action="#{budgetAdmin.deleteUser}" value="ลบผู้ใช้งาน" reRender="listPanel, editPanel" disabled ="#{budgetAdmin.editUser.id == null }" ajaxSingle="true" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>								
				</rich:panel>
			</a4j:region>		
			</div>	
			</rich:tab>
			<rich:tab label="กำหนดสิทธิการเข้าใช้งบประมาณ" ajaxSingle="true">
					<a4j:region renderRegionOnly="false">
					<rich:panel id="listPanel2" style="width:44%; float:left">
						<f:facet name="header">
							<h:panelGroup>
								<h:outputText value="รายชื่อผู้ใช้งาน" />
							</h:panelGroup>
						</f:facet>
						<rich:dataTable
							id="tableUserList2"
							value="#{budgetAdmin.purchasingUserList}" 
							var="user"  
							rows="20" 
							rowKeyVar="rowNo"
							style="width:100%"			
							rowClasses="odd-row, even-row"	
							>
						<a4j:support event="onRowClick" action="#{budgetAdmin.purchasingUserTableRowClicked}" reRender="editPanel2" status="showload">
							<f:setPropertyActionListener value="#{user}" target="#{budgetAdmin.editUser}" />
						</a4j:support>
						<rich:column style="text-align:right"  width="20px">
							<f:facet name="header"><h:outputText value=""/></f:facet>
							<h:outputText value="#{rowNo+1}"/>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="ชื่อผู้ใช้งาน"/></f:facet>
							<h:outputText value="#{user.username}"/>
						</rich:column>
						<rich:column style="text-align:left"  >
							<f:facet name="header"><h:outputText value="ชื่อ - นามสกุล"/></f:facet>
							<h:outputText value="#{user.fullName}"/>
						</rich:column>
						<rich:column style="text-align:left" >
							<f:facet name="header"><h:outputText value="ศูนย์" /></f:facet>
							<h:outputText value="#{user.warehouseCode}" />
						</rich:column>
						<f:facet name="footer">
							<rich:datascroller renderIfSinglePage="false"></rich:datascroller>
						</f:facet>
					</rich:dataTable>
				</rich:panel>
			</a4j:region>
			<div style="float:left;width:55%;margin-left:5px;">
			<a4j:region renderRegionOnly="false">
				<rich:panel id="editPanel2" >
					<f:facet name="header">
						<h:panelGroup>
							<h:outputText value="กำหนดสิทธิการเข้าใช้งบประมาณ" />
						</h:panelGroup>
					</f:facet>
					<h:outputText value="ชื่อผู้ใช้งาน" styleClass="label medium"/>
					<h:inputText value="#{budgetAdmin.editUser.username}" styleClass="input medium" required="true" size="5" requiredMessage="กรุณาใสชื่อผู้ใช้งาน" disabled="true"/>
					<div id="wrapper"></div>
					<h:outputText value="ชื่อ" styleClass="label medium"/>
					<h:inputText value="#{budgetAdmin.editUser.firstName}" styleClass="input medium" required="true" size="5" requiredMessage="กรุณาใสชื่อผู้ใช้งาน" disabled="true"/>
					<div id="wrapper"></div>
					<h:outputText value="นามสกุล" styleClass="label medium"/>
					<h:inputText value="#{budgetAdmin.editUser.lastName}" styleClass="input medium" required="true" size="5" requiredMessage="กรุณาใสชื่อผู้ใช้งาน" disabled="true"/>
					<div id="wrapper"></div>
					<h:outputText value="ศูนย์" styleClass="label medium"/>
					<h:inputText value="#{budgetAdmin.editUser.warehouseCode}" styleClass="input medium" required="true" size="5" requiredMessage="กรุณาใสชื่อผู้ใช้งาน" disabled="true"/>
					<div id="wrapper"></div>
					<h:outputText value="สิทธิการใช้งานงบประมาณ" styleClass="label medium"/>
					<h:selectManyCheckbox value="#{budgetAdmin.selectedBudgetAuthorizationList}" layout="pageDirection" style="border:1px dashed red">
						<f:selectItems value="#{budgetAdmin.budgetAuthorizationSelectItemList}"/>
					</h:selectManyCheckbox>
				<rich:separator height="1" lineType="solid" style="margin-bottom:5px" />		
				<a4j:commandButton action="#{budgetAdmin.saveBudgetAuthorization}" value="บันทึกสิทธิ" reRender="listPanel2, editPanel2" oncomplete="if (#{facesContext.maximumSeverity != null}) Richfaces.showModalPanel('mpErrors');" status="showload"/>
				</rich:panel>
			</a4j:region>
			</div>
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
