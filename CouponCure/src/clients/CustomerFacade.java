package clients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.javabeans.Coupon;
import clients.javabeans.Customer;
import clients.javabeans.SortByPrice;
import clients.javabeans.SortByType;
import dao.CouponDAO;
import dao.CustomerDAO;
import dao.db.CouponDAODB;
import dao.db.CustomerDAODB;
/**
 * Customers access class
 */
public class CustomerFacade extends CouponClientFacade {


	private static CustomerDAO customerDAO = null;
	private CouponDAO couponDAO = null;
	private Customer customer = null;
	//private static boolean lockPurchase = true;


	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	private CustomerFacade(String name) throws CouponServerException {
		super();
		customer = customerDAO.getCustomerByName(name);
		couponDAO = new CouponDAODB();
	}

	/**
	 * LOGS IN AS CUSTOMER IF USER NAME AND PASSWORD ARE CORRECT
	 * @param name
	 * @param password
	 * @return CustomerFacade
	 * @throws CouponServerException
	 * @throws CouponException
	 */
	public static CouponClientFacade login(String name, String password) throws CouponServerException, CouponException {
		customerDAO = new CustomerDAODB();
		if (customerDAO.login(name, password)) {
			return new CustomerFacade(name);
		}
		throw new CouponException("your password or user name isn't right");
	}

	/**
	 * LOGS IN AS CUSTOMER IF USER NAME AND PASSWORD ARE CORRECT 
	 * @param coupon
	 * @throws CouponException
	 * @throws CouponServerException
	 */
	public void purchaseCoupon(Coupon coupon) throws CouponException, CouponServerException {
		coupon = couponDAO.getCoupon(coupon.getIdCoupon());
		if (coupon == null)
			throw new CouponException("the coupon isn't exist");
		for (Coupon pastParcaseCoupon : customer.getCoupons()) {
			if (pastParcaseCoupon.getIdCoupon() == coupon.getIdCoupon())
				throw new CouponException("You can not purchase the same coupon twice");
		}
		// lock this segment for to Prevent some purchase to same coupon together
		//try {
			//cheakAndLockPurchase();
			//coupon = couponDAODB.getCoupon(coupon.getIdCoupon());
			if (coupon.getAmount() < 1)
				throw new CouponException("the coupon out of inventory");
			customerDAO.purchaseCoupon(coupon.getIdCoupon(), customer.getIdCustomer());
			coupon.setAmount(coupon.getAmount() - 1);
			couponDAO.updateCoupon(coupon);
			customer.addCoupon(coupon);
		//} finally {
			//lockPurchase = true;
		//}
	}

	/**
	 * @return  A LIST OF ALL COUPONS THAT WERE PURCHASED BY A SPECIFIED CUSTOMER
	 */
	public List<Coupon> getAllPurchaseCoupons() {
		return customer.getCoupons();
	}

	/**
	 * @return A LIST OF A CUSTOMER COUPON OF A SPECIFIED TYPE THROWS COUPON
	 */
	public List<Coupon> getAllpurchaseCouponsSortByType() {
		Collections.sort(customer.getCoupons(), new SortByType());
		return customer.getCoupons();
	}

	/**
	 * @param price
	 * @return A LIST OF A CUSTOMER COUPON OF A APECIFIED PRICE
	 */
	public List<Coupon> getAllpurchaseCouponsByPrice(double price) {
		Collections.sort(customer.getCoupons(), new SortByPrice());
		List<Coupon> couponsUntilPrice = new ArrayList<Coupon>();
		for (Coupon coupon : customer.getCoupons()) {
			if (price >= coupon.getPrice()) {
				couponsUntilPrice.add(coupon);
			} else {
				continue;
			}
		}
		return couponsUntilPrice;

	}
	/*
	 // LOCK A SPECIFIED COUPON SOW IT WONT B able to be PURCHASE at the same time
	 * 
	 * 
	 * private synchronized static void cheakAndLockPurchase() { if (lockPurchase) {
	 * lockPurchase = false; } else { try { Thread.sleep(100); } catch
	 * (InterruptedException e) { } cheakAndLockPurchase(); } }
	 */
	
	public List<Coupon> getAllCoupons() throws CouponServerException {
		List<Coupon> coupons = couponDAO.getAllCoupons();		
		return coupons;

	}
}
