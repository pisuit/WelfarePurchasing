
    alter table WFSAUTHORIZATION 
        drop constraint FKDCA1CB7537E669CF LOCK DATAROWS GO 

    alter table WFSBUDGETAUTHORIZATION 
        drop constraint FK45C09B5037E669CF LOCK DATAROWS GO 

    alter table WFSBUDGETEXPENSE 
        drop constraint FKB7703C6FCEE604B1 LOCK DATAROWS GO 

    alter table WFSBUDGETEXPENSE 
        drop constraint FKB7703C6F390B2CAA LOCK DATAROWS GO 

    alter table WFSBUDGETITEM 
        drop constraint FK63134E3CF71BCA5B LOCK DATAROWS GO 

    alter table WFSBUDGETITEM 
        drop constraint FK63134E3C390B2CAA LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        drop constraint FKF64BC694E2DD4A4F LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        drop constraint FKF64BC69448DCDE66 LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        drop constraint FKF64BC69496ADCF48 LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        drop constraint FKF64BC6943B661137 LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        drop constraint FKF64BC694EF6A317E LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        drop constraint FKF64BC694F12D5C37 LOCK DATAROWS GO 

    alter table WFSGIITEM 
        drop constraint FK6EC86F5953D0E3E4 LOCK DATAROWS GO 

    alter table WFSGIITEM 
        drop constraint FK6EC86F59446E01C3 LOCK DATAROWS GO 

    alter table WFSGR 
        drop constraint FK4EB0F4F8882B1C9 LOCK DATAROWS GO 

    alter table WFSGRITEM 
        drop constraint FK6F4742E2390B2CAA LOCK DATAROWS GO 

    alter table WFSGRITEM 
        drop constraint FK6F4742E2446E01C3 LOCK DATAROWS GO 

    alter table WFSGRITEM 
        drop constraint FK6F4742E264A8CCA8 LOCK DATAROWS GO 

    alter table WFSLOG 
        drop constraint FK9876ED4037E669CF LOCK DATAROWS GO 

    alter table WFSMATERIAL 
        drop constraint FKB877008B9897F0CE LOCK DATAROWS GO 

    alter table WFSMATERIALLOT 
        drop constraint FK606956C6446E01C3 LOCK DATAROWS GO 

    alter table WFSPO 
        drop constraint FK4EB1063F3B52D34 LOCK DATAROWS GO 

    alter table WFSPO 
        drop constraint FK4EB1063CEE604B1 LOCK DATAROWS GO 

    alter table WFSPO 
        drop constraint FK4EB10634000132F LOCK DATAROWS GO 

    alter table WFSPOITEM 
        drop constraint FK7E7899F68882B1C9 LOCK DATAROWS GO 

    alter table WFSPOITEM 
        drop constraint FK7E7899F6390B2CAA LOCK DATAROWS GO 

    alter table WFSPOITEM 
        drop constraint FK7E7899F6446E01C3 LOCK DATAROWS GO 

    alter table WFSPRITEM 
        drop constraint FK7EA2E079F3B52D34 LOCK DATAROWS GO 

    alter table WFSPRITEM 
        drop constraint FK7EA2E079390B2CAA LOCK DATAROWS GO 

    alter table WFSPRITEM 
        drop constraint FK7EA2E079446E01C3 LOCK DATAROWS GO 

    alter table WFSSTOCKMOVEMENT 
        drop constraint FKCA8F6CC19B5BF624 LOCK DATAROWS GO 

    alter table WFSSTOCKMOVEMENT 
        drop constraint FKCA8F6CC138C9DCE8 LOCK DATAROWS GO 

    alter table WFSSTOCKMOVEMENT 
        drop constraint FKCA8F6CC1542C7351 LOCK DATAROWS GO 

    alter table WFSSTOCKMOVEMENT 
        drop constraint FKCA8F6CC1446E01C3 LOCK DATAROWS GO 

    alter table human.DEPARTMENTASSIGNMENT 
        drop constraint FK61A0239F724FC07B LOCK DATAROWS GO 

    alter table human.DEPARTMENTASSIGNMENT 
        drop constraint FK61A0239F5381AA57 LOCK DATAROWS GO 

    alter table human.EMPLOYEE 
        drop constraint FK75C8D6AE5A51F229 LOCK DATAROWS GO 

    alter table human.EMPLOYEE 
        drop constraint FK75C8D6AE5381AA57 LOCK DATAROWS GO 

    drop table WFSAUTHORIZATION LOCK DATAROWS GO 

    drop table WFSBUDGET LOCK DATAROWS GO 

    drop table WFSBUDGETAUTHORIZATION LOCK DATAROWS GO 

    drop table WFSBUDGETEXPENSE LOCK DATAROWS GO 

    drop table WFSBUDGETITEM LOCK DATAROWS GO 

    drop table WFSBUDGETTRANSFER LOCK DATAROWS GO 

    drop table WFSGI LOCK DATAROWS GO 

    drop table WFSGIITEM LOCK DATAROWS GO 

    drop table WFSGR LOCK DATAROWS GO 

    drop table WFSGRITEM LOCK DATAROWS GO 

    drop table WFSLOG LOCK DATAROWS GO 

    drop table WFSMATERIAL LOCK DATAROWS GO 

    drop table WFSMATERIALGROUP LOCK DATAROWS GO 

    drop table WFSMATERIALLOT LOCK DATAROWS GO 

    drop table WFSPO LOCK DATAROWS GO 

    drop table WFSPOITEM LOCK DATAROWS GO 

    drop table WFSPR LOCK DATAROWS GO 

    drop table WFSPRITEM LOCK DATAROWS GO 

    drop table WFSSTOCKMOVEMENT LOCK DATAROWS GO 

    drop table WFSUSER LOCK DATAROWS GO 

    drop table WFSVENDOR LOCK DATAROWS GO 

    drop table human.DEPARTMENT LOCK DATAROWS GO 

    drop table human.DEPARTMENTASSIGNMENT LOCK DATAROWS GO 

    drop table human.EMPLOYEE LOCK DATAROWS GO 

    drop table human.HOLIDAY LOCK DATAROWS GO 

    drop table human.NAMEPREFIX LOCK DATAROWS GO 

    create table WFSAUTHORIZATION (
        ID numeric(19,0) not null,
        SYSTEMROLE varchar(50) null,
        WFSUSER_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSBUDGET (
        ID numeric(19,0) not null,
        BUDGETYEAR int null,
        ISAVAILABLE tinyint null,
        ISEDITABLE tinyint null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSBUDGETAUTHORIZATION (
        ID numeric(19,0) not null,
        BUDGETAUTH varchar(255) null,
        WFSUSER_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSBUDGETEXPENSE (
        ID numeric(19,0) not null,
        POSTINGDATE datetime null,
        AMOUNT numeric(19,2) null,
        INVOICENUMBER varchar(20) null,
        DETAIL varchar(100) null,
        WFSBUDGETITEM_ID numeric(19,0) null,
        WFSVENDOR_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSBUDGETITEM (
        ID numeric(19,0) not null,
        ACCOUNTCODE varchar(8) null,
        CATEGORY varchar(100) null,
        INITIALAMOUNT numeric(19,2) null,
        RESERVEDAMOUNT numeric(19,2) null,
        EXPENSEDAMOUNT numeric(19,2) null,
        TRANSFERINAMOUNT numeric(19,2) null,
        TRANSFEROUTAMOUNT numeric(19,2) null,
        BUDGETTYPE varchar(1) null,
        STATUS varchar(15) null,
        ISAVAILABLE tinyint null,
        ISEDITABLE tinyint null,
        ISCONTROLLED tinyint null,
        ISMONTHLYBUDGET tinyint null,
        ISPURCHASINGBUDGET tinyint null,
        ISEXPENSEENTRY tinyint null,
        ISNURSERYBUDGET tinyint null,
        BUDGETLEVEL varchar(7) null,
        WFBBUDGET_ID numeric(19,0) null,
        WFSBUDGETITEM_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSBUDGETTRANSFER (
        ID numeric(19,0) not null,
        TRANSFERNUMBER int null,
        EXTERNALTRANSFERNUMBER varchar(10) null,
        REQUESTAMOUNT numeric(19,2) null,
        APPROVEAMOUNT numeric(19,2) null,
        REASON varchar(100) null,
        REQUESTDATE datetime null,
        APPROVEDATE datetime null,
        OLDAMOUNT numeric(19,2) null,
        STATUS varchar(15) null,
        WFBBUDGETITEM_ID_FROM numeric(19,0) null,
        WFBBUDGETITEM_ID_TO numeric(19,0) null,
        WFBBUDGETITEM_ID_OLD_FROM numeric(19,0) null,
        WFBBUDGETITEM_ID_OLD_TO numeric(19,0) null,
        WFSBUDGETITEM_ID_TO numeric(19,0) null,
        WFSBUDGETITEM_ID_FROM numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSGI (
        ID numeric(19,0) not null,
        GINUMBER int null,
        REASON varchar(300) null,
        STATUS varchar(15) null,
        AMOUNT numeric(19,2) null,
        ISSUEDATE datetime null,
        ISSUERNAME varchar(50) null,
        ISSUETYPE varchar(15) null,
        WAREHOUSE varchar(15) null,
        STORAGELOC varchar(25) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSGIITEM (
        ID numeric(19,0) not null,
        ITEMNUMBER int null,
        QTY numeric(19,2) null,
        UNITPRICE numeric(19,2) null,
        TOTALPRICE numeric(19,2) null,
        UNIT varchar(10) null,
        WAREHOUSE varchar(15) null,
        STORAGELOC varchar(25) null,
        WFSGI_ID numeric(19,0) null,
        WFSMATERIAL_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSGR (
        ID numeric(19,0) not null,
        GRNUMBER int null,
        REASON varchar(300) null,
        INVNUMBER varchar(30) null,
        RECDATE datetime null,
        POSTINGDATE datetime null,
        RECNAME varchar(50) null,
        RECPOS varchar(50) null,
        ENTNAME varchar(50) null,
        ENTPOS varchar(50) null,
        TOTALPRICE numeric(19,2) null,
        TOTALDISC numeric(19,2) null,
        WAREHOUSECODE varchar(15) null,
        STATUS varchar(15) null,
        WFSPO_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSGRITEM (
        ID numeric(19,0) not null,
        ITEMNUMBER int null,
        OTHMAT varchar(80) null,
        ORDQTY numeric(19,2) null,
        RECQTY numeric(19,2) null,
        ORDUNITPRICE numeric(19,2) null,
        UNITPRICE numeric(19,2) null,
        RECEIVEUNIT varchar(10) null,
        BUDEXPENSEDAMT numeric(19,2) null,
        STORAGELOC varchar(25) null,
        TOTALPRICE numeric(19,2) null,
        DISCAMOUNT numeric(19,2) null,
        NETPRICE numeric(19,2) null,
        AVGPRICE numeric(19,2) null,
        WFSGR_ID numeric(19,0) null,
        WFSMATERIAL_ID numeric(19,0) null,
        WFSBUDGETITEM_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSLOG (
        ID numeric(19,0) not null,
        LOGDATE datetime null,
        DESCRIPTION varchar(255) null,
        LOGTYPE varchar(15) null,
        IPADDRESS varchar(20) null,
        WFSUSER_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSMATERIAL (
        ID numeric(19,0) not null,
        CODE varchar(4) null,
        DESCRIPTION varchar(50) null,
        ISSUEUNIT varchar(25) null,
        ORDERUNIT varchar(25) null,
        UNITCONVERTER double precision null,
        ORDERUNITPRICE numeric(19,2) null,
        MAXSTOCK double precision null,
        MINSTOCK double precision null,
        WAREHOUSECODE varchar(15) null,
        STATUS varchar(15) null,
        WFPMATERIALGROUP_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSMATERIALGROUP (
        ID numeric(19,0) not null,
        CODE varchar(2) null,
        DESCRIPTION varchar(50) null,
        WAREHOUSECODE varchar(15) null,
        STATUS varchar(15) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSMATERIALLOT (
        ID numeric(19,0) not null,
        UNITPRICE numeric(19,2) null,
        TOTALQTY numeric(19,2) null,
        TOTALPRICE numeric(19,2) null,
        AVAILABLEQTY numeric(19,2) null,
        CREATEDDATE datetime null,
        WAREHOUSE varchar(15) null,
        STORAGELOC varchar(25) null,
        STATUS varchar(15) null,
        WFSMATERIAL_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSPO (
        ID numeric(19,0) not null,
        PONUMBER int null,
        POTYPE varchar(15) null,
        REASON varchar(255) null,
        BUYNAME varchar(25) null,
        RECNAME varchar(25) null,
        REQNAME varchar(25) null,
        REQPOS varchar(25) null,
        INSNAME varchar(25) null,
        INSPOS varchar(25) null,
        APPNAME varchar(25) null,
        APPPOS varchar(25) null,
        POSTINGDATE datetime null,
        TOTALPRICE numeric(19,2) null,
        WAREHOUSECODE varchar(15) null,
        STATUS varchar(15) null,
        WSFSPR_ID numeric(19,0) null,
        WFSVENDOR_ID numeric(19,0) null,
        WFSPR_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSPOITEM (
        ID numeric(19,0) not null,
        ITEMNUMBER int null,
        OTHERMAT varchar(80) null,
        QUANTITY numeric(19,2) null,
        UNITPRICE numeric(19,2) null,
        ORDERUNIT varchar(10) null,
        DELIVERYDATE datetime null,
        RECQTY numeric(19,2) null,
        BUDGETRESERVEDAMOUNT numeric(19,2) null,
        WFSPO_ID numeric(19,0) null,
        WFSMATERIAL_ID numeric(19,0) null,
        WFSBUDGETITEM_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSPR (
        ID numeric(19,0) not null,
        PRNUMBER int null,
        REASON varchar(255) null,
        REFDOCNUMBER varchar(50) null,
        REQNAME varchar(25) null,
        REQPOS varchar(25) null,
        INSNAME varchar(25) null,
        INSPOS varchar(25) null,
        APPNAME varchar(25) null,
        APPPOS varchar(25) null,
        POSTINGDATE datetime null,
        TOTALPRICE numeric(19,2) null,
        WAREHOUSECODE varchar(15) null,
        STATUS varchar(15) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSPRITEM (
        ID numeric(19,0) not null,
        ITEMNUMBER int null,
        OTHERMAT varchar(80) null,
        QUANTITY numeric(19,2) null,
        UNITPRICE numeric(19,2) null,
        ORDERUNIT varchar(10) null,
        DELIVERYDATE datetime null,
        BUDGETRESERVEDAMOUNT numeric(19,2) null,
        WFSPR_ID numeric(19,0) null,
        WFSMATERIAL_ID numeric(19,0) null,
        WFSBUDGETITEM_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSSTOCKMOVEMENT (
        ID numeric(19,0) not null,
        MOVEMENTDATE datetime null,
        TOTALQTY numeric(19,2) null,
        UNITPRICE numeric(19,2) null,
        TOTALPRICE numeric(19,2) null,
        MOVEMENTSIGN int null,
        MOVEMENTTYPE varchar(25) null,
        MOVEMENTREMARK varchar(100) null,
        STATUS varchar(15) null,
        WAREHOUSE varchar(15) null,
        STORAGELOC varchar(25) null,
        WFSMATERIAL_ID numeric(19,0) null,
        WFSMATERIALLOT_ID numeric(19,0) null,
        WFSGRITEM_ID numeric(19,0) null,
        WFSGIITEM_ID numeric(19,0) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSUSER (
        ID numeric(19,0) not null,
        USERNAME varchar(10) null,
        USERPASSWORD varchar(10) null,
        FIRSTNAME varchar(25) null,
        LASTNAME varchar(25) null,
        MAINSYSTEMNAME varchar(50) null,
        SUBSYSTEMNAME varchar(50) null,
        USERTYPE varchar(1) null,
        STATUS varchar(15) null,
        WAREHOUSECODE varchar(15) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table WFSVENDOR (
        ID numeric(19,0) not null,
        VENDORNUMBER int null,
        NAME varchar(50) null,
        TAXID varchar(15) null,
        ADDRESS1 varchar(50) null,
        ADDRESS2 varchar(25) null,
        ADDRESS3 varchar(25) null,
        STATUS varchar(15) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    create table human.DEPARTMENT (
        DEPARTMENTID numeric(19,0) not null,
        THAISHORTNAME varchar(255) null,
        THAIFULLNAME varchar(255) null,
        ENGSHORTNAME varchar(255) null,
        ENGFULLNAME varchar(255) null,
        ISACTIVE tinyint null,
        primary key (DEPARTMENTID)
    ) LOCK DATAROWS GO 

    create table human.DEPARTMENTASSIGNMENT (
        DEPARTMENTASSIGNMENTID numeric(19,0) not null,
        EFFECTIVEDATE datetime null,
        EXPIREDDATE datetime null,
        EMPLOYEE_EMPLOYEEID numeric(19,0) null,
        DEPARTMENT_DEPARTMENTID numeric(19,0) null,
        primary key (DEPARTMENTASSIGNMENTID)
    ) LOCK DATAROWS GO 

    create table human.EMPLOYEE (
        EMPLOYEEID numeric(19,0) not null,
        EMPLOYEECODE varchar(6) null,
        THAIFIRSTNAME varchar(50) null,
        THAILASTNAME varchar(50) null,
        ENGFIRSTNAME varchar(50) null,
        ENGLASTNAME varchar(50) null,
        NAMEPREFIX_ID numeric(19,0) null,
        EMPLOYEE_EMPLOYEEID numeric(19,0) null,
        primary key (EMPLOYEEID)
    ) LOCK DATAROWS GO 

    create table human.HOLIDAY (
        HOLIDAYID numeric(19,0) not null,
        DATE1 datetime null,
        DESCRIPTION varchar(100) null,
        INSTEADOFDATE datetime null,
        primary key (HOLIDAYID)
    ) LOCK DATAROWS GO 

    create table human.NAMEPREFIX (
        ID numeric(19,0) not null,
        THAISHORTNAME varchar(50) null,
        THAIFULLNAME varchar(50) null,
        ENGSHORTNAME varchar(50) null,
        ENGFULLNAME varchar(50) null,
        primary key (ID)
    ) LOCK DATAROWS GO 

    alter table WFSAUTHORIZATION 
        add constraint FKDCA1CB7537E669CF 
        foreign key (WFSUSER_ID) 
        references WFSUSER LOCK DATAROWS GO 

    alter table WFSBUDGETAUTHORIZATION 
        add constraint FK45C09B5037E669CF 
        foreign key (WFSUSER_ID) 
        references WFSUSER LOCK DATAROWS GO 

    alter table WFSBUDGETEXPENSE 
        add constraint FKB7703C6FCEE604B1 
        foreign key (WFSVENDOR_ID) 
        references WFSVENDOR LOCK DATAROWS GO 

    alter table WFSBUDGETEXPENSE 
        add constraint FKB7703C6F390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSBUDGETITEM 
        add constraint FK63134E3CF71BCA5B 
        foreign key (WFBBUDGET_ID) 
        references WFSBUDGET LOCK DATAROWS GO 

    alter table WFSBUDGETITEM 
        add constraint FK63134E3C390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC694E2DD4A4F 
        foreign key (WFBBUDGETITEM_ID_OLD_TO) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC69448DCDE66 
        foreign key (WFBBUDGETITEM_ID_FROM) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC69496ADCF48 
        foreign key (WFSBUDGETITEM_ID_TO) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC6943B661137 
        foreign key (WFBBUDGETITEM_ID_TO) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC694EF6A317E 
        foreign key (WFBBUDGETITEM_ID_OLD_FROM) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSBUDGETTRANSFER 
        add constraint FKF64BC694F12D5C37 
        foreign key (WFSBUDGETITEM_ID_FROM) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSGIITEM 
        add constraint FK6EC86F5953D0E3E4 
        foreign key (WFSGI_ID) 
        references WFSGI LOCK DATAROWS GO 

    alter table WFSGIITEM 
        add constraint FK6EC86F59446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL LOCK DATAROWS GO 

    alter table WFSGR 
        add constraint FK4EB0F4F8882B1C9 
        foreign key (WFSPO_ID) 
        references WFSPO LOCK DATAROWS GO 

    alter table WFSGRITEM 
        add constraint FK6F4742E2390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSGRITEM 
        add constraint FK6F4742E2446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL LOCK DATAROWS GO 

    alter table WFSGRITEM 
        add constraint FK6F4742E264A8CCA8 
        foreign key (WFSGR_ID) 
        references WFSGR LOCK DATAROWS GO 

    alter table WFSLOG 
        add constraint FK9876ED4037E669CF 
        foreign key (WFSUSER_ID) 
        references WFSUSER LOCK DATAROWS GO 

    alter table WFSMATERIAL 
        add constraint FKB877008B9897F0CE 
        foreign key (WFPMATERIALGROUP_ID) 
        references WFSMATERIALGROUP LOCK DATAROWS GO 

    alter table WFSMATERIALLOT 
        add constraint FK606956C6446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL LOCK DATAROWS GO 

    alter table WFSPO 
        add constraint FK4EB1063F3B52D34 
        foreign key (WFSPR_ID) 
        references WFSPR LOCK DATAROWS GO 

    alter table WFSPO 
        add constraint FK4EB1063CEE604B1 
        foreign key (WFSVENDOR_ID) 
        references WFSVENDOR LOCK DATAROWS GO 

    alter table WFSPO 
        add constraint FK4EB10634000132F 
        foreign key (WSFSPR_ID) 
        references WFSPR LOCK DATAROWS GO 

    alter table WFSPOITEM 
        add constraint FK7E7899F68882B1C9 
        foreign key (WFSPO_ID) 
        references WFSPO LOCK DATAROWS GO 

    alter table WFSPOITEM 
        add constraint FK7E7899F6390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSPOITEM 
        add constraint FK7E7899F6446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL LOCK DATAROWS GO 

    alter table WFSPRITEM 
        add constraint FK7EA2E079F3B52D34 
        foreign key (WFSPR_ID) 
        references WFSPR LOCK DATAROWS GO 

    alter table WFSPRITEM 
        add constraint FK7EA2E079390B2CAA 
        foreign key (WFSBUDGETITEM_ID) 
        references WFSBUDGETITEM LOCK DATAROWS GO 

    alter table WFSPRITEM 
        add constraint FK7EA2E079446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL LOCK DATAROWS GO 

    alter table WFSSTOCKMOVEMENT 
        add constraint FKCA8F6CC19B5BF624 
        foreign key (WFSGIITEM_ID) 
        references WFSGIITEM LOCK DATAROWS GO 

    alter table WFSSTOCKMOVEMENT 
        add constraint FKCA8F6CC138C9DCE8 
        foreign key (WFSGRITEM_ID) 
        references WFSGRITEM LOCK DATAROWS GO 

    alter table WFSSTOCKMOVEMENT 
        add constraint FKCA8F6CC1542C7351 
        foreign key (WFSMATERIALLOT_ID) 
        references WFSMATERIALLOT LOCK DATAROWS GO 

    alter table WFSSTOCKMOVEMENT 
        add constraint FKCA8F6CC1446E01C3 
        foreign key (WFSMATERIAL_ID) 
        references WFSMATERIAL LOCK DATAROWS GO 

    alter table human.DEPARTMENTASSIGNMENT 
        add constraint FK61A0239F724FC07B 
        foreign key (DEPARTMENT_DEPARTMENTID) 
        references human.DEPARTMENT LOCK DATAROWS GO 

    alter table human.DEPARTMENTASSIGNMENT 
        add constraint FK61A0239F5381AA57 
        foreign key (EMPLOYEE_EMPLOYEEID) 
        references human.EMPLOYEE LOCK DATAROWS GO 

    alter table human.EMPLOYEE 
        add constraint FK75C8D6AE5A51F229 
        foreign key (NAMEPREFIX_ID) 
        references human.NAMEPREFIX LOCK DATAROWS GO 

    alter table human.EMPLOYEE 
        add constraint FK75C8D6AE5381AA57 
        foreign key (EMPLOYEE_EMPLOYEEID) 
        references human.EMPLOYEE LOCK DATAROWS GO 
