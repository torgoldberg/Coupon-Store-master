package dao;

import java.util.List;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.javabeans.Coupon;
import clients.javabeans.Customer;

public interface CustomerDAO {
	
	public void createCustomer(Customer customer)throws CouponServerException;
	public void removeCustomer(Customer customer)throws CouponServerException;
	public void updateCustomer(Customer customer)throws CouponServerException;
	public  List <Customer> getAllCustomer()throws CouponServerException, CouponException;
	public   List <Coupon> getCoupons(long id)throws CouponServerException;
	public boolean login(String custName, String password)throws CouponServerException;
	Customer getCustomer(long id) throws CouponServerException;
	Customer getCustomerByName(String name) throws CouponServerException;
	void purchaseCoupon(long idCoupon, long idCustomer) throws CouponServerException, CouponException;
	List<Long> getIdCoupons(long idCustomer) throws CouponServerException;

}
