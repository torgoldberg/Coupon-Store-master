package dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.javabeans.Coupon;
import clients.javabeans.Customer;
import dao.CustomerDAO;

/**
 * AN object designed to contact a DB (Access) to perform operations for the
 * customers and administers
 */
public class CustomerDAODB implements CustomerDAO {

	
	private ConnectionPool connectionPool;

	
	public CustomerDAODB() throws CouponServerException {
		super();
		this.connectionPool = ConnectionPool.getConnectionPool();
	}

	/**
	 * ADDS CUSTOMER TO THE DATA BASE INSERT CUSTOMER DETAILS INTO THE DATA BASE
	 * TABLES EXCEPT ID WICH GENERATED AUTOMARICLLY BY THE DATA BASE
	 * @throws CouponServerException
	 */
	@Override
	public void createCustomer(Customer customer) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "INSERT INTO Customer (CUST_NAME,PASSWORD) VALUES  (?,?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, customer.getCustName());
			statement.setString(2, customer.getPassword());
			statement.execute();
			
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			customer.setIdCustomer(resultSet.getLong(1));
			
		} catch (SQLException | NullPointerException e) {
			throw new CouponServerException("eror in create Customer", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	/**
	 * remove customer from DB (Access) , and the DB automatically remove all past purchase
	 * @throws CouponServerException
	 */
	@Override
	public void removeCustomer(Customer customer) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "DELETE FROM Customer WHERE ID = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, customer.getIdCustomer());
			statement.execute();

		} catch (SQLException e) {
			throw new CouponServerException("eror in remove Customer", e);

		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	/**
	 * UPDATES INFORMATION ABOUT THE SPECIFIED CUSTOMER IN THE DATA BASE 
	 * @throws CouponServerException
	 */
	@Override
	public void updateCustomer(Customer customer) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "UPDATE Customer set PASSWORD = ? WHERE ID = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, customer.getPassword());
			statement.setLong(2, customer.getIdCustomer());
			statement.execute();

		} catch (SQLException e) {
			throw new CouponServerException("eror in update Customer", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	/**
	 * EXTRACTS THE SPECIFIED CUSTOMER FROM THE DATA BASE WITH THE GIVEN ID 
	 * @throws CouponServerException
	 */
	@Override
	public Customer getCustomer(long id) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "SELECT (*) FROM Customer WHERE ID = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, id);

			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			Customer customer = takeCustomerFromDB(resultSet);
			// return the connection for use in new connection to copy from another table
			if (connection != null)
				connectionPool.returnConnection(connection);
			customer.setCoupons(getCoupons(id));
			return customer;

		} catch (SQLException e) {
			throw new CouponServerException("the id isn't exist", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	/**
	 * Give customer by name from DB , need the customer name 
	 * @throws CouponServerException
	 */
	@Override
	public Customer getCustomerByName(String name) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "SELECT (*) FROM Customer WHERE CUST_NAME = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, name);

			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			Customer customer = takeCustomerFromDB(resultSet);
			// return the connection for use in new connection to copy from another table
			if (connection != null)
				connectionPool.returnConnection(connection);
			customer.setCoupons(getCoupons(customer.getIdCustomer()));
			return customer;

		} catch (SQLException e) {
			throw new CouponServerException("eror in get customer by name", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	
	private Customer takeCustomerFromDB(ResultSet resultSet) throws SQLException, CouponServerException {
		return new Customer(resultSet.getLong("ID"), resultSet.getString("CUST_NAME"), resultSet.getString("PASSWORD"));
	}

	/**
	 * RETURNS A LIST OF ALL CUSTOMERS FROM THE DATA BASE 
	 * @throws CouponServerException
	 * @throws CouponException 
	 */
	@Override
	public List<Customer> getAllCustomer() throws CouponServerException, CouponException {

		Connection connection = null;
		List<Customer> customers = new ArrayList<Customer>();
		try {
			connection = connectionPool.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT (*) FROM Customer;");
			while (resultSet.next()) {
				customers.add(takeCustomerFromDB(resultSet));
			}
			// return the connection for use in new connection to copy from another table
			if (connection != null) {
				connectionPool.returnConnection(connection);
			}
			//if(customers==null) {throw new CouponException("There are no customers in the system");}
			for (Customer customer : customers) {
					customer.setCoupons(getCoupons(customer.getIdCustomer()));
					}
				
			return customers;
		} catch (SQLException e) {
			throw new CouponServerException("eror in get All Customers", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	/**
	 * RETURNS A LIST OF COUPONS OF A SPECIFIED CUSTOMER AND SPECIFIED COUPON 
	 * @throws CouponServerException
	 */
	@Override
	public List<Long> getIdCoupons(long idCustomer) throws CouponServerException {
		Connection connection = null;
		List<Long> couponId = new ArrayList<Long>();
		try {
			connection = connectionPool.getConnection();
			String query = "SELECT COUPON_ID FROM Customer_Coupon WHERE CUST_ID = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, idCustomer);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				couponId.add(resultSet.getLong("COUPON_ID"));
			}
		} catch (SQLException e) {
			throw new CouponServerException("eror in get Id Coupons by Customer", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
		return couponId;
	}

	/**
	 * RETURNS A LIST OF COUPONS OF A SPECIFIRD CUSTOMER BY ITS ID 
	 * @throws CouponServerException
	 */
	@Override
	public List<Coupon> getCoupons(long idCustomer) throws CouponServerException {
		List<Long> couponsId = getIdCoupons(idCustomer);
		CouponDAODB couponDAODB = new CouponDAODB();
		List<Coupon> coupons = new ArrayList<Coupon>();
		for (Long couponId : couponsId) {
			coupons.add(couponDAODB.getCoupon(couponId));
		}
		return coupons;
	}

	/**
	 * RETURNS TRUE OF FALSE IF A CUSTOMER LOGED IN Receives the customer name and password and checks whether it is the correct password of the customer (and whether it exists in the customers table) 
	 * @throws CouponServerException
	 */
	@Override
	public boolean login(String custName, String password) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT CUST_NAME ,PASSWORD FROM Customer");

			while (resultSet.next()) {
				if (resultSet.getString("PASSWORD").equals(password)
						&& resultSet.getString("CUST_NAME").equalsIgnoreCase(custName)) {
					return true;
				}
			}
		} catch (SQLException | NullPointerException e) {
			throw new CouponServerException("eror in login", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
		return false;
	}

	/**
	 * purchase coupon to DB. Attention! There are missing important actions here that should be made when purchasing a coupon (the amount of coupons and checking the validity of the sale). It is recommended to buy only through the instance of CustomerFacade in method 'purchaseCoupon(Coupon)'
	
	 * @throws CouponServerException,  CouponException
	 */
	@Override
	public void purchaseCoupon(long idCoupon, long idCustomer) throws CouponServerException, CouponException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "INSERT INTO Customer_Coupon (COUPON_ID, CUST_ID) VALUES  (?,?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, idCoupon);
			statement.setLong(2, idCustomer);
			statement.execute();
		} catch (SQLException e) {
			throw new CouponServerException("eror in get purchase Coupon", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

}
