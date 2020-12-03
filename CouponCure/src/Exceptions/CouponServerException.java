package Exceptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
*Writes all the errors that are relevant to the server in the file on
 *
 */
public class CouponServerException extends Exception {

	
	private static final long serialVersionUID = 1L;	

	public CouponServerException(String message, Exception exception) throws CouponServerException {
		super(message);
		Long erorsFileName=new Date().getTime();
		String erorsLibName = getLocation();
		ExeptionStorage exeptionStorage = new ExeptionStorage(exception, message, erorsFileName);
		exception.printStackTrace();
		GetPostExeption getPostExeption = GetPostExeption.getInstance();
		getPostExeption.addExeption(exeptionStorage);
		
		File folder = new File(erorsLibName);
		folder.mkdirs();	
		
		ObjectOutputStream objectOutputStream = null;
		try {
		FileOutputStream fileOutputStream = new FileOutputStream(erorsLibName+erorsFileName+".dat");
		objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(exeptionStorage);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CouponServerException("eror in the report to the server");

		} finally{
			try {
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new CouponServerException("eror in the report to the server");
			}
		}
	}

	public static String getLocation() throws CouponServerException   {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream propertiesFile = classLoader.getResourceAsStream("application.properties");
		Properties properties = new Properties();
		try {
			properties.load( propertiesFile );
			propertiesFile.close();
			return properties.getProperty("application.erorsAddress");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CouponServerException("eror in the report to the server");
		}		
	}
	

	/**
	 * RETURN AN INFORMATIVE MESSAGE
	 */
	public CouponServerException(String message) {
		super(message);
	}

}
