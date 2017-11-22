// @formatter:off
package com.everhomes.point;

public interface PointModuleProvider {

	void createPointModule(PointModule pointModule);

	void updatePointModule(PointModule pointModule);

	PointModule findById(Long id);

}