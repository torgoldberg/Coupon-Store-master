package Exceptions;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

public class ExeptionStorage implements Serializable {
	
	private String exceptionStuck;
	private String message;
	private Long time;
	
	public ExeptionStorage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExeptionStorage(Exception exception, String message , Long erorsFileName) {
		super();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		this.exceptionStuck = sw.toString();
		this.message = message;
		this.time = erorsFileName;
	}

	public String getExceptionStuck() {
		return exceptionStuck;
	}

	public void setExceptionStuck(String exceptionStuck) {
		this.exceptionStuck = exceptionStuck;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}




	

}
