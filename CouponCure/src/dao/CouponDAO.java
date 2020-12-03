package dao;

import java.sql.Date;
import java.util.List;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.javabeans.Coupon;
import clients.javabeans.CouponType;

public interface CouponDAO {
	
	public void createCoupon(Coupon coupon)throws CouponServerException;
	public void removeCoupon(Coupon coupon)throws CouponServerException;
	public void updateCoupon(Coupon coupon)throws CouponServerException;
	public Coupon getCoupon(Long id)throws CouponServerException;
	public List <Coupon> getAllCoupons()throws CouponServerException;
	public List<Coupon> getCouponsByType(CouponType coupon)throws CouponServerException;
	boolean checkTitleOfAllCoupons(Coupon coupon) throws CouponServerException, CouponException;
	public void removeCouponByDate(Date sQLDate) throws CouponServerException;
	
}
