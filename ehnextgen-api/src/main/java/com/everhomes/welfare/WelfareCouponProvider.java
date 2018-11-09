// @formatter:off
package com.everhomes.welfare;

import java.util.List;

public interface WelfareCouponProvider {

	void createWelfareCoupon(WelfareCoupon welfareCoupon);

	void updateWelfareCoupon(WelfareCoupon welfareCoupon);

	WelfareCoupon findWelfareCouponById(Long id);

	List<WelfareCoupon> listWelfareCoupon();

	List<WelfareCoupon> listWelfareCoupon(Long welfareId);

	void deleteWelfareCoupons(Long welfareId);

}