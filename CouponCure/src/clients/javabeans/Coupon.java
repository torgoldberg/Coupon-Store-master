package clients.javabeans;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Coupon {
	
	/**
	 * ATTRIBUTES
	 */
	private long idCoupon;
	private long idCompany;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType couponType;
	private String message;
	private double price;
	private String image;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * CONTRUCTOR
	 */
	public Coupon() {
		super();
	}
	

	public Coupon(long idCoupon, long idCompany, String title, Date startDate, Date endDate, int amount,
			CouponType couponType, String message, double price, String image) {
		super();
		this.idCoupon = idCoupon;
		this.idCompany = idCompany;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.couponType = couponType;
		this.message = message;
		this.price = price;
		this.image = image;

	}


	/**
	 * GETTERS AND SETTERS METHODS
	 */
	public long getIdCoupon() {
		return idCoupon;
	}

	public void setIdCoupon(long idCoupon) {
		this.idCoupon = idCoupon;
	}

	public long getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(long idCompany) {
		this.idCompany = idCompany;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CouponType getCouponType() {
		return couponType;
	}

	public void setCouponType(CouponType couponType) {
		this.couponType = couponType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * TO STRING METHOD
	 */
	@Override
	public String toString() {
		return "Coupon [idCoupon=" + idCoupon + ", idCompany=" + idCompany + ", title=" + title + ", startDate="
				+ startDate + ", endDate=" + endDate + ", amount=" + amount + ", couponType=" + couponType
				+ ", message=" + message + ", price=" + price + ", image=" + image + "]";
	}

	


}
