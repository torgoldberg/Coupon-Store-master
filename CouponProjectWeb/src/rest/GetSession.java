package rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import clients.ClientType;
import clients.CompanyFacade;
import clients.CustomerFacade;
import filters.FinalAtributte;

@Path("/getSession")
public class GetSession {

	@Path("/getSession")
	@POST
	public Object getSession( @Context HttpServletRequest request, @Context HttpServletResponse httpServletResponse) {
		HttpSession httpSession = request.getSession(false);
		if (httpSession==null) {
			httpServletResponse.addHeader(FinalAtributte.FACADE_TYPE, null);	
		}
		
		if (httpSession!=null) {
			
			String clientFacade = (String) httpSession.getAttribute(FinalAtributte.CLIENT_TYPE);
			ClientType clientFacadeEnum = ClientType.valueOf(clientFacade);
			switch(clientFacadeEnum) {
			case ADMINASTOR: 
				System.out.println("adminService");
				httpServletResponse.addHeader(FinalAtributte.FACADE_TYPE, "adminService");
				return null;
			
			case COMPANY:  
				httpServletResponse.addHeader(FinalAtributte.FACADE_TYPE, "companyService");	
				return ((CompanyFacade) httpSession.getAttribute(FinalAtributte.COUPON_CLIENT_FACADE)).getcompany();

			case CUSTOMER:
				httpServletResponse.addHeader(FinalAtributte.FACADE_TYPE, "customerService");	
				return ((CustomerFacade) httpSession.getAttribute(FinalAtributte.COUPON_CLIENT_FACADE)).getCustomer();
			}
			
		}
		return null;
		
		


		
	}
	@Path("/invalidate")
	@POST
	public void invalidate( @Context HttpServletRequest request, @Context HttpServletResponse httpServletResponse) {
		HttpSession httpSession = request.getSession();
		httpSession.invalidate();
		httpServletResponse.addHeader(FinalAtributte.STATUS_TO_CLIENT, FinalAtributte.SUCCEED);
	}

}
