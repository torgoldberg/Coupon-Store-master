package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

	/**
	 * This method saves the inputStream into the folderPah/fileName location into a unique file name.
	 * The file will be created only if the stream is not empty. 
	 */
	public int writeToFile(InputStream inputStream, String folderPath, String fileName) throws IOException {

		int read = 0;
		byte[] bytes = new byte[1024];
		int totalBytesRead = 0;
		
		if ( (read = inputStream.read(bytes)) == -1) {
			return 0;
		}

		System.out.println(folderPath);
		System.out.println(fileName);
		OutputStream outputStream = null;			
		File folder = new File(folderPath);
		folder.mkdirs();
		File file = new File(folder,fileName);
		
		outputStream = new FileOutputStream( file );
		do {
			outputStream.write(bytes, 0, read);
			totalBytesRead += read;
		} while ((read = inputStream.read(bytes)) != -1) ;

		outputStream.flush();
		outputStream.close();
				
		return totalBytesRead;

	}

	public File getImage(String name) {
		Constants constants = Constants.getInstance;
		File image = new File(constants.getUPLOAD_FOLDER() + name);	
		return image;
	}
	
}
