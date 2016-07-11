package com.everhomes.user;

import org.springframework.web.multipart.MultipartFile;


public interface UserExcelImportService {

	public void importUserInfo(MultipartFile mFile, Long userId);
}
