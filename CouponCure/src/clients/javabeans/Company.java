package clients.javabeans;

import java.util.ArrayList;
import java.util.List;

import Exceptions.CouponServerException;
import dao.db.CouponDAODB;

public class Company {
	
	/**
	 * ATTRIBUTES
	 */
	private long idCompany;
	private String compName;
	private String password;
	private String email;
	List<Coupon> coupons;
	
	/**
	 * CONSTRUCTORE
	 */
	public Company() {
		super();
	}
	
	public Company(long idCompany, String compName, String password, String email) {
		this(compName,  password,  email);
		this.idCompany = idCompany;
	}
	
	public Company(String compName, String password, String email, ArrayList<Coupon> coupons) {
		this(compName,  password,  email);
		this.coupons = coupons;
	}
	
	public Company(String compName, String password, String email) {
		this.compName = compName;
		this.password = password;
		this.email = email;
	}

	/**
	 *GETTERS AND SETERRS METHODS
	 */
	public long getIdCompany() {
		return idCompany;
	}
	public void setIdCompany(long idCompany) {
		this.idCompany = idCompany;
	}
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Coupon> getCoupons()  {
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
		return "Company [idCompany=" + idCompany + ", compName=" + compName + ", password=" + password + ", email="
				+ email + ", coupons=" + coupons + "]";
	}

	/**
	 * ADD COUPON METHOD
	 */
	public void addCoupon(Coupon coupon) {
		coupons.add(coupon);
	}

	public void remove(Coupon coupon) {
		coupons.remove(coupon);
	}
	

}
