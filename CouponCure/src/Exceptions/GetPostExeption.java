package Exceptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class GetPostExeption {
	
	List<ExeptionStorage> exeptionStorages;
	private static GetPostExeption getPostExeption = new GetPostExeption();
	
	public static GetPostExeption getInstance( ) {
		return getPostExeption;
	}

	private GetPostExeption()  {
		super();
		if(exeptionStorages==null) {
			ObjectInputStream objectInputStream = null;
			exeptionStorages = new ArrayList<ExeptionStorage>();
			try {
				exeptionStorages = getAllFiles();
			} catch (ClassNotFoundException | CouponServerException | IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if (objectInputStream != null) objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
		
	}

	
	private List<ExeptionStorage> getAllFiles() throws CouponServerException, ClassNotFoundException, IOException{
		
		File file = new File(CouponServerException.getLocation());
		if (file.exists() && file.isDirectory()) {
			for (File oneFile: file.listFiles()) {
				FileInputStream fileInputStream = new FileInputStream(CouponServerException.getLocation()+"/"+oneFile.getName());
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				Object object = objectInputStream.readObject();
				if (object instanceof ExeptionStorage) {
					exeptionStorages.add((ExeptionStorage)object);
				}
			}
			return exeptionStorages;
		}
		return null;
	}
	
	
	public void addExeption(ExeptionStorage exeptionStorage) {
		this.exeptionStorages.add(exeptionStorage);
	}

	public List<ExeptionStorage> getExeptionStorages() {
		return exeptionStorages;
	}
	

}
