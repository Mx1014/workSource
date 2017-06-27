// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryGroupEntityProvider {

	void createSalaryGroupEntity(SalaryGroupEntity salaryGroupEntity);

	void updateSalaryGroupEntity(SalaryGroupEntity salaryGroupEntity);

	SalaryGroupEntity findSalaryGroupEntityById(Long id);

	List<SalaryGroupEntity> listSalaryGroupEntityByGroupId(Long salaryGroupId);

	List<SalaryGroupEntity> listSalaryGroupEntity();

<<<<<<< HEAD
	void updateSalaryGroupEntityVisible(Long id, Byte visibleFlag);
=======
	void deleteSalaryGroupEntity(SalaryGroupEntity entity);
>>>>>>> 968e91b2538ba9e8b300ccaea880418a8b14ad98
}