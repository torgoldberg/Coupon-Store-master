package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import Exceptions.CouponException;
import Exceptions.CouponServerException;

public class Constants {
	
	public static Constants getInstance = new Constants();

	private String UPLOAD_FOLDER;
	
	private Constants() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream propertiesFile = classLoader.getResourceAsStream("application.properties");
		Properties properties = new Properties();

		try {
			
			properties.load(propertiesFile);
			propertiesFile.close();
			UPLOAD_FOLDER = properties.getProperty("application.sourceFolder");

		} catch (IOException e) {
			try {
				throw new CouponServerException("eror in loading source folder", e);
			} catch (CouponServerException e1) {
			}
		}
	}

	 public String getUPLOAD_FOLDER() {
		return UPLOAD_FOLDER;
	}

	
		

}
