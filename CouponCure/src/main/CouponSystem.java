package main;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.ClientType;
import clients.CouponClientFacade;
import dao.db.ConnectionPool;

/**
 * single instance of the system
 */

 
public class CouponSystem {


	private static CouponSystem couponSystem = new CouponSystem();
	private ConnectionPool connectionPool;
	private DailyCouponExpirationTask couponExpirationTask;
	private Thread removeOldCoupons;


	public static CouponSystem getCouponSystem() {
		return couponSystem;
	}

	private CouponSystem() {
		couponExpirationTask = new DailyCouponExpirationTask();
		removeOldCoupons = new Thread(couponExpirationTask);
		removeOldCoupons.setDaemon(true);
		removeOldCoupons.start();
	}

	/**
	 * LOG IN AS ADMINISTOR COMPANY OR CUSTOMER CHECKS WITH DATA BASE IF USER NAME AND PASSWORD ARE CORRECT RETURNS FACADES ACCORDING TO CLIENT TYPE 
	 * @param name
	 * @param password
	 * @param clientType
	 * @return clientFacade
	 * @throws CouponException
	 * @throws CouponServerException
	 */
	public CouponClientFacade login(String name, String password, String clientType)
			throws CouponException, CouponServerException {
		ClientType newClientType = null;
		try {
			newClientType = ClientType.valueOf(clientType.toUpperCase());
		} catch (IllegalArgumentException a) {
			throw new CouponException("your clientType is eror");
		}
		return login(name, password, newClientType);
	}

	/**
	 * LOG IN AS ADMINISTOR COMPANY OR CUSTOMER CHECKS WITH DATA BASE IF USER NAME AND PASSWORD ARE CORRECT RETURNS FACADES ACCORDING TO CLIENT TYPE 
	 * @param name
	 * @param password
	 * @param clientType (ENUM)
	 * @return clientFacade
	 * @throws CouponServerException
	 * @throws CouponException
	 */
	public CouponClientFacade login(String name, String password, ClientType clientType)
			throws CouponServerException, CouponException {
		CouponClientFacade clientFacade = CouponClientFacade.login(name, password, clientType);
		return clientFacade;
	}

	/**
	 *  FORCES TO SHUTDOWN A DAILY COUPON EXPIRATION TASK THREAD CLOSE A CONNECTION POOL BY CLOSING ALL THE CONNETIONS THROWS COUPON SERVER EXCEPTION
	 * @throws CouponServerException
	 */
	public void shutdown() throws CouponServerException {

		connectionPool = ConnectionPool.getConnectionPool();
		connectionPool.closeAllConnections();
	}
}
