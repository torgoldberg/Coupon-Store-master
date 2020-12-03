package clients.javabeans;

import java.util.Comparator;

public class SortByPrice  implements Comparator<Coupon> {
	
	/**
	 * SORT COUPON BY PRICE
	 */
	@Override
	public int compare(Coupon coupon, Coupon coupon2) {
	
		return (int) (coupon.getPrice()-coupon2.getPrice());

	}

}