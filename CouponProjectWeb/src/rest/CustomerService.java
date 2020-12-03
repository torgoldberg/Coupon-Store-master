package rest;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.CustomerFacade;
import clients.javabeans.Coupon;
import db.Description;
import db.Income;
import filters.FinalAtributte;
import proxy.BusinessDelegate;
import utils.FileUtils;

@Path("/customerService")
public class CustomerService {


	private FileUtils fileUtils;
	private CustomerFacade customerFacade;
	private BusinessDelegate businessDelegate;
	
	public CustomerService(@Context HttpServletRequest request ) {
		HttpSession httpSession = request.getSession();
		this.customerFacade = (CustomerFacade) httpSession.getAttribute(FinalAtributte.COUPON_CLIENT_FACADE);
		this.fileUtils = new FileUtils();
		this.businessDelegate = (BusinessDelegate) httpSession.getAttribute(FinalAtributte.BUSINESS_DELEGATE);

	}
	
	@Path("/getAllCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public List<Coupon> getAllCoupons(@Context HttpServletResponse httpServletResponse)  {
		List<Coupon> coupons = null;
		try {
			coupons = customerFacade.getAllCoupons();
		} catch (CouponServerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}		
		return coupons;
	}
	
	@Path("/purchaseCoupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void purchaseCoupon(Coupon coupon, @Context HttpServletResponse httpServletResponse) {
		try {
			customerFacade.purchaseCoupon(coupon);
			businessDelegate.storeIncome(new Income(customerFacade.getCustomer().getCustName(), new Date(), coupon.getPrice(), Description.CUSTOMER_PURCHASE));
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		} catch (CouponException | CouponServerException|NullPointerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());

		}
	}
	

	
	@Path("/getAllPurchaseCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public List<Coupon> getAllPurchaseCoupons() {
		List<Coupon> couponsList = customerFacade.getAllPurchaseCoupons();
		return couponsList;
		
	}
	@Path("/getAllpurchaseCouponsSortByType")
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public List<Coupon> getAllpurchaseCouponsSortByType() {
		List<Coupon> couponsList = customerFacade.getAllpurchaseCouponsSortByType();
		return couponsList;
				
	}
	
	@Path("/getAllpurchaseCouponsByPrice/{maxPrice}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<Coupon> getAllpurchaseCouponsByPrice(@PathParam("maxPrice") double price) {
		List<Coupon> couponsList = customerFacade.getAllpurchaseCouponsByPrice(price);
		return couponsList;		
	}

	@GET
	@Path("/image/{name}")
	@Produces(MediaType.MULTIPART_FORM_DATA)	
	public File image ( @PathParam("name") String name)  {	
		return fileUtils.getImage(name);
	}
	
	@Path("/viewIncomeByCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	@POST	
	public Collection<Income> viewIncomeByCustomer(){
		return businessDelegate.viewIncomeByCustomer(customerFacade.getCustomer().getCustName());	
	}
}
