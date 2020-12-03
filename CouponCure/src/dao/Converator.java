package dao;

import java.util.Date;

public class Converator {
	
	
	public static Date convoretorLongToDate(long date) {
		return new Date(date);
	}
	
	public static Date converetorLongToSQLDate(long time) {
		return 	new java.sql.Date(time);
	}

}
