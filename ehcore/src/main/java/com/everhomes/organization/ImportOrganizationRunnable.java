package com.everhomes.organization;

public class ImportOrganizationRunnable implements Runnable{
	
	private OrganizationService organizationService;
	private String filePath;
	private Long userId;
	
	ImportOrganizationRunnable(OrganizationService organizationService,String filePath,Long userId){
		this.organizationService = organizationService;
		this.filePath = filePath;
		this.userId = userId;
	}

	@Override
	public void run() {
		organizationService.executeImportOrganization(filePath, userId);
	}

}
