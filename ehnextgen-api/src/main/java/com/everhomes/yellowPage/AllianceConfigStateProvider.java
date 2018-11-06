package com.everhomes.yellowPage;

import java.util.List;

public interface AllianceConfigStateProvider {

	void createAllianceConfigState(AllianceConfigState state);

	AllianceConfigState findConfigState(Long type, Long projectId);

	void updateAllianceConfigState(AllianceConfigState state);
}
