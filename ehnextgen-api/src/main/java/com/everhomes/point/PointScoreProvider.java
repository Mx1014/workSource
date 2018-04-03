// @formatter:off
package com.everhomes.point;

import java.util.List;

public interface PointScoreProvider {

	void createPointScore(PointScore pointScore);

	void updatePointScore(PointScore pointScore);

	PointScore findById(Long id);

    <T> T findUserPointScore(Integer namespaceId, Long systemId, Long uid, Class<T> clazz);

    List<PointScore> listPointScoreBySystem(Long systemId);
}