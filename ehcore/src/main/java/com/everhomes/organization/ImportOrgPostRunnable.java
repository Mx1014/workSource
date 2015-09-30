package com.everhomes.organization;

public class ImportOrgPostRunnable implements Runnable{
	
	private OrganizationService organizationService;
	private String filePath;
	private Long userId;
	
	ImportOrgPostRunnable(OrganizationService organizationService,String filePath,Long userId){
		this.organizationService = organizationService;
		this.filePath = filePath;
		this.userId = userId;
	}

	@Override
	public void run() {
		organizationService.executeImportOrgPost(filePath, userId);
	}

}
