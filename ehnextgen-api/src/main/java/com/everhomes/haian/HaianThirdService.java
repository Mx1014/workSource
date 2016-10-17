package com.everhomes.haian;

import com.everhomes.rest.haian.EncryptForInsurobotCommand;
import com.everhomes.rest.haian.EncryptForInsurobotDTO;

public interface HaianThirdService {
	EncryptForInsurobotDTO encryptForInsurobot(EncryptForInsurobotCommand cmd);
}
