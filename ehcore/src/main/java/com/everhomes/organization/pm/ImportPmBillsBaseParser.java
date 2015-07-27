package com.everhomes.organization.pm;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImportPmBillsBaseParser {
	
	List<CommunityPmBill> parse(MultipartFile[] files);
	

}
