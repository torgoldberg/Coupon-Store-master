package clients;

import Exceptions.CouponException;
import Exceptions.CouponServerException;

public abstract class CouponClientFacade {
	
	public static CouponClientFacade login (String name, String password, ClientType clientType) throws CouponServerException, CouponException {
		switch (clientType) {
		case CUSTOMER:
			return CustomerFacade.login(name, password);
		case COMPANY:
			return CompanyFacade.login(name, password);
		case ADMINASTOR:
			return AdminastorFacde.login(name, password);

		default:
			return null;
		}
	}

}
