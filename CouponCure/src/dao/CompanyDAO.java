package dao;

import java.util.List;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.javabeans.Company;
import clients.javabeans.Coupon;

public interface CompanyDAO {
	
	public void createCompany(Company company) throws CouponServerException;
	public void removeCompany(Company company) throws CouponServerException;
	public void updateCompany(Company company) throws CouponServerException;
	public Company getCompany(long id)throws CouponServerException, CouponException;
	public Company getCompanyByName(String name)throws CouponServerException, CouponException;
	public List <Company> getAllCompany()throws CouponServerException;
	public boolean login(String compName, String password)throws CouponServerException;
	List<Coupon> getCouponsByCompany(long compId) throws CouponServerException;
	
}
