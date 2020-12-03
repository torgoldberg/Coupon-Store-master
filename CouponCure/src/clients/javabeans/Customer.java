package clients.javabeans;

import java.util.ArrayList;
import java.util.List;

import Exceptions.CouponServerException;
import dao.db.CustomerDAODB;

public class Customer {
	
	/**
	 * ATTRIBUTES
	 */
	private long idCustomer;
	private String custName;
	private String password;
	List<Coupon> coupons = new ArrayList<Coupon>();
	CustomerDAODB customerDAODB;
	
	/**
	 * CONSTRUCTOR
	 */
	public Customer() {
		super();
	}
	
	public Customer(String custName, String password) throws CouponServerException {
		super();
		customerDAODB = new CustomerDAODB();
		this.custName = custName;
		this.password = password;
		this.coupons = customerDAODB.getCoupons(idCustomer);
	}
	
	public Customer(long idCustomer, String custName, String password) throws CouponServerException {
		this(custName, password);
		this.idCustomer = idCustomer;
	}

	/**
	 * GETTERS AND SETTERS METHODS
	 */
	public long getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(long idCustomer) {
		this.idCustomer = idCustomer;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List <Coupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(List<Coupon> list) {
		this.coupons = list;
	}
	
	/**
	 * TO STRING METHOD
	 */
	@Override
	public String toString() {
		return "Customer [idCustomer=" + idCustomer + ", custName=" + custName + ", password=" + password + ", coupons="
				+ coupons + ", customerDAODB=" + customerDAODB + "]";
	}

	/**
	 * ADD COUPON METHOD
	 */
	public void addCoupon(Coupon coupon) {
		coupons.add(coupon)	;	
	}

}
