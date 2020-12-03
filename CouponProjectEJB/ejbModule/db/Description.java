package db;

public enum Description {
CUSTOMER_PURCHASE("customer buy coupon"),
COMPANY_NEW_COUPON("company created coupon"),
COMPANY_UPDATE_COUPON("company update coupon");
	
	private String description;

	private Description(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return description;
	}
	
}
