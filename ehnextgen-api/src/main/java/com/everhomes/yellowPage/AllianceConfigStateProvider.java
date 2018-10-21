package com.everhomes.yellowPage;

import java.util.List;

public interface AllianceConfigStateProvider {

	AllianceConfigState createAllianceConfigState(AllianceConfigState state);

	AllianceConfigState findConfigState(Long type, Long projectId);

	List<AllianceConfigState> getConfigStatesByType(Long type);
}
