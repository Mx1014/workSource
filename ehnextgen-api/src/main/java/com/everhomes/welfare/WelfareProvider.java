// @formatter:off
package com.everhomes.welfare;

import java.util.List;

public interface WelfareProvider {

	void createWelfare(Welfare welfare);

	void updateWelfare(Welfare welfare);

	Welfare findWelfareById(Long id);

	List<Welfare> listWelfare(Long ownerId);

	void deleteWelfare(Long welfareId);
}