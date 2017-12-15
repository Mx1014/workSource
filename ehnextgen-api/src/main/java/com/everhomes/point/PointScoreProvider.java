// @formatter:off
package com.everhomes.point;

public interface PointScoreProvider {

	void createPointScore(PointScore pointScore);

	void updatePointScore(PointScore pointScore);

	PointScore findById(Long id);

    <T> T findUserPointScore(Integer namespaceId, Long systemId, Long uid, Class<T> clazz);
}