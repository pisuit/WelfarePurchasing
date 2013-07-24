
    drop table WFSAUTHORIZATION cascade constraints ;

    drop table WFSBUDGET cascade constraints ;

    drop table WFSBUDGETAUTHORIZATION cascade constraints ;

    drop table WFSBUDGETEXPENSE cascade constraints ;

    drop table WFSBUDGETITEM cascade constraints ;

    drop table WFSBUDGETTRANSFER cascade constraints ;

    drop table WFSGI cascade constraints ;

    drop table WFSGIITEM cascade constraints ;

    drop table WFSGR cascade constraints ;

    drop table WFSGRITEM cascade constraints ;

    drop table WFSLOG cascade constraints ;

    drop table WFSMATERIAL cascade constraints ;

    drop table WFSMATERIALGROUP cascade constraints ;

    drop table WFSMATERIALLOT cascade constraints ;

    drop table WFSPO cascade constraints ;

    drop table WFSPOITEM cascade constraints ;

    drop table WFSPR cascade constraints ;

    drop table WFSPRITEM cascade constraints ;

    drop table WFSSTOCKMOVEMENT cascade constraints ;

    drop table WFSUSER cascade constraints ;

    drop table WFSVENDOR cascade constraints ;

    drop table human.DEPARTMENT cascade constraints ;

    drop table human.DEPARTMENTASSIGNMENT cascade constraints ;

    drop table human.EMPLOYEE cascade constraints ;

    drop table human.HOLIDAY cascade constraints ;

    drop table human.NAMEPREFIX cascade constraints ;

    create table WFSAUTHORIZATION (
        ID number(19,0) not null,
        SYSTEMROLE varchar2(50 char),
        WFSUSER_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSBUDGET (
        ID number(19,0) not null,
        BUDGETYEAR number(10,0),
        ISAVAILABLE number(1,0),
        ISEDITABLE number(1,0),
        primary key (ID)
    ) ;

    create table WFSBUDGETAUTHORIZATION (
        ID number(19,0) not null,
        BUDGETAUTH varchar2(255 char),
        WFSUSER_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSBUDGETEXPENSE (
        ID number(19,0) not null,
        POSTINGDATE date,
        AMOUNT number(19,2),
        INVOICENUMBER varchar2(20 char),
        DETAIL varchar2(500 char),
        WFSBUDGETITEM_ID number(19,0),
        WFSVENDOR_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSBUDGETITEM (
        ID number(19,0) not null,
        ACCOUNTCODE varchar2(8 char),
        CATEGORY varchar2(500 char),
        INITIALAMOUNT number(19,2),
        RESERVEDAMOUNT number(19,2),
        EXPENSEDAMOUNT number(19,2),
        TRANSFERINAMOUNT number(19,2),
        TRANSFEROUTAMOUNT number(19,2),
        BUDGETTYPE varchar2(1 char),
        STATUS varchar2(15 char),
        ISAVAILABLE number(1,0),
        ISEDITABLE number(1,0),
        ISCONTROLLED number(1,0),
        ISMONTHLYBUDGET number(1,0),
        ISPURCHASINGBUDGET number(1,0),
        ISEXPENSEENTRY number(1,0),
        ISNURSERYBUDGET number(1,0),
        BUDGETLEVEL varchar2(7 char),
        WFBBUDGET_ID number(19,0),
        WFSBUDGETITEM_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSBUDGETTRANSFER (
        ID number(19,0) not null,
        TRANSFERNUMBER number(10,0),
        EXTERNALTRANSFERNUMBER varchar2(10 char),
        REQUESTAMOUNT number(19,2),
        APPROVEAMOUNT number(19,2),
        REASON varchar2(500 char),
        REQUESTDATE date,
        APPROVEDATE date,
        OLDAMOUNT number(19,2),
        STATUS varchar2(15 char),
        WFBBUDGETITEM_ID_FROM number(19,0),
        WFBBUDGETITEM_ID_TO number(19,0),
        WFBBUDGETITEM_ID_OLD_FROM number(19,0),
        WFBBUDGETITEM_ID_OLD_TO number(19,0),
        WFSBUDGETITEM_ID_TO number(19,0),
        WFSBUDGETITEM_ID_FROM number(19,0),
        primary key (ID)
    ) ;

    create table WFSGI (
        ID number(19,0) not null,
        GINUMBER number(10,0),
        REASON varchar2(500 char),
        STATUS varchar2(15 char),
        AMOUNT number(19,2),
        ISSUEDATE date,
        ISSUERNAME varchar2(200 char),
        ISSUETYPE varchar2(15 char),
        WAREHOUSE varchar2(15 char),
        STORAGELOC varchar2(25 char),
        primary key (ID)
    ) ;

    create table WFSGIITEM (
        ID number(19,0) not null,
        ITEMNUMBER number(10,0),
        QTY number(19,2),
        UNITPRICE number(19,2),
        TOTALPRICE number(19,2),
        UNIT varchar2(10 char),
        WAREHOUSE varchar2(15 char),
        STORAGELOC varchar2(25 char),
        WFSGI_ID number(19,0),
        WFSMATERIAL_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSGR (
        ID number(19,0) not null,
        GRNUMBER number(10,0),
        BUDGETYEAR number(10,0),
        REASON varchar2(500 char),
        INVNUMBER varchar2(30 char),
        GRTYPE varchar2(15 char),
        RECDATE date,
        POSTINGDATE date,
        RECNAME varchar2(200 char),
        RECPOS varchar2(200 char),
        ENTNAME varchar2(200 char),
        ENTPOS varchar2(200 char),
        TOTALPRICE number(19,2),
        TOTALDISC number(19,2),
        WAREHOUSECODE varchar2(15 char),
        STATUS varchar2(15 char),
        WFSPO_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSGRITEM (
        ID number(19,0) not null,
        ITEMNUMBER number(10,0),
        OTHMAT varchar2(500 char),
        ORDQTY number(19,2),
        RECQTY number(19,2),
        ORDUNITPRICE number(19,2),
        UNITPRICE number(19,2),
        RECEIVEUNIT varchar2(10 char),
        BUDEXPENSEDAMT number(19,2),
        STORAGELOC varchar2(25 char),
        TOTALPRICE number(19,2),
        DISCAMOUNT number(19,2),
        NETPRICE number(19,2),
        AVGPRICE number(19,2),
        WFSGR_ID number(19,0),
        WFSMATERIAL_ID number(19,0),
        WFSBUDGETITEM_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSLOG (
        ID number(19,0) not null,
        LOGDATE timestamp,
        DESCRIPTION varchar2(255 char),
        LOGTYPE varchar2(15 char),
        IPADDRESS varchar2(20 char),
        WFSUSER_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSMATERIAL (
        ID number(19,0) not null,
        CODE varchar2(4 char),
        DESCRIPTION varchar2(50 char),
        ISSUEUNIT varchar2(25 char),
        ORDERUNIT varchar2(25 char),
        UNITCONVERTER double precision,
        ORDERUNITPRICE number(19,2),
        MAXSTOCK double precision,
        MINSTOCK double precision,
        WAREHOUSECODE varchar2(15 char),
        STATUS varchar2(15 char),
        WFPMATERIALGROUP_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSMATERIALGROUP (
        ID number(19,0) not null,
        CODE varchar2(2 char),
        DESCRIPTION varchar2(500 char),
        WAREHOUSECODE varchar2(15 char),
        STATUS varchar2(15 char),
        primary key (ID)
    ) ;

    create table WFSMATERIALLOT (
        ID number(19,0) not null,
        UNITPRICE number(19,2),
        TOTALQTY number(19,2),
        TOTALPRICE number(19,2),
        AVAILABLEQTY number(19,2),
        CREATEDDATE date,
        WAREHOUSE varchar2(15 char),
        STORAGELOC varchar2(25 char),
        STATUS varchar2(15 char),
        WFSMATERIAL_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSPO (
        ID number(19,0) not null,
        PONUMBER number(10,0),
        BUDGETYEAR number(10,0),
        POTYPE varchar2(15 char),
        REASON varchar2(500 char),
        BUYNAME varchar2(200 char),
        RECNAME varchar2(200 char),
        REQNAME varchar2(200 char),
        REQPOS varchar2(200 char),
        INSNAME varchar2(200 char),
        INSPOS varchar2(200 char),
        APPNAME varchar2(200 char),
        APPPOS varchar2(200 char),
        POSTINGDATE date,
        TOTALPRICE number(19,2),
        WAREHOUSECODE varchar2(15 char),
        STATUS varchar2(15 char),
        OTHERVENDOR varchar2(500 char),
        WSFSPR_ID number(19,0),
        WFSVENDOR_ID number(19,0),
        WFSPR_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSPOITEM (
        ID number(19,0) not null,
        ITEMNUMBER number(10,0),
        OTHERMAT varchar2(500 char),
        QUANTITY number(19,2),
        UNITPRICE number(19,2),
        ORDERUNIT varchar2(10 char),
        DELIVERYDATE date,
        RECQTY number(19,2),
        BUDGETRESERVEDAMOUNT number(19,2),
        WFSPO_ID number(19,0),
        WFSMATERIAL_ID number(19,0),
        WFSBUDGETITEM_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSPR (
        ID number(19,0) not null,
        PRNUMBER number(10,0),
        REASON varchar2(500 char),
        REFDOCNUMBER varchar2(50 char),
        REQNAME varchar2(200 char),
        REQPOS varchar2(200 char),
        BUDGETYEAR number(10,0),
        PRTYPE varchar2(15 char),
        INSNAME varchar2(200 char),
        INSPOS varchar2(200 char),
        APPNAME varchar2(200 char),
        APPPOS varchar2(200 char),
        POSTINGDATE date,
        TOTALPRICE number(19,2),
        WAREHOUSECODE varchar2(15 char),
        STATUS varchar2(15 char),
        primary key (ID)
    ) ;

    create table WFSPRITEM (
        ID number(19,0) not null,
        ITEMNUMBER number(10,0),
        OTHERMAT varchar2(500 char),
        QUANTITY number(19,2),
        UNITPRICE number(19,2),
        ORDERUNIT varchar2(10 char),
        DELIVERYDATE date,
        BUDGETRESERVEDAMOUNT number(19,2),
        WFSPR_ID number(19,0),
        WFSMATERIAL_ID number(19,0),
        WFSBUDGETITEM_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSSTOCKMOVEMENT (
        ID number(19,0) not null,
        MOVEMENTDATE date,
        TOTALQTY number(19,2),
        UNITPRICE number(19,2),
        TOTALPRICE number(19,2),
        MOVEMENTSIGN number(10,0),
        MOVEMENTTYPE varchar2(25 char),
        MOVEMENTREMARK varchar2(100 char),
        STATUS varchar2(15 char),
        WAREHOUSE varchar2(15 char),
        STORAGELOC varchar2(25 char),
        WFSMATERIAL_ID number(19,0),
        WFSMATERIALLOT_ID number(19,0),
        WFSGRITEM_ID number(19,0),
        WFSGIITEM_ID number(19,0),
        primary key (ID)
    ) ;

    create table WFSUSER (
        ID number(19,0) not null,
        USERNAME varchar2(30 char),
        USERPASSWORD varchar2(30 char),
        FIRSTNAME varchar2(25 char),
        LASTNAME varchar2(25 char),
        MAINSYSTEMNAME varchar2(50 char),
        SUBSYSTEMNAME varchar2(50 char),
        USERTYPE varchar2(1 char),
        STATUS varchar2(15 char),
        WAREHOUSECODE varchar2(15 char),
        primary key (ID)
    ) ;

    create table WFSVENDOR (
        ID number(19,0) not null,
        VENDORNUMBER number(10,0),
        NAME varchar2(500 char),
        TAXID varchar2(15 char),
        ADDRESS1 varchar2(200 char),
        ADDRESS2 varchar2(200 char),
        ADDRESS3 varchar2(200 char),
        STATUS varchar2(15 char),
        primary key (ID)
    ) ;

    create table human.DEPARTMENT (
        DEPARTMENTID number(19,0) not null,
        THAISHORTNAME varchar2(255 char),
        THAIFULLNAME varchar2(255 char),
        ENGSHORTNAME varchar2(255 char),
        ENGFULLNAME varchar2(255 char),
        ISACTIVE number(1,0),
        primary key (DEPARTMENTID)
    ) ;

    create table human.DEPARTMENTASSIGNMENT (
        DEPARTMENTASSIGNMENTID number(19,0) not null,
        EFFECTIVEDATE date,
        EXPIREDDATE date,
        EMPLOYEE_EMPLOYEEID number(19,0),
        DEPARTMENT_DEPARTMENTID number(19,0),
        primary key (DEPARTMENTASSIGNMENTID)
    ) ;

    create table human.EMPLOYEE (
        EMPLOYEEID number(19,0) not null,
        EMPLOYEECODE varchar2(6 char),
        THAIFIRSTNAME varchar2(50 char),
        THAILASTNAME varchar2(50 char),
        ENGFIRSTNAME varchar2(50 char),
        ENGLASTNAME varchar2(50 char),
        NAMEPREFIX_ID number(19,0),
        EMPLOYEE_EMPLOYEEID number(19,0),
        primary key (EMPLOYEEID)
    ) ;

    create table human.HOLIDAY (
        HOLIDAYID number(19,0) not null,
        DATE1 date,
        DESCRIPTION varchar2(100 char),
        INSTEADOFDATE date,
        primary key (HOLIDAYID)
    ) ;

    create table human.NAMEPREFIX (
        ID number(19,0) not null,
        THAISHORTNAME varchar2(50 char),
        THAIFULLNAME varchar2(50 char),
        ENGSHORTNAME varchar2(50 char),
        ENGFULLNAME varchar2(50 char),
        primary key (ID)
    ) ;

    alter table WFSAUTHORIZATION 
        add constraint FKDCA1CB7537E669CF 
        foreign key (WFSUSER_ID) 
        references WFSUSER ;

    alter table WFSBUDGETAUTHORIZATION 
        add constraint FK45C09B5037E669CF 
        foreign key (WFSUSER_ID) 
        references WFSUSER ;

    alter table WFSBUDGETEXPENSE 
        add constraint FKB7703C6FCEE604B1 
        foreign key (WFSVENDOR_ID) 
        references WFSVENDOR ;

    alter table WFSBUDGETEXPENSE 
        add constraint FKB7703C6F390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM ;

    alter table WFSBUDGETITEM 
        add constraint FK63134E3CF71BCA5B 
        foreign key (WFBBUDGET_ID) 
        references WFSBUDGET ;

    alter table WFSBUDGETITEM 
        add constraint FK63134E3C390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM ;

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC694E2DD4A4F 
        foreign key (WFBBUDGETITEM_ID_OLD_TO) 
        references WFSBUDGETITEM ;

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC69448DCDE66 
        foreign key (WFBBUDGETITEM_ID_FROM) 
        references WFSBUDGETITEM ;

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC69496ADCF48 
        foreign key (WFSBUDGETITEM_ID_TO) 
        references WFSBUDGETITEM ;

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC6943B661137 
        foreign key (WFBBUDGETITEM_ID_TO) 
        references WFSBUDGETITEM ;

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC694EF6A317E 
        foreign key (WFBBUDGETITEM_ID_OLD_FROM) 
        references WFSBUDGETITEM ;

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC694F12D5C37 
        foreign key (WFSBUDGETITEM_ID_FROM) 
        references WFSBUDGETITEM ;

    alter table WFSGIITEM 
        add constraint FK6EC86F5953D0E3E4 
        foreign key (WFSGI_ID) 
        references WFSGI ;

    alter table WFSGIITEM 
        add constraint FK6EC86F59446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL ;

    alter table WFSGR 
        add constraint FK4EB0F4F8882B1C9 
        foreign key (WFSPO_ID) 
        references WFSPO ;

    alter table WFSGRITEM 
        add constraint FK6F4742E2390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM ;

    alter table WFSGRITEM 
        add constraint FK6F4742E2446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL ;

    alter table WFSGRITEM 
        add constraint FK6F4742E264A8CCA8 
        foreign key (WFSGR_ID) 
        references WFSGR ;

    alter table WFSLOG 
        add constraint FK9876ED4037E669CF 
        foreign key (WFSUSER_ID) 
        references WFSUSER ;

    alter table WFSMATERIAL 
        add constraint FKB877008B9897F0CE 
        foreign key (WFPMATERIALGROUP_ID) 
        references WFSMATERIALGROUP ;

    alter table WFSMATERIALLOT 
        add constraint FK606956C6446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL ;

    alter table WFSPO 
        add constraint FK4EB1063F3B52D34 
        foreign key (WFSPR_ID) 
        references WFSPR ;

    alter table WFSPO 
        add constraint FK4EB1063CEE604B1 
        foreign key (WFSVENDOR_ID) 
        references WFSVENDOR ;

    alter table WFSPO 
        add constraint FK4EB10634000132F 
        foreign key (WSFSPR_ID) 
        references WFSPR ;

    alter table WFSPOITEM 
        add constraint FK7E7899F68882B1C9 
        foreign key (WFSPO_ID) 
        references WFSPO ;

    alter table WFSPOITEM 
        add constraint FK7E7899F6390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM ;

    alter table WFSPOITEM 
        add constraint FK7E7899F6446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL ;

    alter table WFSPRITEM 
        add constraint FK7EA2E079F3B52D34 
        foreign key (WFSPR_ID) 
        references WFSPR ;

    alter table WFSPRITEM 
        add constraint FK7EA2E079390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM ;

    alter table WFSPRITEM 
        add constraint FK7EA2E079446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL ;

    alter table WFSSTOCKMOVEMENT 
        add constraint FKCA8F6CC19B5BF624 
        foreign key (WFSGIITEM_ID) 
        references WFSGIITEM ;

    alter table WFSSTOCKMOVEMENT 
        add constraint FKCA8F6CC138C9DCE8 
        foreign key (WFSGRITEM_ID) 
        references WFSGRITEM ;

    alter table WFSSTOCKMOVEMENT 
        add constraint FKCA8F6CC1542C7351 
        foreign key (WFSMATERIALLOT_ID) 
        references WFSMATERIALLOT ;

    alter table WFSSTOCKMOVEMENT 
        add constraint FKCA8F6CC1446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL ;

    alter table human.DEPARTMENTASSIGNMENT 
        add constraint FK61A0239F724FC07B 
        foreign key (DEPARTMENT_DEPARTMENTID) 
        references human.DEPARTMENT ;

    alter table human.DEPARTMENTASSIGNMENT 
        add constraint FK61A0239F5381AA57 
        foreign key (EMPLOYEE_EMPLOYEEID) 
        references human.EMPLOYEE ;

    alter table human.EMPLOYEE 
        add constraint FK75C8D6AE5A51F229 
        foreign key (NAMEPREFIX_ID) 
        references human.NAMEPREFIX ;

    alter table human.EMPLOYEE 
        add constraint FK75C8D6AE5381AA57 
        foreign key (EMPLOYEE_EMPLOYEEID) 
        references human.EMPLOYEE ;
