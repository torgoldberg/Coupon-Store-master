package filters;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.ClientType;
import clients.CouponClientFacade;
import main.CouponSystem;
import proxy.BusinessDelegate;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouponSystem couponSystem;
	private String status;
	private BusinessDelegate businessDelegate;



	@Override
	public void init() throws ServletException {
		super.init();
		couponSystem = CouponSystem.getCouponSystem();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		session.invalidate();

		String name = request.getParameter(FinalAtributte.NAME);
		String password = request.getParameter(FinalAtributte.PASSWORD);
		String clientType = request.getParameter(FinalAtributte.CLIENT_TYPE);

		try {
			CouponClientFacade couponClientFacade = couponSystem.login(name, password, clientType);
			status = "Link successful";
			session = request.getSession();
			session.setAttribute(FinalAtributte.COUPON_CLIENT_FACADE, couponClientFacade);
			session.setAttribute(FinalAtributte.CLIENT_TYPE, clientType);
			this.businessDelegate = new BusinessDelegate();
			session.setAttribute(FinalAtributte.BUSINESS_DELEGATE, businessDelegate);
			System.out.println("you login!!!");
		} catch (CouponException | CouponServerException e) {
			status = e.getMessage();
			System.out.println(status);
			response.addHeader("status", status);
			return;
		}
		response.addHeader("status", status);
				
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
