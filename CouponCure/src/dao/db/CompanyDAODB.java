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
import clients.javabeans.Company;
import clients.javabeans.Coupon;
import dao.CompanyDAO;

/**
 * AN object designed to contact a DB to perform operations for the companies
 */
public class CompanyDAODB implements CompanyDAO {
	
	/**
	 * ATTRIBUTE
	 */
	private ConnectionPool connectionPool;
	
	/**
	 * The constructor take the instance of connection pool
	 * @throws CouponServerException
	 */
	public CompanyDAODB() throws CouponServerException {
		super();
		this.connectionPool = ConnectionPool.getConnectionPool();
	}
	
	/**
	 * ADDS COMPANY TO THE DATA BASE
	 * INSERT COMPANY DETAILS INTO THE SPECIFIED DATA BASE TABLE EXCEPT FOR THE ID HWO GENERATED AUTOMATICALLY BY THE DATA BASE
	 * @throws CouponServerException
	 * @throws CouponException 
	 */
	@Override
	public void createCompany(Company company) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "INSERT INTO Company (COMP_NAME , PASSWORD , EMAIL) VALUES  (?,?,?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1,company.getCompName());
			statement.setString(2,company.getPassword());
			statement.setString(3,company.getEmail   ());
			statement.execute();
			
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			company.setIdCompany(resultSet.getLong(1));

		} catch (SQLException| NullPointerException e) {
			throw new CouponServerException("eror in create Company", e);
		} finally {
			if (connection != null) connectionPool.returnConnection(connection);	
		}
	}

	/**
	*REMOVE ALL SPECIFIED COMPANY FROM THE DATA BASE
	*REMOVE COMPANY FROM THE DATA BASE TABLE 
	 * @throws CouponServerException
	 * @throws CouponException 
	 */
	@Override
	public void removeCompany(Company company) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "DELETE FROM Company WHERE ID = ?;" ;
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, company.getIdCompany());
			statement.execute();

		} catch (SQLException e) {
			throw new CouponServerException("eror in remove Company", e);

		} finally {
			if (connection != null) connectionPool.returnConnection(connection);	
		}
	}

	/**
	 *UPDATE COMPANY IN THE DATA BASE
	 * @throws CouponServerException
	 * @throws CouponException 
	 */
	@Override
	public void updateCompany(Company company) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "UPDATE Company set COMP_NAME = ?,PASSWORD = ?,EMAIL=?  WHERE ID = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1,company.getCompName());
			statement.setString(2,company.getPassword());
			statement.setString(3,company.getEmail());
			statement.setLong(4,company.getIdCompany());
			statement.execute();

		} catch(SQLException e) {
			throw new CouponServerException("eror in update Company", e);
		} finally {
			if (connection != null) connectionPool.returnConnection(connection);	
		}
	}

	/**
	 * Give company by name from DB , need the name of company
	 * @throws CouponServerException
	 * @throws CouponException 
	 */
	@Override
	public Company 	getCompanyByName(String name) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "SELECT (*) FROM Company WHERE COMP_NAME = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1,name);
			
			ResultSet resultSet = statement.executeQuery();	
			resultSet.next();
			Company company =  takeCompanyFromDB(resultSet);
			// return the connection for use in new connection to copy from another	table	
			if (connection != null) connectionPool.returnConnection(connection);
			company.setCoupons(getCouponsByCompany((company.getIdCompany())));
			return company;
		} catch (SQLException e) {
			throw new CouponServerException("eror in get Company", e);
		} finally {
			if (connection != null) connectionPool.returnConnection(connection);	
		}
	}

	/**
	 * EXTRACTS THE SPECIFIED COMPANY OBJECT FROM THE DATA BASE WITH THE GIVEN ID
	 * @throws CouponServerException, CouponException
	 */
	@Override
	public Company getCompany(long id) throws CouponServerException, CouponException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "SELECT (*) FROM Company WHERE ID = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1,id);
			
			ResultSet resultSet = statement.executeQuery();	
			if (!(resultSet.next())) throw new CouponException("this id company not exist in the program");
			Company company =  takeCompanyFromDB(resultSet);
			if (connection != null) connectionPool.returnConnection(connection);
			company.setCoupons(getCouponsByCompany(company.getIdCompany()));
			return company;
		} catch (SQLException e) {
			throw new CouponServerException("eror in get Company", e);
		} finally {
			if (connection != null) connectionPool.returnConnection(connection);	
		}
	}

	private Company takeCompanyFromDB(ResultSet resultSet) throws SQLException {
		return new Company(resultSet.getLong("ID"), resultSet.getString("COMP_NAME"), resultSet.getString("PASSWORD"), resultSet.getString("EMAIL"));
	}

	/**
	 * RETURNS A COUPON LIST OF A SPECIFIED COMPANY FROM THE DATA BASE BY ID
	 * @throws CouponServerException
	 * @throws CouponException 
	 */
	@Override
	public List<Coupon> getCouponsByCompany(long compId) throws CouponServerException {
		Connection connection = null;
		List<Coupon> coupons = new ArrayList<Coupon>();
		try {
			connection = connectionPool.getConnection();
			String query = "SELECT * FROM Coupon WHERE COMP_ID = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, compId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				coupons.add(CouponDAODB.newCoupon(resultSet));
			}
		} catch (SQLException e) {
			throw new CouponServerException("eror in getCouponByType", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
		return coupons;
	}


	/**
	 *RETURNS A LIST OF ALL THE COMPANIES FROM THE DATA BASE
	 * @throws CouponServerException
	 * @throws CouponException 
	 */
	@Override
	public List <Company> getAllCompany() throws CouponServerException {
		Connection connection = null;
		List <Company> companies = new ArrayList<Company>();
		try {
			connection = connectionPool.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT (*) FROM Company;");		
			while (resultSet.next()) {
				companies.add(takeCompanyFromDB(resultSet));
			}
			
			// return the connection for use in new connection to copy from another	table	
			if (connection != null) connectionPool.returnConnection(connection);
			for (Company company : companies) {
				company.setCoupons(getCouponsByCompany((company.getIdCompany())));
			}
		} catch (SQLException e) {
			throw new CouponServerException("eror in get All Companies", e);
		} finally {
			if (connection != null) connectionPool.returnConnection(connection);
		}
		return companies;
	}


	/**
	 * RETURNS TRUE OF FALSE IF A COMPANY LOGED IN
	 * Receives the company name and password and checks whether it is the correct password of the company
	 * @throws CouponServerException
	 */
	@Override
	public boolean login(String compName, String password) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery( "SELECT COMP_NAME , PASSWORD FROM Company");		
			
			while (resultSet.next()) {
				if (resultSet.getString("PASSWORD").equals(password) && resultSet.getString("COMP_NAME").equalsIgnoreCase(compName)) {
					return true;
				}
			}

		} catch (SQLException| NullPointerException e) {
			throw new CouponServerException("eror in create Company", e);
		} finally {
			if (connection != null) connectionPool.returnConnection(connection);	
		}		
		return false;
	}
}
