package rest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import utils.Constants;
import utils.FileUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.CompanyFacade;
import clients.javabeans.Company;
import clients.javabeans.Coupon;
import clients.javabeans.CouponType;
import db.Description;
import db.Income;
import filters.FinalAtributte;
import proxy.BusinessDelegate;


@Path("/companyService")
public class CompanyService {

	private CompanyFacade companyFacade;
	private FileUtils fileUtils;
	private BusinessDelegate businessDelegate;
	
	public CompanyService(@Context HttpServletRequest request, @Context HttpServletResponse httpServletResponse ) {
		HttpSession httpSession = request.getSession();
		this.companyFacade = (CompanyFacade) httpSession.getAttribute(FinalAtributte.COUPON_CLIENT_FACADE);
		this.fileUtils = new FileUtils();
		this.businessDelegate = (BusinessDelegate) httpSession.getAttribute(FinalAtributte.BUSINESS_DELEGATE);
	}
			
	
	@Path("/addCoupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void addCoupon (Coupon coupon, @Context HttpServletResponse httpServletResponse) {
		try {
			cheeckCoupon(coupon);
			companyFacade.addCoupon(coupon);
			businessDelegate.storeIncome(new Income(companyFacade.getcompany().getCompName(), new Date(), 100, Description.COMPANY_NEW_COUPON));
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		} catch (CouponException | CouponServerException| NullPointerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}	
	}
	
	private void cheeckCoupon(Coupon coupon) throws CouponException {
		try {
			if(coupon.getAmount()==0 ||coupon.getCouponType()==null||coupon.getEndDate()==null||
					coupon.getIdCompany()==0||coupon.getMessage()==null||coupon.getPrice()==0||
					coupon.getStartDate()==null||coupon.getTitle()==null||
					coupon.getMessage().trim().equals("")||coupon.getTitle().trim().equals("")) {
				throw new CouponException("All details must be given");
			};	
		}catch (NullPointerException e) {
			throw new CouponException("All details must be given");
			}
		
		
	}
	@POST
	@Path("/sendPicture")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void upload(	@FormDataParam("file") InputStream uploadedInputStream /* File bytes content */,
							@FormDataParam("file") FormDataContentDisposition fileDetail /* file meta-data */,
							@Context HttpServletResponse httpServletResponse,
							@FormDataParam("name") String name) {
		
		try {
			String fileName = name;
			Constants constants = Constants.getInstance;
			int bytesWritten = fileUtils.writeToFile(uploadedInputStream, constants.getUPLOAD_FOLDER(), fileName);
			if ( bytesWritten == 0 ) {
				String errorMessage;
				if ( fileName == null || fileName.isEmpty() ) {
					errorMessage = "No file was received";	
				} else {
					errorMessage = "An empty file was received";
				}				
				httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, errorMessage );
			}
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		
		} catch (IOException e) {
			e.printStackTrace();
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, "An error occured while uploading" );
		}

	}
	
	@GET
	@Path("/image/{name}")
	@Produces(MediaType.MULTIPART_FORM_DATA)	
	public File image ( @PathParam("name") String name)  {		
		return fileUtils.getImage(name);
	}

	
	
	@Path("/removeCoupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void removeCoupon (Coupon coupon, @Context HttpServletResponse httpServletResponse)  {
		try {
			cheeckCoupon(coupon);
			companyFacade.removeCoupon(coupon);
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		} catch (CouponServerException | CouponException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
	}
	@Path("/updateCoupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public void updateCoupon (Coupon coupon, @Context HttpServletResponse httpServletResponse)  {
		try {
			cheeckCoupon(coupon);
			companyFacade.updateCoupon(coupon);
			businessDelegate.storeIncome(new Income(companyFacade.getcompany().getCompName(), new Date(), 10, Description.COMPANY_UPDATE_COUPON));
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED );
		} catch (CouponServerException | CouponException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
	}
	@Path("/getcompany")
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Company getcompany() {
		return companyFacade.getcompany();
		
	}
	@Path("/getAllCoupons")
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public List<Coupon> getAllCoupons(@Context HttpServletResponse httpServletResponse) {
		try {
			return companyFacade.getAllCoupons();
		} catch (CouponServerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
		return null;
		
	}

	@Path("/getCouponsByType")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@POST
	public List<Coupon> getCouponsByType(CouponType couponType, @Context HttpServletResponse httpServletResponse) {
		try {
			return companyFacade.getCouponsByType(couponType);
		} catch (CouponServerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
		return null;
		
	}
	@Path("/getCouponsByMaxPrice/{maxPrice}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<Coupon> getCouponsByMaxPrice(@PathParam("maxPrice") double maxPrice, @Context HttpServletResponse httpServletResponse) {
		try {
			return companyFacade.getCouponsByMaxPrice(maxPrice);
		} catch (CouponServerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
		return null;
		
	}
	@Path("/getCouponsAfterYourDate/{minDate}")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public List<Coupon> getCouponsAfterYourDate(@PathParam("minDate") long longMinDate, @Context HttpServletResponse httpServletResponse) {
		
		Date minDate = new Date(longMinDate);
		
		try {
			return companyFacade.getCouponsAfterYourDate(minDate);
		} catch (CouponException | CouponServerException e) {
			httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, e.getMessage());
		}
		return null;
		
	}
	@Path("/viewIncomeByCompany")
	@Produces(MediaType.APPLICATION_JSON)
	@POST	
	public Collection<Income> viewIncomeByCompany(){
		return businessDelegate.viewIncomeByCompany(companyFacade.getcompany().getCompName());		
	}
	
	}
