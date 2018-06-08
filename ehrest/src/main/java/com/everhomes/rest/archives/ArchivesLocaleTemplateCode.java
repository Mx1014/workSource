package com.everhomes.rest.archives;

public interface ArchivesLocaleTemplateCode {
	String SCOPE = "archives.notification";

	int ARCHIVES_PROBATION_CASE = 1;		//	试用或实习员工

	int ARCHIVES_ON_THE_JOB_CASE = 2;		//	在职员工

	int ARCHIVES_DISMISS_CASE = 3;			//	离职员工

	int ARCHIVES_REMIND_BEGINNING = 5;		//	人事提醒开头

	int ARCHIVES_REMIND_EMPLOYMENT = 6;	//	人事转正提醒

	int ARCHIVES_REMIND_CONTRACT = 7;		//	人事合同到期提醒

	int ARCHIVES_REMIND_ID	= 8;			//	人事身份证到期提醒

	int ARCHIVES_REMIND_ANNIVERSARY = 9;	//	人事周年提醒

	int ARCHIVES_REMIND_BIRTH = 10;		//	人事生日提醒


	int OPERATION_PROBATION_PERIOD = 101;     //  操作提示试用期
	int OPERATION_ORG_CHANGE = 102;    //  操作提示部门、岗位、职级变动
}
