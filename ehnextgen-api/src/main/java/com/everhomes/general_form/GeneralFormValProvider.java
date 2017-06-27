package com.everhomes.general_form;



import java.util.List;

public interface GeneralFormValProvider {

	Long createGeneralFormVal(GeneralFormVal obj);

	void updateGeneralFormVal(GeneralFormVal obj);

	void deleteGeneralFormVal(GeneralFormVal obj);

	GeneralFormVal getGeneralFormValById(Long id);

	List<GeneralFormVal> queryGeneralFormVals(String sourceType, Long sourceId);

	void deleteGeneralFormVals(String sourceType, Long sourceId);

}
