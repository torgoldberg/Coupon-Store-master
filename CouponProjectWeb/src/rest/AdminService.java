package rest;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import Exceptions.ExeptionStorage;
import Exceptions.GetPostExeption;
import clients.AdminastorFacde;
import clients.javabeans.Company;
import clients.javabeans.Customer;
import db.Income;
import filters.FinalAtributte;
import proxy.BusinessDelegate;


@Path("/adminService")
public class AdminService {
	private AdminastorFacde adminastorFacde;
	private BusinessDelegate businessDelegate;

	public AdminService (@Context HttpServletRequest request ) {
		HttpSession httpSession = request.getSession();
		this.adminastorFacde = (AdminastorFacde) httpSession.getAttribute(FinalAtributte.COUPON_CLIENT_FACADE);
		this.businessDelegate = (BusinessDelegate) httpSession.getAttribute(FinalAtributte.BUSINESS_DELEGATE);
	}
	
	@Path("/addCompany")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void addCompany(Company newCompany, @Context HttpServletResponse httpServletResponse)  {
		try {
			checkCompany(newCompany);
			adminastorFacde.addCompany(newCompany);
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		} catch (CouponServerException | CouponException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
	}
		
	private void checkCompany(Company company) throws CouponException {
		
		try {
			if(company.getCompName()==null||company.getEmail()==null||company.getPassword()==null||
			company.getCompName().trim().equals("")||company.getEmail().trim().equals("")||company.getPassword().trim().equals("")) {
				throw new CouponException("All details must be given");
			};
		}catch (NullPointerException e) {
			throw new CouponException("All details must be given");
		}
	}

	@Path("/removeCompany")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void removeCompany(Company company, @Context HttpServletResponse httpServletResponse)  {
		try {
			checkCompany(company);
			adminastorFacde.removeCompany(company);
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		} catch (CouponServerException | CouponException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
	}
			
	@Path("/updateCompany")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void updateCompany (Company company, @Context HttpServletResponse httpServletResponse) {
		try {
			checkCompany(company);
			adminastorFacde.updateCompany(company);
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );

		} catch (CouponServerException | CouponException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
	}
				
	@Path("/getAllCompanies")
	
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public List<Company> getAllCompanies(@Context HttpServletResponse httpServletResponse){
		List<Company> companyList = null;
		try {
			companyList = adminastorFacde.getAllCompanies();
		} catch (CouponServerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
		return companyList;

	}
	
	@Path("/getCompany")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@POST				
	public Company getCompany(long idCompany, @Context HttpServletResponse httpServletResponse) {
		try {
			return adminastorFacde.getCompany(idCompany);
		} catch (CouponServerException | CouponException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
		
		return null;
		
	}
	@Path("/addCustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST						
	public void addCustomer(Customer newCustomer, @Context HttpServletResponse httpServletResponse) {
		try {
			checkCustomer(newCustomer);
			adminastorFacde.addCustomer(newCustomer);
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		} catch (CouponServerException | CouponException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
	}
	private void checkCustomer(Customer customer) throws CouponException {
		try {
			if(customer.getCustName()==null||customer.getPassword()==null||
					customer.getCustName().trim().equals("")||customer.getPassword().trim().equals("")) {
				throw new CouponException("All details must be given");
			};
		}catch (NullPointerException e) {
			throw new CouponException("All details must be given");
		}
	}

	@Path("/removeCustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST						
	public void removeCustomer(Customer customer, @Context HttpServletResponse httpServletResponse) {
		try {
			adminastorFacde.removeCustomer(customer);
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		} catch (CouponServerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
	}
	@Path("/updateCustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST								
	public void updateCustomr(Customer updateCustomer, @Context HttpServletResponse httpServletResponse) {
		try {
			adminastorFacde.updateCustomr(updateCustomer);
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		} catch (CouponServerException | CouponException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
	}
	@Path("/getAllCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	@POST								
	public List<Customer> getAllCustomers(@Context HttpServletResponse httpServletResponse) {
		try {
			return adminastorFacde.getAllCustomers();
		} catch (CouponException | CouponServerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
		return null;
		
	}
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("/getCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	@POST						
	public Customer getCustomer(long customrId, @Context HttpServletResponse httpServletResponse) {
		try {
			return adminastorFacde.getCustomer(customrId);
		} catch (CouponServerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
		return null;
		
	}
	@Path("/viewAllIncome")
	@Produces(MediaType.APPLICATION_JSON)
	@POST	
	public Collection<Income> viewAllIncome(){
		return businessDelegate.viewAllIncome();		
	}
	@Path("/viewIncomeByCustomer/{customerName}")
	@Produces(MediaType.APPLICATION_JSON)
	@POST	
	public Collection<Income> viewIncomeByCustomer(@PathParam("customerName") String customerName){
		return businessDelegate.viewIncomeByCustomer(customerName);	
	}
	@Path("/viewIncomeByCompany/{companyName}")
	@Produces(MediaType.APPLICATION_JSON)
	@POST	
	public Collection<Income> viewIncomeByCompany(@PathParam("companyName") String companyName){
		return businessDelegate.viewIncomeByCompany(companyName);		
	}
	@Path("/viewAllErrors")
	@Produces(MediaType.APPLICATION_JSON)
	@POST	
	public List<ExeptionStorage> viewAllErrors(){
		GetPostExeption exeption = GetPostExeption.getInstance();
		return exeption.getExeptionStorages();		
	}
	

}
