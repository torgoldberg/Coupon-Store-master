package clients.javabeans;

import java.util.Comparator;

public class SortByType implements Comparator<Coupon> {

	/**
	 * SORT COUPON BY TYPE
	 */
	@Override
	public int compare(Coupon coupon, Coupon coupon2) {
		
	if (coupon.getCouponType().toString().equals(coupon2.getCouponType().toString())){
		return coupon.getTitle().compareToIgnoreCase(coupon2.getTitle());	
	}
		
		return 	coupon.getCouponType().toString().compareTo(coupon2.getCouponType().toString());

	}

}
