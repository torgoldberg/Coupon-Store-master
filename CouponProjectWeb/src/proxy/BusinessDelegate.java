package proxy;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import Exceptions.CouponServerException;
import services.IncomeService;
import db.Income;

public class BusinessDelegate {

	private IncomeService stub;

	public BusinessDelegate() throws CouponServerException {
		InitialContext ctx = getInitialContext();
		Object ref=null;
		try {
			ref = ctx.lookup("IncomeService/remote");
		} catch (NamingException e) {
			throw new CouponServerException("eror in look up", e);
		}
		stub = (IncomeService)PortableRemoteObject.narrow(ref, IncomeService.class);
	}
	
	
	public void storeIncome(Income income) {
		stub.storeIncome(income);	
	};
	
	public Collection<Income> viewAllIncome(){
		return stub.viewAllIncome();
		
	}
	public Collection<Income> viewIncomeByCustomer(String customerName){
		return stub.viewIncomeByCustomer(customerName);
		
	}
	public Collection<Income> viewIncomeByCompany(String companyName){
		return stub.viewIncomeByCompany(companyName);
		
	}

	private InitialContext getInitialContext() throws CouponServerException {
   	 Properties env = new Properties();

		env.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.NamingContextFactory");
		env.put(Context.URL_PKG_PREFIXES,"org.jboss.naming:org.jnp.interfaces");
		env.put(Context.PROVIDER_URL, "localhost:1099");
	
		try {
			return new InitialContext(env);
		} catch (NamingException e) {
			throw new CouponServerException("eror in get Initial Context", e);
		}
	}
	

   
}
