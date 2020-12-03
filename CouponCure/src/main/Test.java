package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.AdminastorFacde;
import clients.CompanyFacade;
import clients.CustomerFacade;
import clients.javabeans.Company;
import clients.javabeans.Coupon;
import clients.javabeans.CouponType;
import clients.javabeans.Customer;

public class Test {

	public static void main(String[] args) {
		
		
		CouponSystem couponSystem =CouponSystem.getCouponSystem();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		AdminastorFacde adminastorFacde = null;
		CustomerFacade customerFacade = null;
		CompanyFacade companyFacade = null;
		
		
		Company company = new Company( "asd", "1234", "fdgsdhf@fhhfh");
		Company company2 = new Company( "jonbrais", "4444", "fdfghgsdhf@fhhfh");
		Company company5 = new Company( "goodCompany", "1234", "dgadf");
		Customer customer = null;
		
		try {
			customer = new Customer("itay", "1234");
		} catch (CouponServerException e2) {
			System.out.println(e2.getMessage());
		}
		

		try {                             //login as admin
			adminastorFacde = (AdminastorFacde) couponSystem.login("admin", "1234", "ADMINASTOR");
		} catch (CouponException|CouponServerException e) {
			System.out.println(e.getMessage());
		} 
		
		                        //adding companies and customers to Database
		 try {
			adminastorFacde.addCompany(company);
		} catch (CouponServerException|CouponException e1) {
			System.out.println(e1.getMessage());
		}
		 try {
			adminastorFacde.addCompany(company2);
		} catch (CouponServerException|CouponException e1) {
			System.out.println(e1.getMessage());
		}
		 try {
				adminastorFacde.addCompany(company5);
			} catch (CouponServerException|CouponException e1) {
				System.out.println(e1.getMessage());
			}
		 try {
			adminastorFacde.addCustomer(customer);
		} catch (CouponServerException|CouponException e1) {
			System.out.println(e1.getMessage());
		}


		 try {
				adminastorFacde.addCustomer(customer);
			} catch (CouponServerException|CouponException e1) {
				System.out.println(e1.getMessage());
			}
		 
		
		
			
			
			try {
				System.out.println();
				System.out.println("shutdown thr system");
				couponSystem.shutdown();
			} catch (CouponServerException e) {
				System.out.println(e.getMessage());
				}
			
		
	}

	private static void printCustomer(Customer customer) {
		System.out.println("**********************");
		System.out.print(customer.getIdCustomer()+ ": ");
		System.out.println("customer name: : "+ customer.getCustName());
		System.out.println("**********************");
		System.out.println();		
	}

	private static void printCompany(Company company) {
		System.out.println("**********************");
		System.out.print(company.getIdCompany()+": ");
		System.out.print("company name: " + company.getCompName());
		System.out.println(". email: " + company.getEmail());
		System.out.println("**********************");
		System.out.println();			
	}

	private static void printAllCustomers(AdminastorFacde adminastorFacde) throws CouponServerException, CouponException {
		for(Customer customer  : adminastorFacde.getAllCustomers()) {
			printCustomer(customer);
	
		}		
	}

	private static void printAllCompanies(AdminastorFacde adminastorFacde) throws CouponServerException {
		for(Company company : adminastorFacde.getAllCompanies()) {
			printCompany(company);
		}
	}

	private static void printCoupons(List<Coupon> coupons) {
		
		for (Coupon coupon3 : coupons) {
			System.out.println("**********************");
			System.out.println("id: " + coupon3.getIdCoupon());
			System.out.println("title: "+ coupon3.getTitle());
			System.out.println("price: " + coupon3.getPrice());
			System.out.println("type: " + coupon3.getCouponType().toString());
			System.out.println("IdCompany: " + coupon3.getIdCompany());
			System.out.println("**********************");
			System.out.println();

		}		
	}
	

}
