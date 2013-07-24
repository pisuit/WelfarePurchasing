function Validator(frmName) {
	//this.formobj = document.forms[frmName];
	this.frmName = frmName;
	//if (!this.formobj) {
	//	alert("Error : "+frmName+" does not exists.");
	//	return ;
	//}
	this.addValidation = addValidation;
	this.validationDesc = new Array();
	this.validationLen = 0;
	this.validate = validate;
	//alert("created");
}

function addValidation(objName, validation, strError) {
	//if (!this.formobj) {
	//	alert("Error : form doesn't exists");
	//	return;
	//}
	//
	//var itemobj = this.formobj.elements[itemName];
	//if (!itemobj){
	//	alert("Error : "+itemName+" does not exists.");
	//	return;
	//}
	var desc =  new ValidationDesc();
	desc.objName = objName;
	desc.validation = validation;
	desc.errorMsg = strError;
	this.validationDesc[this.validationLen] = desc ;
	this.validationLen = this.validationLen + 1;
	//this.validationLen = this.validationDesc.push(desc);
}

function ValidationDesc(){
	var objName;
	var validation;
	var errorMsg;
}

function validate(){
	//alert("Start validate");
	var pass = true;
	//alert(this.validationDesc.length);
	for (var index=0; index < this.validationDesc.length; index++){
	//for (index in this.validationDesc){
		//alert("Validation result 01 : "+ret);
		var desc = this.validationDesc[index];
		//alert("this.formobj.name : "+this.formobj.name);
		//alert("desc.obj.name : "+this.validationDesc[index].obj.name);
		//alert(this.validationDesc[index].validation);
		//alert(this.frmName);
		//alert(desc);
		//alert(desc.objName);
		pass =  validateInput(this.frmName, desc.objName, desc.validation, desc.errorMsg, true);
		//alert("Validation result 03 : "+ret);
		if (!pass) {
			return false;
		} 
	}
	return pass;
}

function validateInput(frmName, objName, validation, strError, focusOnError) {
	//alert("frmName : "+frmName);
	//alert("inputName : "+inputName);
 	var formobj= document.forms[frmName];
	if (!formobj) {
		alert("Error : "+this.frmName+" does not exists.");
		return false;
	}
	var itemobj = formobj.elements[objName];
	//alert(formobj.name);
	//alert(itemobj.name);
	//alert(formobj);
	//for (var i=0;i<formobj.length;i++)
	//{
	//	alert(formobj.elements[i].name);
	//}
	//alert(itemobj);
	if (!itemobj){
		alert("Error : "+objName+" does not exists.");
		return false;
	}
	var pass = false;
	switch (validation) {
		case "req":
			pass = !isEmpty(itemobj);
			break;
		case "num":
			pass = !matchRegExp(itemobj, "[^0-9]");
			break;
		case "dec":
			pass = !matchRegExp(itemobj, "[^0-9\.]");
			break;
		case "alnum":
			pass = !matchRegExp(itemobj, "[^0-9A-Za-z]");
			break;
		case "alpha":
			pass = !matchRegExp(itemobj, "[^A-Za-z\\s]");
			break;
	}
	//alert("pass : "+pass);
	if (!pass){
		showErrorMsg(strError);
		if (focusOnError) {
			focus(itemobj);
		}
	}
	return pass;
}
function isEmpty(obj){
	var ret = false;
	var val = obj.value;
	val = val.replace(/^\s+|\s+$/g,"");//trim
    if(eval(val.length) == 0) 
    { 
       //if(!strError || strError.length ==0) 
       //{ 
       //  strError = obj.name + " : Required Field"; 
       //}//if 
       //showErrorMsg(strError,val); 
       ret=true; 
    }//if 
	return ret;
}

function matchRegExp(obj,strRegExp){
	var ret = false;
	var val = obj.value;
    if( val.length > 0 && 
        val.match(strRegExp) != null) 
    { 
 		ret = true;                   
    }//if 
	return ret;
}

function showErrorMsg(strError){
	if (!strError ||strError.length > 0) {
		alert(strError);
	}
}

function focus(obj) {
	if (!obj) {
		alert("Error : Object does not exists.");
		return;
	}
	obj.focus();
}