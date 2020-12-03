package clients;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.javabeans.Company;
import clients.javabeans.Coupon;
import clients.javabeans.CouponType;
import dao.CompanyDAO;
import dao.CouponDAO;
import dao.db.CompanyDAODB;
import dao.db.CouponDAODB;
/**
 * Companies access class
 */
public class CompanyFacade extends CouponClientFacade {

	private CouponDAO couponDAO = null;
	private static CompanyDAO companyDAO = null;
	private Company company = null; 
	

	private CompanyFacade(String name) throws CouponServerException, CouponException {
		this.company = companyDAO.getCompanyByName(name);
		this.couponDAO = new CouponDAODB();
	}

	/**
	 * LOGS IN AS COMPANY IF USER NAME AND PASSWORD ARE CORRECT
	 * @param name
	 * @param password
	 * @return CompanyFacade
	 * @throws CouponServerException
	 * @throws CouponException
	 */
	public static CouponClientFacade login(String name, String password) throws CouponServerException, CouponException {
		companyDAO = new CompanyDAODB();
		if (companyDAO.login(name, password)) {
			return new CompanyFacade(name);
		}
		throw new CouponException("your password or user name isn't right");
		
	}
	
	/**
	 * CREATE NEW COUPON WITH UNIQE TITLE
	 * INSERT COUPON INFO INTO COUPON TABLE
	 * INSERT COUPON AUTO GENERATED ID AND COMPANY ID TO THE COUSTOMER TABLE	
	 * @param coupon
	 * @throws CouponException
	 * @throws CouponServerException
	 */
	public void addCoupon (Coupon coupon) throws CouponException, CouponServerException {
		
		if (company.getIdCompany()!=coupon.getIdCompany()) throw new CouponException("You do not have permission to add coupon to ID Company: " + coupon.getIdCompany());
		couponDAO.checkTitleOfAllCoupons(coupon);
		couponDAO.createCoupon(coupon);
		company.addCoupon(coupon);
	}
	
	/**
	 * CREATE NEW COUPON WITH UNIQE TITLE
	 * INSERT COUPON INFO INTO COUPON TABLE
	 * INSERT COUPON AUTO GENERATED ID AND COMPANY ID TO THE COUSTOMER TABLE
	 * @param coupon
	 * @throws CouponServerException
	 * @throws CouponException
	 */
	public void removeCoupon (Coupon coupon) throws CouponServerException, CouponException {
		if (coupon.getIdCompany()!=company.getIdCompany()) throw new CouponException("You do not have permission for this coupon");
		couponDAO.removeCoupon(coupon);
		company.remove(coupon);
		throw new CouponException("the coupon:" + coupon.getTitle() + "is delete from the system");
	}
	
	/**
	 * UPDATES COUPON START AND END DATE
	 * @param coupon
	 * @throws CouponServerException
	 * @throws CouponException
	 */
	public void updateCoupon (Coupon coupon) throws CouponServerException, CouponException {
		if (coupon.getIdCompany()!=company.getIdCompany()) throw new CouponException("You do not have permission for this coupon");
		boolean x = true;
		Coupon theOldCoupon = coupon;
		for (Coupon oldCoupon : company.getCoupons()){
			if (oldCoupon.getTitle().equalsIgnoreCase(coupon.getTitle())&& oldCoupon.getIdCoupon()==coupon.getIdCoupon()) {
				x = false;
				theOldCoupon=oldCoupon;
			};
		}
		if (x) {
			throw new CouponException("this coupon isn't exist in the system");
		}
		company.remove(theOldCoupon);
		company.addCoupon(coupon);
		couponDAO.updateCoupon(coupon);
	}
	
	/**
	 * @return INFORMATION ABOUT the company that uses
	 */
	public Company getcompany() {
		return company;
	}
	
	/**
	 * @return  A COUPON LIST OF A SPECIFIED COMPANY
	 * @throws CouponServerException
	 */
	public List<Coupon> getAllCoupons() throws CouponServerException{
		return company.getCoupons();
	}
	
	/**
	 * @param couponType
	 * @return  A LIST OF COUPONS BY THE SPECIFIED TYPE
	 * @throws CouponServerException
	 */
	public List<Coupon> getCouponsByType(CouponType couponType) throws CouponServerException{
		List<Coupon> couponsByType = new ArrayList<Coupon>();
		for (Coupon coupon : company.getCoupons()) {
			if(coupon.getCouponType().toString().equals(couponType.toString())) {
				couponsByType.add(coupon);
			}
		}
		return couponsByType;
	}
	
	/**
	 * @param maxPrice
	 * @return  A LIST OF COUPONS BY THE SAME PRICE OR LESS
	 * @throws CouponServerException
	 */
	public List<Coupon> getCouponsByMaxPrice(double maxPrice) throws CouponServerException{
		List<Coupon> couponsByPrice = new ArrayList<Coupon>();
		for (Coupon coupon : company.getCoupons()) {
			if(coupon.getPrice() <= maxPrice) {
				couponsByPrice.add(coupon);
			}
		}
		return couponsByPrice;
	}
	
	/**
	 * 
	 * @param minDate
	 * @return A COLLECTION OF A COMPANY COUPON THAT WILL BE VALID ON A SPECIFIED DAY
	 * @throws CouponException
	 * @throws CouponServerException
	 */
	public List<Coupon> getCouponsAfterYourDate(Date minDate) throws CouponException, CouponServerException {
		List<Coupon> couponsByDate = new ArrayList<Coupon>();
		for (Coupon coupon : company.getCoupons()) {
			if(coupon.getEndDate().after(minDate)) {
				couponsByDate.add(coupon);
			}
		}
		return couponsByDate;
	}
	
	

}
