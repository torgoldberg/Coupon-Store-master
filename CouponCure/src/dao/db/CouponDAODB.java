package dao.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Exceptions.CouponException;
import Exceptions.CouponServerException;
import clients.javabeans.Coupon;
import clients.javabeans.CouponType;
import dao.Converator;
import dao.CouponDAO;

/**
 * AN object designed to contact a DB to perform operations that are related to
 * coupons
 */
public class CouponDAODB implements CouponDAO {

	private ConnectionPool connectionPool;

	/**
	 * Take the instance of connection pool
	 * @throws CouponServerException
	 */
	public CouponDAODB() throws CouponServerException {
		super();
		this.connectionPool = ConnectionPool.getConnectionPool();
	}

	/**
	 * ADDS COUPON TO THE DATA BASE INSERT COUPON DETAILS INTO THE SPECIFIED DATA BASE TABLE EXCEPT FOR THE ID GENERATED AUTOMARICLLY 
	 * @throws CouponServerException (IF THE COUPON COULDNT BE CREATED)
	 */
	@Override
	public void createCoupon(Coupon coupon) throws CouponServerException {
		Connection connection = null;
		java.sql.Date startDateSql = (Date) Converator.converetorLongToSQLDate(coupon.getStartDate().getTime());
		java.sql.Date EndDateSql = (Date) Converator.converetorLongToSQLDate(coupon.getEndDate().getTime());
		try {
			connection = connectionPool.getConnection();
			String query = "INSERT INTO Coupon (TITLE,START_DATE,END_DATE,AMOUNT,TYPE,MESSAGE,PRICE,IMAGE,COMP_ID) VALUES  (?,?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, coupon.getTitle());
			statement.setDate(2, startDateSql);
			statement.setDate(3, EndDateSql);
			statement.setInt(4, coupon.getAmount());
			statement.setString(5, coupon.getCouponType().toString());
			statement.setString(6, coupon.getMessage());
			statement.setDouble(7, coupon.getPrice());
			statement.setString(8, coupon.getImage());
			statement.setLong(9, coupon.getIdCompany());
			statement.execute();

			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			coupon.setIdCoupon(resultSet.getLong(1));

		} catch (SQLException | NullPointerException e) {
			throw new CouponServerException("eror in create Coupon", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	/**
	 * REMOVES SPECIFIED COUPON FROM THE DATA BASE and the DB also remove all the past purchase.
	 * @throws CouponServerException
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "DELETE FROM Coupon WHERE ID = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, coupon.getIdCoupon());
			statement.execute();

		} catch (SQLException e) {
			throw new CouponServerException("eror in removeCoupon", e);

		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	/**
	 * UPDATES SPECIFIED COUPON IN THE DATA BASE 
	 * (this method don't synchronized and It is recommended to buy a coupon only through the instance of CustomerFacade.)
	 * @throws CouponServerException
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws CouponServerException {
		Connection connection = null;
		java.sql.Date startDateSql = (Date) Converator.converetorLongToSQLDate(coupon.getStartDate().getTime());
		java.sql.Date EndDateSql = (Date) Converator.converetorLongToSQLDate(coupon.getEndDate().getTime());
		try {
			connection = connectionPool.getConnection();
			String query = "UPDATE Coupon set TITLE = ?,START_DATE = ?,END_DATE=?,AMOUNT = ?,TYPE = ?,MESSAGE = ?,PRICE = ?,IMAGE = ?,COMP_ID = ? WHERE ID = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, coupon.getTitle());
			statement.setDate(2, startDateSql);
			statement.setDate(3, EndDateSql);
			statement.setInt(4, coupon.getAmount());
			statement.setString(5, coupon.getCouponType().toString());
			statement.setString(6, coupon.getMessage());
			statement.setDouble(7, coupon.getPrice());
			statement.setString(8, coupon.getImage());
			statement.setLong(9, coupon.getIdCompany());
			statement.setLong(10, coupon.getIdCoupon());
			statement.execute();

		} catch (SQLException e) {
			throw new CouponServerException("eror in Update Coupon", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	/**
	 * EXTRACTS THE SPECIFIED COUPON FROM THE DATA BASE WITH A GIVEN ID
	 * @throws CouponServerException
	 */
	@Override
	public Coupon getCoupon(Long id) throws CouponServerException {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
			String query = "SELECT (*) FROM Coupon WHERE ID = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setLong(1, id);

			ResultSet resultSet = statement.executeQuery();
			Coupon coupon = null;
			if (resultSet.next()) {
				coupon = newCoupon(resultSet);
			}
			return coupon;

		} catch (SQLException e) {
			throw new CouponServerException("eror in get Coupon", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
	}

	static Coupon newCoupon(ResultSet resultSet) throws SQLException {
		return new Coupon(resultSet.getLong("ID"), resultSet.getLong("COMP_ID"), resultSet.getString("TITLE"),
				Converator.convoretorLongToDate(resultSet.getDate("START_DATE").getTime()),
				Converator.convoretorLongToDate(resultSet.getDate("END_DATE").getTime()), resultSet.getInt("AMOUNT"),
				CouponType.valueOf(resultSet.getString("TYPE")), resultSet.getString("MESSAGE"), resultSet.getDouble("PRICE"),
				resultSet.getString("IMAGE"));
	}

	/**
	 * RETURNS A LIST OF ALL THE COUPONS FROM THE DATA BASE
	 * @throws CouponServerException
	 */
	@Override
	public List<Coupon> getAllCoupons() throws CouponServerException {
		Connection connection = null;
		List<Coupon> coupons = new ArrayList<Coupon>();
		try {
			connection = connectionPool.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Coupon;");
			while (resultSet.next()) {
				coupons.add(newCoupon(resultSet));
			}
		} catch (SQLException e) {
			throw new CouponServerException("eror in get all coupon", e);
		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}
		return coupons;
	}

	/**
	 * RETURNS A LIST OF COUPONS WITH A SPECIFIED TYPE 
   	* @throws CouponServerException
	 */
	@Override
	public List<Coupon> getCouponsByType(CouponType couponType) throws CouponServerException {
		Connection connection = null;
		List<Coupon> coupons = new ArrayList<Coupon>();
		try {
			connection = connectionPool.getConnection();
			String query = "SELECT * FROM Coupon WHERE TYPE = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, couponType.toString());
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				coupons.add(newCoupon(resultSet));
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
	 * check the Title of all coupons to avoid duplication
	 * @throws CouponServerException, CouponException
	 */
	@Override
	public boolean checkTitleOfAllCoupons(Coupon coupon) throws CouponServerException, CouponException {
		for (Coupon couponFromDB : getAllCoupons()) {
			if (couponFromDB.getTitle().equals(coupon.getTitle()))
				throw new CouponException("the title in use");
			 if (couponFromDB.getIdCoupon()==coupon.getIdCoupon()) {
				 throw new CouponException("the id in use");
			 }
		}
		return true;
	}

	/*
	 * public void removeCouponPurchase(long idCoupon) throws
	 * CouponServerException {
	 * Connection connection = null; try { connection =
	 * connectionPool.getConnection(); String query =
	 * "DELETE FROM Customer_Coupon WHERE COUPON_ID = ?;"; PreparedStatement
	 * statement = connection.prepareStatement(query); statement.setLong(1,
	 * idCoupon); statement.execute();
	 * } catch (SQLException e) { throw new
	 * CouponServerException("eror in remove Coupon Purchase", e); } finally { if
	 * (connection != null) connectionPool.returnConnection(connection); } }
	 */

	/**
	 * REMOVE SPECIFIED COUPON FROM THE DATA BASE TABLE ACCORDING TO ITS DATE
   	* @throws CouponServerException
	 */
	@Override
	public void removeCouponByDate(java.sql.Date date) throws CouponServerException {
		Connection connection = null;

		try {
			connection = connectionPool.getConnection();
			String query = "DELETE FROM Coupon WHERE END_DATE < ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setDate(1, date);
			statement.execute();

		} catch (SQLException e) {
			throw new CouponServerException("eror in removeCoupon", e);

		} finally {
			if (connection != null)
				connectionPool.returnConnection(connection);
		}

	}

}
