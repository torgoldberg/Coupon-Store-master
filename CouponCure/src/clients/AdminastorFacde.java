package clients;

import java.util.List;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.javabeans.Company;
import clients.javabeans.Coupon;
import clients.javabeans.Customer;
import dao.CompanyDAO;
import dao.CouponDAO;
import dao.CustomerDAO;
import dao.db.CompanyDAODB;
import dao.db.CouponDAODB;
import dao.db.CustomerDAODB;
/**
 * Admin access class
 */
public class AdminastorFacde extends CouponClientFacade {
	
	/**
	 * PRIVATE DAODB ATTRIBUTES
	 */
	private CouponDAO couponDAO = null;
	private CustomerDAO customerDAO = null;
	private CompanyDAO companyDAO = null;
	private final static String ADMINASTOR_NAME = "admin";
	private final static String ADMINASTOR_PASSWORD = "1234";
	
	/**
	 * CONSTRCOTURE
	 */
	private AdminastorFacde() throws CouponServerException {
		super();
		this.couponDAO = new CouponDAODB();
		this.customerDAO = new CustomerDAODB();
		this.companyDAO = new CompanyDAODB();
	}

	/**
	 * LOGS IN AS ADMIN IF PASSWORD AND NAME ARE CORRECT
	 * @param name
	 * @param password
	 * @return AdminastorFacde
	 * @throws CouponServerException
	 * @throws CouponException
	 */
	public static CouponClientFacade login(String name, String password) throws CouponServerException, CouponException {               
		if (name.equalsIgnoreCase(ADMINASTOR_NAME)&&password.equals(ADMINASTOR_PASSWORD)) {
			return new AdminastorFacde();
		}
		throw new CouponException("your password isn't right");
	}
	
	/**
	 * CREATES NEW COMPANY WITH AUTO GENERATED ID AND UNIQE NAME
	 * @throws CouponException 
	 */
	public void addCompany(Company newCompany) throws CouponServerException, CouponException {
		
		for (Company company : companyDAO.getAllCompany()) {
			///if (company.getIdCompany()==newCompany.getIdCompany()) throw new CouponException("the id is exist");
			if (company.getCompName().equalsIgnoreCase(newCompany.getCompName())) throw new CouponException("the name use in another company");
		}
		companyDAO.createCompany(newCompany);
	}
	
	/**
	 * REMOVE A SPECIFIED COMPANY,ALL THE COUPONS OF THIS COMPANY AND ALL THE COUPONS THAT WERE COUGHT BY CUSTOMERS
	 * @throws CouponException , CouponServerException
	 */
	public void removeCompany(Company company) throws CouponServerException, CouponException {
		//take the company from DB and throw CouponException if isn't exist
		company = companyDAO.getCompany(company.getIdCompany());
			companyDAO.removeCompany(company);
	}
	
	/**
	 * UPDATES SPECIFIED COMPANY FIELDS IN THE DATA BASE
	 * @throws CouponException, CouponServerException 
	 */
	public void updateCompany (Company company) throws CouponServerException, CouponException {
		Company oldCompany = companyDAO.getCompany(company.getIdCompany());
		if (oldCompany==null) throw new CouponException("this id Company isn't exist");
		if (company.getCompName().equalsIgnoreCase(oldCompany.getCompName())){
			companyDAO.updateCompany(company);
		}else {
			throw new CouponException("you cann't update your compny name");
		}
	}
	
	/**
	 * @return A LIST OF ALL THE COMPANIES FROM THE DATA BASE
	 * @throws CouponServerException
	 */

	public List<Company> getAllCompanies() throws CouponServerException{
		
		return companyDAO.getAllCompany();
	}
	
	/**
	 * 
	 * @param idCompany
	 * @return  SPECIFIED COMPANY BY THE COMPANY ID FROM THE DATA BASE
	 * @throws CouponServerException
	 * @throws CouponException
	 */
	
	public Company getCompany(long idCompany) throws CouponServerException, CouponException {
		return companyDAO.getCompany(idCompany);
	}
	
	/**
	 * INSERT NEW CUTOMER INFO IN TO CUSTOMER TABLES
	 * @param newCustomer
	 * @throws CouponServerException
	 * @throws CouponException
	 */
	public void addCustomer(Customer newCustomer) throws CouponServerException, CouponException {
		for (Customer customer : customerDAO.getAllCustomer()) {
			if (customer.getIdCustomer()==newCustomer.getIdCustomer()) throw new CouponException("the id is exist");
			if (customer.getCustName().equalsIgnoreCase(newCustomer.getCustName())) throw new CouponException("the name use in another customer");
		}
		customerDAO.createCustomer(newCustomer);
	}
	
	/**
	 * REMOVE INFO ACOUT A SPECIFIED CUSTOMER FROM CUSTOMER TABLE
	 * @throws CouponServerException 
	 */
	public void removeCustomer(Customer customer) throws CouponServerException {
		List <Coupon> parchaseCoupons = customerDAO.getCoupons(customer.getIdCustomer());
		for (Coupon coupon : parchaseCoupons) {
			coupon.setAmount(coupon.getAmount()+1);
			couponDAO.updateCoupon(coupon);
		}
		customerDAO.removeCustomer(customer);
	}
	
	/**
	 * UPDATES CUSTOMER INFO IN THE DATA BASE
	 * @throws CouponException , CouponServerException
	 */
	public void updateCustomr(Customer updateCustomer) throws CouponServerException, CouponException {
		Customer oldCustomer = customerDAO.getCustomer(updateCustomer.getIdCustomer());
		if (oldCustomer==null) throw new CouponException("this id customer isn't exist");
		if (updateCustomer.getCustName().equalsIgnoreCase(oldCustomer.getCustName())){
			customerDAO.updateCustomer(updateCustomer);;
		}else {
			throw new CouponException("you cann't update your customer name");
		}
	}
	
	/**
	 * @return  A COLLECTION OF ALL THE EXISTING CUSTOMERS
	 * @throws CouponServerException
	 * @throws CouponException
	 */

	public List<Customer> getAllCustomers() throws CouponServerException, CouponException{
		return customerDAO.getAllCustomer();
	}
	
	/**
	 * 
	 * @param customrId
	 * @return APECIFIED CUSTOMER BY THE CUSTOMER ID
	 * @throws CouponServerException
	 */
	
	public Customer getCustomer(long customrId) throws CouponServerException {
		return customerDAO.getCustomer(customrId);
		
	}


}
