package welfare.utils;

import java.text.DecimalFormat;

public class Constants {
	// OUTCOME
	public static final String FAILURE_OUTCOME = "FAILURE";
	public static final String SUCCESS_OUTCOME = "SUCCESS";
	// KEY
	public static final String USER_KEY 			= "user";
	public static final String ORIGINAL_VIEW_KEY	= "origin";
	// VIEW
	public static final String LOGIN_VIEW = "/login.jsf";
	// Log System
	public static final String SYS_MAIN_LOG = "SYS_MAIN_LOG";
	public static final String SYS_LOG = "SYS_LOG";
	public static final String ROLE_LOG = "ROLE_LOG";
	// Main System
	public static final String SYS_MAIN_BUDGET = "SYS_MAIN_BUDGET";
	public static final String SYS_MAIN_PURCHASING = "SYS_MAIN_PURCHASING";

	public static final String SYS_MAIN_STOCK = "SYS_MAIN_STOCK";
	//Stock Sub System
	public static final String SYS_STOCK_STOCK = "SYS_STOCK_STOCK";
	//StockRole
	public static final String ROLE_STOCK_ADMIN = "ROLE_STOCK_ADMIN";
	public static final String ROLE_STOCK_MAT = "ROLE_STOCK_MAT";
	public static final String ROLE_STOCK_MATGROUP = "ROLE_STOCK_MATGROUP";

	// Budget Sub System
	public static final String SYS_BUDGET_BUDGET = "SYS_BUDGET_BUDGET";
	// Budget Role
	public static final String ROLE_BUDGET_ADMIN = "ROLE_BUDGET_ADMIN";
	public static final String ROLE_BUDGET_MAIN = "ROLE_BUDGET_MAIN";
	public static final String ROLE_BUDGET_SUB = "ROLE_BUDGET_SUB";
	public static final String ROLE_BUDGET_TRANSFER_REQUEST = "ROLE_BUDGET_TRANSFER_REQUEST";
	public static final String ROLE_BUDGET_TRANSFER_APPROVE_MAIN = "ROLE_BUDGET_TRANSFER_APPROVE_MAIN";
	public static final String ROLE_BUDGET_TRANSFER_APPROVE_SUB = "ROLE_BUDGET_TRANSFER_APPROVE_SUB";
	public static final String ROLE_BUDGET_EXPENSE = "ROLE_BUDGET_EXPENSE";
	public static final String ROLE_BUDGET_CAPITAL = "ROLE_BUDGET_CAPITAL";
	// Purchasing Sub System
	public static final String SYS_PURCHASING_PURCHASING = "SYS_PURCHASING_PURCHASING";
	public static final String SYS_PURCHASING_CANTEEN = "SYS_PURCHASING_CANTEEN";
	public static final String SYS_PURCHASING_SPORT = "SYS_PURCHASING_SPORT";
	public static final String SYS_PURCHASING_NURSERY = "SYS_PURCHASING_NURSERY";
	public static final String SYS_PURCHASING_HOTEL = "SYS_PURCHASING_HOTEL";
	public static final String SYS_PURCHASING_MEDICAL = "SYS_PURCHASING_MEDICAL";
	public static final String SYS_PURCHASING_SWIMMING = "SYS_PURCHASING_SWIMMING";
	// Purchasing Role
	public static final String ROLE_PURCHASING_ADMIN = "ROLE_PURCHASING_ADMIN";
	public static final String ROLE_PURCHASING_PR = "ROLE_PURCHASING_PR";
	public static final String ROLE_PURCHASING_PO = "ROLE_PURCHASING_PO";
	public static final String ROLE_PURCHASING_GR = "ROLE_PURCHASING_GR";
	public static final String ROLE_PURCHASING_GI = "ROLE_PURCHASING_GI";
	public static final String ROLE_PURCHASING_FF_PO = "ROLE_PURCHASING_FF_PO";
	public static final String ROLE_PURCHASING_FF_GR = "ROLE_PURCHASING_FF_GR";
	public static final String ROLE_PURCHASING_NURSERY_CHILDEN = "ROLE_PURCHASING_NURSERY_CHILDEN";
	public static final String ROLE_PURCHASING_MAT = "ROLE_PURCHASING_MAT";
	public static final String ROLE_PURCHASING_TRANSFER = "ROLE_PURCHASING_TRANSFER";
	public static final String ROLE_PURCHASING_RETURN = "ROLE_PURCHASING_RETURN";
	public static final String ROLE_PURCHASING_BANQUET = "ROLE_PURCHASING_BANQUET";
	public static final String ROLE_PURCHASING_SWIMMING = "ROLE_PURCHASING_SWIMMING";
	public static final String ROLE_PURCHASING_VENDOR = "ROLE_PURCHASING_VENDOR";
	// Warehouse Code
//	public static final String WH_CANTEEN = "WH_CANTEEN";
//	public static final String WH_SPORT = "WH_SPORT";
//	public static final String WH_NURSERRY = "WH_NURSERRY";
//	public static final String WH_HOTEL = "WH_HOTEL";
//	public static final String WH_MEDICAL = "WH_MEDICAL";
//	public static final String WH_SWIMMING = "WH_SWIMMING";
	public static final String WH_UBON = "WH_UBON";
	public static final String WH_UDON = "WH_UDON";
	public static final String WH_NAKORN = "WH_NAKORN";
	public static final String WH_SURAT = "WH_SURAD";
	public static final String WH_HUAHIN = "WH_HUAHIN";
	public static final String WH_CHIANGMAI = "WH_CHIANGMAI";
	public static final String WH_HADYAI = "WH_HADYAI";
	public static final String WH_PUKET = "WH_PUKET";
	public static final String WH_PHITSANULOK = "WH_PHITSANULOK";
	public static final String WH_MAHAMEK = "WH_MAHAMEK";	
	//Storage Location Code
	public static final String SL_CANTEEN_MAIN = "CANTEEN_MAIN";
	public static final String SL_CANTEEN_CANTEEN = "CANTEEN_CANTEEN";
	public static final String SL_HOTEL_MAIN = "HOTEL_MAIN";
	public static final String SL_HOTEL_FRONT = "HOTEL_FRONT";
	// Formatter
	public static final DecimalFormat DF_MATGROUP_CODE = new DecimalFormat("00");
	public static final DecimalFormat DF_MAT_CODE = new DecimalFormat("0000");
	//Budget Level
	public static final String LEVEL_1 = "Level_1";
	public static final String LEVEL_2 = "Level_2";
	public static final String LEVEL_3 = "Level_3";
	public static final String LEVEL_4 = "Level_4";
	
}
