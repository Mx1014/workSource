// @formatter:off
package com.everhomes.sequence;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.admin.GetSequenceCommand;
import com.everhomes.rest.admin.GetSequenceDTO;
import com.everhomes.schema.tables.pojos.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.pojos.EhAclinkFirmware;
import com.everhomes.server.schema.tables.pojos.EhAclinkLogs;
import com.everhomes.server.schema.tables.pojos.EhAclinkUndoKey;
import com.everhomes.server.schema.tables.pojos.EhAclinks;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhActivityAttachments;
import com.everhomes.server.schema.tables.pojos.EhActivityCategories;
import com.everhomes.server.schema.tables.pojos.EhActivityGoods;
import com.everhomes.server.schema.tables.pojos.EhActivityRoster;
import com.everhomes.server.schema.tables.pojos.EhActivityVideo;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhAesServerKey;
import com.everhomes.server.schema.tables.pojos.EhAesUserKey;
import com.everhomes.server.schema.tables.pojos.EhAppNamespaceMappings;
import com.everhomes.server.schema.tables.pojos.EhAppVersion;
import com.everhomes.server.schema.tables.pojos.EhApprovalAttachments;
import com.everhomes.server.schema.tables.pojos.EhApprovalCategories;
import com.everhomes.server.schema.tables.pojos.EhApprovalDayActualTime;
import com.everhomes.server.schema.tables.pojos.EhApprovalFlowLevels;
import com.everhomes.server.schema.tables.pojos.EhApprovalFlows;
import com.everhomes.server.schema.tables.pojos.EhApprovalOpRequests;
import com.everhomes.server.schema.tables.pojos.EhApprovalRangeStatistics;
import com.everhomes.server.schema.tables.pojos.EhApprovalRequests;
import com.everhomes.server.schema.tables.pojos.EhApprovalRuleFlowMap;
import com.everhomes.server.schema.tables.pojos.EhApprovalRules;
import com.everhomes.server.schema.tables.pojos.EhApprovalTimeRanges;
import com.everhomes.server.schema.tables.pojos.EhAssetBillTemplateFields;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.server.schema.tables.pojos.EhAuthorizationRelations;
import com.everhomes.server.schema.tables.pojos.EhAuthorizations;
import com.everhomes.server.schema.tables.pojos.EhBroadcasts;
import com.everhomes.server.schema.tables.pojos.EhBuildingAttachments;
import com.everhomes.server.schema.tables.pojos.EhBuildings;
import com.everhomes.server.schema.tables.pojos.EhBusinessAssignedNamespaces;
import com.everhomes.server.schema.tables.pojos.EhBusinessAssignedScopes;
import com.everhomes.server.schema.tables.pojos.EhBusinessCategories;
import com.everhomes.server.schema.tables.pojos.EhBusinessPromotions;
import com.everhomes.server.schema.tables.pojos.EhBusinessVisibleScopes;
import com.everhomes.server.schema.tables.pojos.EhBusinesses;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhCommunityApprove;
import com.everhomes.server.schema.tables.pojos.EhCommunityApproveRequests;
import com.everhomes.server.schema.tables.pojos.EhCommunityBuildingGeos;
import com.everhomes.server.schema.tables.pojos.EhCommunityGeopoints;
import com.everhomes.server.schema.tables.pojos.EhCommunityMapInfos;
import com.everhomes.server.schema.tables.pojos.EhCommunityMapSearchTypes;
import com.everhomes.server.schema.tables.pojos.EhConfAccountCategories;
import com.everhomes.server.schema.tables.pojos.EhConfAccountHistories;
import com.everhomes.server.schema.tables.pojos.EhConfAccounts;
import com.everhomes.server.schema.tables.pojos.EhConfConferences;
import com.everhomes.server.schema.tables.pojos.EhConfEnterprises;
import com.everhomes.server.schema.tables.pojos.EhConfInvoices;
import com.everhomes.server.schema.tables.pojos.EhConfOrderAccountMap;
import com.everhomes.server.schema.tables.pojos.EhConfOrders;
import com.everhomes.server.schema.tables.pojos.EhConfReservations;
import com.everhomes.server.schema.tables.pojos.EhConfSourceAccounts;
import com.everhomes.server.schema.tables.pojos.EhContentServerResources;
import com.everhomes.server.schema.tables.pojos.EhContractAttachments;
import com.everhomes.server.schema.tables.pojos.EhContractBuildingMappings;
import com.everhomes.server.schema.tables.pojos.EhContractChargingItemAddresses;
import com.everhomes.server.schema.tables.pojos.EhContractChargingItems;
import com.everhomes.server.schema.tables.pojos.EhContractParams;
import com.everhomes.server.schema.tables.pojos.EhContracts;
import com.everhomes.server.schema.tables.pojos.EhCooperationRequests;
import com.everhomes.server.schema.tables.pojos.EhCustomerApplyProjects;
import com.everhomes.server.schema.tables.pojos.EhCustomerCommercials;
import com.everhomes.server.schema.tables.pojos.EhCustomerEconomicIndicators;
import com.everhomes.server.schema.tables.pojos.EhCustomerInvestments;
import com.everhomes.server.schema.tables.pojos.EhCustomerPatents;
import com.everhomes.server.schema.tables.pojos.EhCustomerTalents;
import com.everhomes.server.schema.tables.pojos.EhCustomerTrademarks;
import com.everhomes.server.schema.tables.pojos.EhDockingMappings;
import com.everhomes.server.schema.tables.pojos.EhDoorAccess;
import com.everhomes.server.schema.tables.pojos.EhDoorAuth;
import com.everhomes.server.schema.tables.pojos.EhDoorAuthLogs;
import com.everhomes.server.schema.tables.pojos.EhDoorCommand;
import com.everhomes.server.schema.tables.pojos.EhDoorUserPermission;
import com.everhomes.server.schema.tables.pojos.EhEnergyCountStatistics;
import com.everhomes.server.schema.tables.pojos.EhEnergyDateStatistics;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterCategories;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterChangeLogs;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterDefaultSettings;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterFormulas;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterPriceConfig;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterReadingLogs;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterSettingLogs;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeters;
import com.everhomes.server.schema.tables.pojos.EhEnergyMonthStatistics;
import com.everhomes.server.schema.tables.pojos.EhEnergyYoyStatistics;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseAddresses;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseAttachments;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseCommunityMap;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactEntries;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactGroupMembers;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContactGroups;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseContacts;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseDetails;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequestBuildings;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequests;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionAccessories;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionAccessoryMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionCategories;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentAttachments;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentParameters;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentStandardMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipments;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionItemResults;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionItems;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandardGroupMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandards;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTaskAttachments;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTaskLogs;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTasks;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTemplateItemMap;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTemplates;
import com.everhomes.server.schema.tables.pojos.EhExpressAddresses;
import com.everhomes.server.schema.tables.pojos.EhExpressCompanies;
import com.everhomes.server.schema.tables.pojos.EhExpressCompanyBusinesses;
import com.everhomes.server.schema.tables.pojos.EhExpressHotlines;
import com.everhomes.server.schema.tables.pojos.EhExpressOrderLogs;
import com.everhomes.server.schema.tables.pojos.EhExpressOrders;
import com.everhomes.server.schema.tables.pojos.EhExpressParamSettings;
import com.everhomes.server.schema.tables.pojos.EhExpressQueryHistories;
import com.everhomes.server.schema.tables.pojos.EhExpressUsers;
import com.everhomes.server.schema.tables.pojos.EhFlowActions;
import com.everhomes.server.schema.tables.pojos.EhFlowAttachments;
import com.everhomes.server.schema.tables.pojos.EhFlowButtons;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.server.schema.tables.pojos.EhFlowEvaluateItems;
import com.everhomes.server.schema.tables.pojos.EhFlowEvaluates;
import com.everhomes.server.schema.tables.pojos.EhFlowEventLogs;
import com.everhomes.server.schema.tables.pojos.EhFlowForms;
import com.everhomes.server.schema.tables.pojos.EhFlowNodes;
import com.everhomes.server.schema.tables.pojos.EhFlowScripts;
import com.everhomes.server.schema.tables.pojos.EhFlowStats;
import com.everhomes.server.schema.tables.pojos.EhFlowSubjects;
import com.everhomes.server.schema.tables.pojos.EhFlowTimeouts;
import com.everhomes.server.schema.tables.pojos.EhFlowUserSelections;
import com.everhomes.server.schema.tables.pojos.EhFlowVariables;
import com.everhomes.server.schema.tables.pojos.EhFlows;
import com.everhomes.server.schema.tables.pojos.EhForumAssignedScopes;
import com.everhomes.server.schema.tables.pojos.EhForumAttachments;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.server.schema.tables.pojos.EhForums;
import com.everhomes.server.schema.tables.pojos.EhGeneralApprovalVals;
import com.everhomes.server.schema.tables.pojos.EhGeneralApprovals;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormVals;
import com.everhomes.server.schema.tables.pojos.EhGeneralForms;
import com.everhomes.server.schema.tables.pojos.EhGroupMemberLogs;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;
import com.everhomes.server.schema.tables.pojos.EhGroupOpRequests;
import com.everhomes.server.schema.tables.pojos.EhGroupSettings;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhHotTags;
import com.everhomes.server.schema.tables.pojos.EhImportFileTasks;
import com.everhomes.server.schema.tables.pojos.EhItemServiceCategries;
import com.everhomes.server.schema.tables.pojos.EhJournalConfigs;
import com.everhomes.server.schema.tables.pojos.EhJournals;
import com.everhomes.server.schema.tables.pojos.EhLaunchAdvertisements;
import com.everhomes.server.schema.tables.pojos.EhLaunchPadItems;
import com.everhomes.server.schema.tables.pojos.EhLaunchPadLayouts;
import com.everhomes.server.schema.tables.pojos.EhLeaseBuildings;
import com.everhomes.server.schema.tables.pojos.EhLeaseConfigs;
import com.everhomes.server.schema.tables.pojos.EhLeaseIssuerAddresses;
import com.everhomes.server.schema.tables.pojos.EhLeaseIssuers;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotionAttachments;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotionCommunities;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotions;
import com.everhomes.server.schema.tables.pojos.EhLocaleTemplates;
import com.everhomes.server.schema.tables.pojos.EhNamespaceDetails;
import com.everhomes.server.schema.tables.pojos.EhNamespaceResources;
import com.everhomes.server.schema.tables.pojos.EhNearbyCommunityMap;
import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.server.schema.tables.pojos.EhNewsComment;
import com.everhomes.server.schema.tables.pojos.EhNewsCommentRule;
import com.everhomes.server.schema.tables.pojos.EhNewsCommunities;
import com.everhomes.server.schema.tables.pojos.EhOauth2ClientTokens;
import com.everhomes.server.schema.tables.pojos.EhOauth2Codes;
import com.everhomes.server.schema.tables.pojos.EhOauth2Servers;
import com.everhomes.server.schema.tables.pojos.EhOauth2Tokens;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleAttachments;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleCategories;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleOrders;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleSpaces;
import com.everhomes.server.schema.tables.pojos.EhOpPromotionActivities;
import com.everhomes.server.schema.tables.pojos.EhOpPromotionAssignedScopes;
import com.everhomes.server.schema.tables.pojos.EhOpPromotionMessages;
import com.everhomes.server.schema.tables.pojos.EhOrganizationAddressMappings;
import com.everhomes.server.schema.tables.pojos.EhOrganizationAddresses;
import com.everhomes.server.schema.tables.pojos.EhOrganizationAttachments;
import com.everhomes.server.schema.tables.pojos.EhOrganizationCommunityRequests;
import com.everhomes.server.schema.tables.pojos.EhOrganizationDetails;
import com.everhomes.server.schema.tables.pojos.EhOrganizationJobPositionMaps;
import com.everhomes.server.schema.tables.pojos.EhOrganizationJobPositions;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberContracts;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberDetails;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberEducations;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberInsurances;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberLogs;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberProfileLogs;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMemberWorkExperiences;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerAddress;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerAttachments;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerBehaviors;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerCarAttachments;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerCars;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerOwnerCar;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerType;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwners;
import com.everhomes.server.schema.tables.pojos.EhOrganizationRoleMap;
import com.everhomes.server.schema.tables.pojos.EhOrganizationTaskTargets;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.pojos.EhOsObjectDownloadLogs;
import com.everhomes.server.schema.tables.pojos.EhOsObjects;
import com.everhomes.server.schema.tables.pojos.EhOwnerDoors;
import com.everhomes.server.schema.tables.pojos.EhParkApplyCard;
import com.everhomes.server.schema.tables.pojos.EhParkCharge;
import com.everhomes.server.schema.tables.pojos.EhParkingAttachments;
import com.everhomes.server.schema.tables.pojos.EhParkingCarSeries;
import com.everhomes.server.schema.tables.pojos.EhParkingCardCategories;
import com.everhomes.server.schema.tables.pojos.EhParkingCardRequests;
import com.everhomes.server.schema.tables.pojos.EhParkingClearanceLogs;
import com.everhomes.server.schema.tables.pojos.EhParkingClearanceOperators;
import com.everhomes.server.schema.tables.pojos.EhParkingFlow;
import com.everhomes.server.schema.tables.pojos.EhParkingLots;
import com.everhomes.server.schema.tables.pojos.EhParkingRechargeOrders;
import com.everhomes.server.schema.tables.pojos.EhParkingRechargeRates;
import com.everhomes.server.schema.tables.pojos.EhParkingStatistics;
import com.everhomes.server.schema.tables.pojos.EhParkingVendors;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardIssuerCommunities;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardIssuers;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardRechargeOrders;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardTransactions;
import com.everhomes.server.schema.tables.pojos.EhPaymentCards;
import com.everhomes.server.schema.tables.pojos.EhPmNotifyConfigurations;
import com.everhomes.server.schema.tables.pojos.EhPmNotifyLogs;
import com.everhomes.server.schema.tables.pojos.EhPmNotifyRecords;
import com.everhomes.server.schema.tables.pojos.EhPmTaskAttachments;
import com.everhomes.server.schema.tables.pojos.EhPmTaskHistoryAddresses;
import com.everhomes.server.schema.tables.pojos.EhPmTaskLogs;
import com.everhomes.server.schema.tables.pojos.EhPmTaskStatistics;
import com.everhomes.server.schema.tables.pojos.EhPmTaskTargetStatistics;
import com.everhomes.server.schema.tables.pojos.EhPmTaskTargets;
import com.everhomes.server.schema.tables.pojos.EhPmTasks;
import com.everhomes.server.schema.tables.pojos.EhPmsyCommunities;
import com.everhomes.server.schema.tables.pojos.EhPmsyOrderItems;
import com.everhomes.server.schema.tables.pojos.EhPmsyOrders;
import com.everhomes.server.schema.tables.pojos.EhPmsyPayers;
import com.everhomes.server.schema.tables.pojos.EhPollItems;
import com.everhomes.server.schema.tables.pojos.EhPollVotes;
import com.everhomes.server.schema.tables.pojos.EhPolls;
import com.everhomes.server.schema.tables.pojos.EhPortalContentScopes;
import com.everhomes.server.schema.tables.pojos.EhPortalItemCategories;
import com.everhomes.server.schema.tables.pojos.EhPortalItemGroups;
import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.server.schema.tables.pojos.EhPortalLaunchPadMappings;
import com.everhomes.server.schema.tables.pojos.EhPortalLayoutTemplates;
import com.everhomes.server.schema.tables.pojos.EhPortalLayouts;
import com.everhomes.server.schema.tables.pojos.EhPortalNavigationBars;
import com.everhomes.server.schema.tables.pojos.EhPortalPublishLogs;
import com.everhomes.server.schema.tables.pojos.EhPreviews;
import com.everhomes.server.schema.tables.pojos.EhPunchDayLogs;
import com.everhomes.server.schema.tables.pojos.EhPunchExceptionApprovals;
import com.everhomes.server.schema.tables.pojos.EhPunchExceptionRequests;
import com.everhomes.server.schema.tables.pojos.EhPunchGeopoints;
import com.everhomes.server.schema.tables.pojos.EhPunchHolidays;
import com.everhomes.server.schema.tables.pojos.EhPunchLocationRules;
import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
import com.everhomes.server.schema.tables.pojos.EhPunchRuleOwnerMap;
import com.everhomes.server.schema.tables.pojos.EhPunchRules;
import com.everhomes.server.schema.tables.pojos.EhPunchSchedulings;
import com.everhomes.server.schema.tables.pojos.EhPunchStatistics;
import com.everhomes.server.schema.tables.pojos.EhPunchTimeRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWifiRules;
import com.everhomes.server.schema.tables.pojos.EhPunchWifis;
import com.everhomes.server.schema.tables.pojos.EhPunchWorkday;
import com.everhomes.server.schema.tables.pojos.EhPunchWorkdayRules;
import com.everhomes.server.schema.tables.pojos.EhPushMessageResults;
import com.everhomes.server.schema.tables.pojos.EhQrcodes;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionCategories;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionEvaluationFactors;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionEvaluations;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionLogs;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleCommunityMap;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleCommunitySpecificationStat;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleGroupMap;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleScoreStat;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSamples;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSpecificationItemResults;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSpecifications;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandardGroupMap;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandardSpecificationMap;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionStandards;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskAttachments;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskRecords;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskTemplates;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTasks;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaireAnswers;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaireOptions;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaireQuestions;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaires;
import com.everhomes.server.schema.tables.pojos.EhRechargeInfo;
import com.everhomes.server.schema.tables.pojos.EhRentalBillAttachments;
import com.everhomes.server.schema.tables.pojos.EhRentalBillPaybillMap;
import com.everhomes.server.schema.tables.pojos.EhRentalBills;
import com.everhomes.server.schema.tables.pojos.EhRentalItemsBills;
import com.everhomes.server.schema.tables.pojos.EhRentalRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteItems;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSites;
import com.everhomes.server.schema.tables.pojos.EhRentalSitesBills;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Cells;
import com.everhomes.server.schema.tables.pojos.EhRentalv2CloseDates;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ConfigAttachments;
import com.everhomes.server.schema.tables.pojos.EhRentalv2DefaultRules;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Items;
import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderAttachments;
import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderPayorderMap;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Orders;
import com.everhomes.server.schema.tables.pojos.EhRentalv2PriceRules;
import com.everhomes.server.schema.tables.pojos.EhRentalv2RefundOrders;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceNumbers;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceOrders;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourcePics;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceRanges;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceTypes;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Resources;
import com.everhomes.server.schema.tables.pojos.EhRentalv2TimeInterval;
import com.everhomes.server.schema.tables.pojos.EhRepeatSettings;
import com.everhomes.server.schema.tables.pojos.EhRequestAttachments;
import com.everhomes.server.schema.tables.pojos.EhRequestTemplates;
import com.everhomes.server.schema.tables.pojos.EhRequestTemplatesNamespaceMapping;
import com.everhomes.server.schema.tables.pojos.EhResourceCategories;
import com.everhomes.server.schema.tables.pojos.EhResourceCategoryAssignments;
import com.everhomes.server.schema.tables.pojos.EhRichTexts;
import com.everhomes.server.schema.tables.pojos.EhRosterOrderSettings;
import com.everhomes.server.schema.tables.pojos.EhScheduleTaskLogs;
import com.everhomes.server.schema.tables.pojos.EhScheduleTasks;
import com.everhomes.server.schema.tables.pojos.EhSearchTypes;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceApartmentRequests;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceAttachments;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCategories;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceGolfRequests;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceGymRequests;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceInvestRequests;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceJumpModule;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceNotifyTargets;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceRequests;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceReservationRequests;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceServerRequests;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceSkipRule;
import com.everhomes.server.schema.tables.pojos.EhServiceAlliances;
import com.everhomes.server.schema.tables.pojos.EhServiceHotlines;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleAssignmentRelations;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleAssignments;
import com.everhomes.server.schema.tables.pojos.EhServiceModulePrivileges;
import com.everhomes.server.schema.tables.pojos.EhServiceModules;
import com.everhomes.server.schema.tables.pojos.EhSettleRequests;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintEmails;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintOrders;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintPrinters;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintRecords;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintSettings;
import com.everhomes.server.schema.tables.pojos.EhSmsLogs;
import com.everhomes.server.schema.tables.pojos.EhStatActiveUsers;
import com.everhomes.server.schema.tables.pojos.EhStatEventAppAttachmentLogs;
import com.everhomes.server.schema.tables.pojos.EhStatEventContentLogs;
import com.everhomes.server.schema.tables.pojos.EhStatEventDeviceLogs;
import com.everhomes.server.schema.tables.pojos.EhStatEventLogs;
import com.everhomes.server.schema.tables.pojos.EhStatEventParamLogs;
import com.everhomes.server.schema.tables.pojos.EhStatEventParams;
import com.everhomes.server.schema.tables.pojos.EhStatEventPortalConfigs;
import com.everhomes.server.schema.tables.pojos.EhStatEventPortalStatistics;
import com.everhomes.server.schema.tables.pojos.EhStatEventStatistics;
import com.everhomes.server.schema.tables.pojos.EhStatEventTaskLogs;
import com.everhomes.server.schema.tables.pojos.EhStatEventUploadStrategies;
import com.everhomes.server.schema.tables.pojos.EhStatEvents;
import com.everhomes.server.schema.tables.pojos.EhStatOrders;
import com.everhomes.server.schema.tables.pojos.EhStatRefunds;
import com.everhomes.server.schema.tables.pojos.EhStatServiceSettlementResults;
import com.everhomes.server.schema.tables.pojos.EhStatSettlements;
import com.everhomes.server.schema.tables.pojos.EhStatTaskLogs;
import com.everhomes.server.schema.tables.pojos.EhStatTransactions;
import com.everhomes.server.schema.tables.pojos.EhTalentCategories;
import com.everhomes.server.schema.tables.pojos.EhTalentMessageSenders;
import com.everhomes.server.schema.tables.pojos.EhTalentQueryHistories;
import com.everhomes.server.schema.tables.pojos.EhTalentRequests;
import com.everhomes.server.schema.tables.pojos.EhTalents;
import com.everhomes.server.schema.tables.pojos.EhTechparkSyncdataBackup;
import com.everhomes.server.schema.tables.pojos.EhTerminalAppVersionActives;
import com.everhomes.server.schema.tables.pojos.EhTerminalAppVersionCumulatives;
import com.everhomes.server.schema.tables.pojos.EhTerminalAppVersionStatistics;
import com.everhomes.server.schema.tables.pojos.EhTerminalDayStatistics;
import com.everhomes.server.schema.tables.pojos.EhTerminalHourStatistics;
import com.everhomes.server.schema.tables.pojos.EhTerminalStatisticsTasks;
import com.everhomes.server.schema.tables.pojos.EhThirdpartConfigurations;
import com.everhomes.server.schema.tables.pojos.EhUniongroupConfigures;
import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;
import com.everhomes.server.schema.tables.pojos.EhUserBlacklists;
import com.everhomes.server.schema.tables.pojos.EhUserCommunities;
import com.everhomes.server.schema.tables.pojos.EhUserFavorites;
import com.everhomes.server.schema.tables.pojos.EhUserGroupHistories;
import com.everhomes.server.schema.tables.pojos.EhUserGroups;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.server.schema.tables.pojos.EhUserImpersonations;
import com.everhomes.server.schema.tables.pojos.EhUserInvitationRoster;
import com.everhomes.server.schema.tables.pojos.EhUserInvitations;
import com.everhomes.server.schema.tables.pojos.EhUserLaunchPadItems;
import com.everhomes.server.schema.tables.pojos.EhUserLikes;
import com.everhomes.server.schema.tables.pojos.EhUserNotificationSettings;
import com.everhomes.server.schema.tables.pojos.EhUserOrganizations;
import com.everhomes.server.schema.tables.pojos.EhUserPosts;
import com.everhomes.server.schema.tables.pojos.EhUserProfiles;
import com.everhomes.server.schema.tables.pojos.EhUserServiceAddresses;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.pojos.EhVarFieldGroupScopes;
import com.everhomes.server.schema.tables.pojos.EhVarFieldGroups;
import com.everhomes.server.schema.tables.pojos.EhVarFieldItemScopes;
import com.everhomes.server.schema.tables.pojos.EhVarFieldItems;
import com.everhomes.server.schema.tables.pojos.EhVarFieldScopes;
import com.everhomes.server.schema.tables.pojos.EhVarFields;
import com.everhomes.server.schema.tables.pojos.EhVersionRealm;
import com.everhomes.server.schema.tables.pojos.EhVersionUpgradeRules;
import com.everhomes.server.schema.tables.pojos.EhVersionUrls;
import com.everhomes.server.schema.tables.pojos.EhVersionedContent;
import com.everhomes.server.schema.tables.pojos.EhWarehouseMaterialCategories;
import com.everhomes.server.schema.tables.pojos.EhWarehouseMaterials;
import com.everhomes.server.schema.tables.pojos.EhWarehouseRequestMaterials;
import com.everhomes.server.schema.tables.pojos.EhWarehouseRequests;
import com.everhomes.server.schema.tables.pojos.EhWarehouseStockLogs;
import com.everhomes.server.schema.tables.pojos.EhWarehouseStocks;
import com.everhomes.server.schema.tables.pojos.EhWarehouseUnits;
import com.everhomes.server.schema.tables.pojos.EhWarehouses;
import com.everhomes.server.schema.tables.pojos.EhWarningContacts;
import com.everhomes.server.schema.tables.pojos.EhWarningSettings;
import com.everhomes.server.schema.tables.pojos.EhWebMenus;
import com.everhomes.server.schema.tables.pojos.EhWifiSettings;
import com.everhomes.server.schema.tables.pojos.EhYellowPageAttachments;
import com.everhomes.server.schema.tables.pojos.EhYellowPages;
import com.everhomes.server.schema.tables.pojos.EhYzbDevices;
import com.everhomes.server.schema.tables.pojos.EhYzxSmsLogs;
import com.everhomes.server.schema.tables.pojos.EhZjSyncdataBackup;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SequenceServiceImpl implements SequenceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceServiceImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private UserProvider userProvider;

    @Override
    public void syncSequence() {
        syncTableSequence(EhAcls.class, EhAcls.class, com.everhomes.schema.Tables.EH_ACLS.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACLS.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACLS).fetchOne().value1();
        });

        syncTableSequence(EhAcls.class, EhAclPrivileges.class, com.everhomes.schema.Tables.EH_ACL_PRIVILEGES.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACL_PRIVILEGES.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACL_PRIVILEGES).fetchOne().value1();
        });

        syncTableSequence(EhAcls.class, EhAclRoles.class, com.everhomes.schema.Tables.EH_ACL_ROLES.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACL_ROLES.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACL_ROLES).fetchOne().value1();
        });

        syncTableSequence(EhAcls.class, EhAclRoleAssignments.class, com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS.ID.max())
                    .from(com.everhomes.schema.Tables.EH_ACL_ROLE_ASSIGNMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServerShardMap.class, com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP.getName(), (dbContext) -> {
            Integer max = dbContext.select(com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP.ID.max())
                    .from(com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP).fetchOne().value1();
            Long lmax = null;
            if (max != null) {
                lmax = Long.valueOf(max.longValue());
            }
            return lmax;
        });

        syncTableSequence(null, EhContentShardMap.class, com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP.ID.max())
                    .from(com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUsers.class, Tables.EH_USERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USERS.ID.max()).from(Tables.EH_USERS).fetchOne().value1();
        });

        // user account is a special field, it default to be number stype, but it can be changed to any character only if they are unique in db
        syncUserAccountName();

        syncTableSequence(EhUniongroupConfigures.class, EhUniongroupConfigures.class, Tables.EH_UNIONGROUP_CONFIGURES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_UNIONGROUP_CONFIGURES.ID.max()).from(Tables.EH_UNIONGROUP_CONFIGURES).fetchOne().value1();
        });
        syncTableSequence(EhUniongroupMemberDetails.class, EhUniongroupMemberDetails.class, Tables.EH_UNIONGROUP_MEMBER_DETAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ID.max()).from(Tables.EH_UNIONGROUP_MEMBER_DETAILS).fetchOne().value1();
        });
        syncTableSequence(EhUsers.class, EhUserGroups.class, Tables.EH_USER_GROUPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_GROUPS.ID.max()).from(Tables.EH_USER_GROUPS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserInvitations.class, Tables.EH_USER_INVITATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_INVITATIONS.ID.max()).from(Tables.EH_USER_INVITATIONS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserIdentifiers.class, Tables.EH_USER_IDENTIFIERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_IDENTIFIERS.ID.max()).from(Tables.EH_USER_IDENTIFIERS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserInvitationRoster.class, Tables.EH_USER_INVITATION_ROSTER.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_INVITATION_ROSTER.ID.max()).from(Tables.EH_USER_INVITATION_ROSTER).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserPosts.class, Tables.EH_USER_POSTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_POSTS.ID.max()).from(Tables.EH_USER_POSTS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserLikes.class, Tables.EH_USER_LIKES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_LIKES.ID.max()).from(Tables.EH_USER_LIKES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserFavorites.class, Tables.EH_USER_FAVORITES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_FAVORITES.ID.max()).from(Tables.EH_USER_FAVORITES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserProfiles.class, Tables.EH_USER_PROFILES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_PROFILES.ID.max()).from(Tables.EH_USER_PROFILES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserServiceAddresses.class, Tables.EH_USER_SERVICE_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_SERVICE_ADDRESSES.ID.max()).from(Tables.EH_USER_SERVICE_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserCommunities.class, Tables.EH_USER_COMMUNITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_COMMUNITIES.ID.max()).from(Tables.EH_USER_COMMUNITIES).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForums.class, Tables.EH_FORUMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FORUMS.ID.max()).from(Tables.EH_FORUMS).fetchOne().value1();
        });

        syncTableSequence(EhAddresses.class, EhAddresses.class, Tables.EH_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ADDRESSES.ID.max()).from(Tables.EH_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(null, EhContentServerResources.class, Tables.EH_CONTENT_SERVER_RESOURCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTENT_SERVER_RESOURCES.ID.max())
                    .from(Tables.EH_CONTENT_SERVER_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhGroups.class, Tables.EH_GROUPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUPS.ID.max()).from(Tables.EH_GROUPS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhGroupMembers.class, Tables.EH_GROUP_MEMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_MEMBERS.ID.max()).from(Tables.EH_GROUP_MEMBERS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhGroupOpRequests.class, Tables.EH_GROUP_OP_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_OP_REQUESTS.ID.max()).from(Tables.EH_GROUP_OP_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForumPosts.class, Tables.EH_FORUM_POSTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_POSTS.ID.max()).from(Tables.EH_FORUM_POSTS).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForumAssignedScopes.class, Tables.EH_FORUM_ASSIGNED_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_FORUM_ASSIGNED_SCOPES).fetchOne().value1();
        });

        syncTableSequence(EhForums.class, EhForumAttachments.class, Tables.EH_FORUM_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FORUM_ATTACHMENTS.ID.max()).from(Tables.EH_FORUM_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhCommunities.class, Tables.EH_COMMUNITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITIES.ID.max()).from(Tables.EH_COMMUNITIES).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhCommunityGeopoints.class, Tables.EH_COMMUNITY_GEOPOINTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_GEOPOINTS.ID.max()).from(Tables.EH_COMMUNITY_GEOPOINTS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhNearbyCommunityMap.class, Tables.EH_NEARBY_COMMUNITY_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NEARBY_COMMUNITY_MAP.ID.max()).from(Tables.EH_NEARBY_COMMUNITY_MAP).fetchOne().value1();
        });

        syncTableSequence(EhActivities.class, EhActivities.class, Tables.EH_ACTIVITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITIES.ID.max()).from(Tables.EH_ACTIVITIES).fetchOne().value1();
        });

        syncTableSequence(EhActivities.class, EhActivityRoster.class, Tables.EH_ACTIVITY_ROSTER.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_ROSTER.ID.max()).from(Tables.EH_ACTIVITY_ROSTER).fetchOne().value1();
        });

        syncTableSequence(EhPolls.class, EhPolls.class, Tables.EH_POLLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_POLLS.ID.max()).from(Tables.EH_POLLS).fetchOne().value1();
        });

        syncTableSequence(EhPolls.class, EhPollItems.class, Tables.EH_POLL_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_POLL_ITEMS.ID.max()).from(Tables.EH_POLL_ITEMS).fetchOne().value1();
        });

        syncTableSequence(EhPolls.class, EhPollVotes.class, Tables.EH_POLL_VOTES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_POLL_VOTES.ID.max()).from(Tables.EH_POLL_VOTES).fetchOne().value1();
        });

        syncTableSequence(null, EhQrcodes.class, Tables.EH_QRCODES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QRCODES.ID.max()).from(Tables.EH_QRCODES).fetchOne().value1();
        });

        syncTableSequence(null, EhOauth2Codes.class, Tables.EH_OAUTH2_CODES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_CODES.ID.max()).from(Tables.EH_OAUTH2_CODES).fetchOne().value1();
        });

        syncTableSequence(null, EhOauth2Tokens.class, Tables.EH_OAUTH2_TOKENS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_TOKENS.ID.max()).from(Tables.EH_OAUTH2_TOKENS).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionRealm.class, Tables.EH_VERSION_REALM.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VERSION_REALM.ID.max()).from(Tables.EH_VERSION_REALM).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionUpgradeRules.class, Tables.EH_VERSION_UPGRADE_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VERSION_UPGRADE_RULES.ID.max()).from(Tables.EH_VERSION_UPGRADE_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionUrls.class, Tables.EH_VERSION_URLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VERSION_URLS.ID.max()).from(Tables.EH_VERSION_URLS).fetchOne().value1();
        });

        syncTableSequence(null, EhVersionedContent.class, Tables.EH_VERSIONED_CONTENT.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VERSIONED_CONTENT.ID.max()).from(Tables.EH_VERSIONED_CONTENT).fetchOne().value1();
        });

        syncTableSequence(null, EhCooperationRequests.class, Tables.EH_COOPERATION_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COOPERATION_REQUESTS.ID.max()).from(Tables.EH_COOPERATION_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinesses.class, Tables.EH_BUSINESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESSES.ID.max()).from(Tables.EH_BUSINESSES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessVisibleScopes.class, Tables.EH_BUSINESS_VISIBLE_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_VISIBLE_SCOPES.ID.max()).from(Tables.EH_BUSINESS_VISIBLE_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessAssignedScopes.class, Tables.EH_BUSINESS_ASSIGNED_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_BUSINESS_ASSIGNED_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessCategories.class, Tables.EH_BUSINESS_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_CATEGORIES.ID.max()).from(Tables.EH_BUSINESS_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhPushMessageResults.class, Tables.EH_PUSH_MESSAGE_RESULTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUSH_MESSAGE_RESULTS.ID.max()).from(Tables.EH_PUSH_MESSAGE_RESULTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContacts.class, Tables.EH_ENTERPRISE_CONTACTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACTS.ID.max()).from(Tables.EH_ENTERPRISE_CONTACTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContactEntries.class, Tables.EH_ENTERPRISE_CONTACT_ENTRIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACT_ENTRIES.ID.max()).from(Tables.EH_ENTERPRISE_CONTACT_ENTRIES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContactGroups.class, Tables.EH_ENTERPRISE_CONTACT_GROUPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACT_GROUPS.ID.max()).from(Tables.EH_ENTERPRISE_CONTACT_GROUPS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseContactGroupMembers.class, Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS.ID.max()).from(Tables.EH_ENTERPRISE_CONTACT_GROUP_MEMBERS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchDayLogs.class, Tables.EH_PUNCH_DAY_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_DAY_LOGS.ID.max()).from(Tables.EH_PUNCH_DAY_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchExceptionApprovals.class, Tables.EH_PUNCH_EXCEPTION_APPROVALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_EXCEPTION_APPROVALS.ID.max()).from(Tables.EH_PUNCH_EXCEPTION_APPROVALS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchExceptionRequests.class, Tables.EH_PUNCH_EXCEPTION_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_EXCEPTION_REQUESTS.ID.max()).from(Tables.EH_PUNCH_EXCEPTION_REQUESTS).fetchOne().value1();
        });


        syncTableSequence(null, EhPunchGeopoints.class, Tables.EH_PUNCH_GEOPOINTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_GEOPOINTS.ID.max()).from(Tables.EH_PUNCH_GEOPOINTS).fetchOne().value1();
        });


        syncTableSequence(null, EhPunchLogs.class, Tables.EH_PUNCH_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_LOGS.ID.max()).from(Tables.EH_PUNCH_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchRules.class, Tables.EH_PUNCH_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_RULES.ID.max()).from(Tables.EH_PUNCH_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWorkday.class, Tables.EH_PUNCH_WORKDAY.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WORKDAY.ID.max()).from(Tables.EH_PUNCH_WORKDAY).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchTimeRules.class, Tables.EH_PUNCH_TIME_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_TIME_RULES.ID.max()).from(Tables.EH_PUNCH_TIME_RULES).fetchOne().value1();
        });
        syncTableSequence(null, EhPunchLocationRules.class, Tables.EH_PUNCH_LOCATION_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_LOCATION_RULES.ID.max()).from(Tables.EH_PUNCH_LOCATION_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWifis.class, Tables.EH_PUNCH_WIFIS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WIFIS.ID.max()).from(Tables.EH_PUNCH_WIFIS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWifiRules.class, Tables.EH_PUNCH_WIFI_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WIFI_RULES.ID.max()).from(Tables.EH_PUNCH_WIFI_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchHolidays.class, Tables.EH_PUNCH_HOLIDAYS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_HOLIDAYS.ID.max()).from(Tables.EH_PUNCH_HOLIDAYS).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchWorkdayRules.class, Tables.EH_PUNCH_WORKDAY_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_WORKDAY_RULES.ID.max()).from(Tables.EH_PUNCH_WORKDAY_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchRuleOwnerMap.class, Tables.EH_PUNCH_RULE_OWNER_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_RULE_OWNER_MAP.ID.max()).from(Tables.EH_PUNCH_RULE_OWNER_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhPunchStatistics.class, Tables.EH_PUNCH_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_STATISTICS.ID.max()).from(Tables.EH_PUNCH_STATISTICS).fetchOne().value1();
        });


        syncTableSequence(null, EhPunchSchedulings.class, Tables.EH_PUNCH_SCHEDULINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PUNCH_SCHEDULINGS.ID.max()).from(Tables.EH_PUNCH_SCHEDULINGS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalBillAttachments.class, Tables.EH_RENTAL_BILL_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_BILL_ATTACHMENTS.ID.max()).from(Tables.EH_RENTAL_BILL_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalBills.class, Tables.EH_RENTAL_BILLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_BILLS.ID.max()).from(Tables.EH_RENTAL_BILLS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalItemsBills.class, Tables.EH_RENTAL_ITEMS_BILLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_ITEMS_BILLS.ID.max()).from(Tables.EH_RENTAL_ITEMS_BILLS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalRules.class, Tables.EH_RENTAL_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_RULES.ID.max()).from(Tables.EH_RENTAL_RULES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalSiteItems.class, Tables.EH_RENTAL_SITE_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITE_ITEMS.ID.max()).from(Tables.EH_RENTAL_SITE_ITEMS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalSiteRules.class, Tables.EH_RENTAL_SITE_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITE_RULES.ID.max()).from(Tables.EH_RENTAL_SITE_RULES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalSites.class, Tables.EH_RENTAL_SITES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITES.ID.max()).from(Tables.EH_RENTAL_SITES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhRentalSitesBills.class, Tables.EH_RENTAL_SITES_BILLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_SITES_BILLS.ID.max()).from(Tables.EH_RENTAL_SITES_BILLS).fetchOne().value1();
        });


        syncTableSequence(EhGroups.class, EhRentalBillPaybillMap.class, Tables.EH_RENTAL_BILL_PAYBILL_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTAL_BILL_PAYBILL_MAP.ID.max()).from(Tables.EH_RENTAL_BILL_PAYBILL_MAP).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2DefaultRules.class, Tables.EH_RENTALV2_DEFAULT_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_DEFAULT_RULES.ID.max()).from(Tables.EH_RENTALV2_DEFAULT_RULES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2TimeInterval.class, Tables.EH_RENTALV2_TIME_INTERVAL.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_TIME_INTERVAL.ID.max()).from(Tables.EH_RENTALV2_TIME_INTERVAL).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2CloseDates.class, Tables.EH_RENTALV2_CLOSE_DATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_CLOSE_DATES.ID.max()).from(Tables.EH_RENTALV2_CLOSE_DATES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ConfigAttachments.class, Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.ID.max()).from(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourceRanges.class, Tables.EH_RENTALV2_RESOURCE_RANGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_RANGES.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_RANGES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourcePics.class, Tables.EH_RENTALV2_RESOURCE_PICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_PICS.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_PICS).fetchOne().value1();
        });


        syncTableSequence(EhOrganizations.class, EhRentalv2OrderAttachments.class, Tables.EH_RENTALV2_ORDER_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ORDER_ATTACHMENTS.ID.max()).from(Tables.EH_RENTALV2_ORDER_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2Orders.class, Tables.EH_RENTALV2_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ORDERS.ID.max()).from(Tables.EH_RENTALV2_ORDERS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourceNumbers.class, Tables.EH_RENTALV2_RESOURCE_NUMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_NUMBERS.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_NUMBERS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2Items.class, Tables.EH_RENTALV2_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ITEMS.ID.max()).from(Tables.EH_RENTALV2_ITEMS).fetchOne().value1();
        });
        // cell的id从资源的cellendid取
        syncTableSequence(EhOrganizations.class, EhRentalv2Cells.class, Tables.EH_RENTALV2_CELLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCES.CELL_END_ID.max()).from(Tables.EH_RENTALV2_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2Resources.class, Tables.EH_RENTALV2_RESOURCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCES.ID.max()).from(Tables.EH_RENTALV2_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2RefundOrders.class, Tables.EH_RENTALV2_REFUND_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_REFUND_ORDERS.ID.max()).from(Tables.EH_RENTALV2_REFUND_ORDERS).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourceOrders.class, Tables.EH_RENTALV2_RESOURCE_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_ORDERS.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_ORDERS).fetchOne().value1();
        });


        syncTableSequence(EhOrganizations.class, EhRentalv2OrderPayorderMap.class, Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ID.max()).from(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP).fetchOne().value1();
        });

        syncTableSequence(EhOrganizations.class, EhRentalv2ResourceTypes.class, Tables.EH_RENTALV2_RESOURCE_TYPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.max()).from(Tables.EH_RENTALV2_RESOURCE_TYPES).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhRechargeInfo.class, Tables.EH_RECHARGE_INFO.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RECHARGE_INFO.ID.max()).from(Tables.EH_RECHARGE_INFO).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseCommunityMap.class, Tables.EH_ENTERPRISE_COMMUNITY_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_COMMUNITY_MAP.ID.max()).from(Tables.EH_ENTERPRISE_COMMUNITY_MAP).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseAddresses.class, Tables.EH_ENTERPRISE_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_ADDRESSES.ID.max()).from(Tables.EH_ENTERPRISE_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseAttachments.class, Tables.EH_ENTERPRISE_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_ATTACHMENTS.ID.max()).from(Tables.EH_ENTERPRISE_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhBuildings.class, Tables.EH_BUILDINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUILDINGS.ID.max()).from(Tables.EH_BUILDINGS).fetchOne().value1();
        });

        syncTableSequence(EhCommunities.class, EhBuildingAttachments.class, Tables.EH_BUILDING_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUILDING_ATTACHMENTS.ID.max()).from(Tables.EH_BUILDING_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhParkCharge.class, Tables.EH_PARK_CHARGE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARK_CHARGE.ID.max()).from(Tables.EH_PARK_CHARGE).fetchOne().value1();
        });

        syncTableSequence(null, EhParkApplyCard.class, Tables.EH_PARK_APPLY_CARD.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARK_APPLY_CARD.ID.max()).from(Tables.EH_PARK_APPLY_CARD).fetchOne().value1();
        });

        syncTableSequence(null, EhRechargeInfo.class, Tables.EH_RECHARGE_INFO.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RECHARGE_INFO.ID.max()).from(Tables.EH_RECHARGE_INFO).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizations.class, Tables.EH_ORGANIZATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATIONS.ID.max()).from(Tables.EH_ORGANIZATIONS).fetchOne().value1();
        });


        syncTableSequence(null, EhYellowPages.class, Tables.EH_YELLOW_PAGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_YELLOW_PAGES.ID.max()).from(Tables.EH_YELLOW_PAGES).fetchOne().value1();
        });

        syncTableSequence(null, EhYellowPageAttachments.class, Tables.EH_YELLOW_PAGE_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_YELLOW_PAGE_ATTACHMENTS.ID.max()).from(Tables.EH_YELLOW_PAGE_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhNamespaceResources.class, Tables.EH_NAMESPACE_RESOURCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NAMESPACE_RESOURCES.ID.max()).from(Tables.EH_NAMESPACE_RESOURCES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhEnterpriseDetails.class, Tables.EH_ENTERPRISE_DETAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_DETAILS.ID.max()).from(Tables.EH_ENTERPRISE_DETAILS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnterpriseOpRequests.class, Tables.EH_ENTERPRISE_OP_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_OP_REQUESTS.ID.max()).from(Tables.EH_ENTERPRISE_OP_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhLeasePromotions.class, Tables.EH_LEASE_PROMOTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROMOTIONS.ID.max()).from(Tables.EH_LEASE_PROMOTIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhLeasePromotionAttachments.class, Tables.EH_LEASE_PROMOTION_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROMOTION_ATTACHMENTS.ID.max()).from(Tables.EH_LEASE_PROMOTION_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(EhUsers.class, EhUserGroupHistories.class, Tables.EH_USER_GROUP_HISTORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_GROUP_HISTORIES.ID.max()).from(Tables.EH_USER_GROUP_HISTORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhConfAccountCategories.class, Tables.EH_CONF_ACCOUNT_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ACCOUNT_CATEGORIES.ID.max()).from(Tables.EH_CONF_ACCOUNT_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(EhConfOrders.class, EhConfInvoices.class, Tables.EH_CONF_INVOICES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_INVOICES.ID.max()).from(Tables.EH_CONF_INVOICES).fetchOne().value1();
        });

        syncTableSequence(null, EhConfOrders.class, Tables.EH_CONF_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ORDERS.ID.max()).from(Tables.EH_CONF_ORDERS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfOrderAccountMap.class, Tables.EH_CONF_ORDER_ACCOUNT_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ID.max()).from(Tables.EH_CONF_ORDER_ACCOUNT_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhConfSourceAccounts.class, Tables.EH_CONF_SOURCE_ACCOUNTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_SOURCE_ACCOUNTS.ID.max()).from(Tables.EH_CONF_SOURCE_ACCOUNTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfAccounts.class, Tables.EH_CONF_ACCOUNTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ACCOUNTS.ID.max()).from(Tables.EH_CONF_ACCOUNTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfAccountHistories.class, Tables.EH_CONF_ACCOUNT_HISTORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ACCOUNT_HISTORIES.ID.max()).from(Tables.EH_CONF_ACCOUNT_HISTORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhConfConferences.class, Tables.EH_CONF_CONFERENCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_CONFERENCES.ID.max()).from(Tables.EH_CONF_CONFERENCES).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfEnterprises.class, Tables.EH_CONF_ENTERPRISES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_ENTERPRISES.ID.max()).from(Tables.EH_CONF_ENTERPRISES).fetchOne().value1();
        });

        syncTableSequence(null, EhWarningContacts.class, Tables.EH_WARNING_CONTACTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WARNING_CONTACTS.ID.max()).from(Tables.EH_WARNING_CONTACTS).fetchOne().value1();
        });

        syncTableSequence(EhGroups.class, EhConfReservations.class, Tables.EH_CONF_RESERVATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONF_RESERVATIONS.ID.max()).from(Tables.EH_CONF_RESERVATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationCommunityRequests.class, Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.ID.max()).from(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationDetails.class, Tables.EH_ORGANIZATION_DETAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_DETAILS.ID.max()).from(Tables.EH_ORGANIZATION_DETAILS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAddresses.class, Tables.EH_ORGANIZATION_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESSES.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESSES).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAttachments.class, Tables.EH_ORGANIZATION_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationRoleMap.class, Tables.EH_ORGANIZATION_ROLE_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ROLE_MAP.ID.max()).from(Tables.EH_ORGANIZATION_ROLE_MAP).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationTaskTargets.class, Tables.EH_ORGANIZATION_TASK_TARGETS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_TASK_TARGETS.ID.max()).from(Tables.EH_ORGANIZATION_TASK_TARGETS).fetchOne().value1();
        });

        syncTableSequence(EhMessages.class, EhMessages.class, com.everhomes.schema.Tables.EH_MESSAGES.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_MESSAGES.ID.max()).from(com.everhomes.schema.Tables.EH_MESSAGES).fetchOne().value1();
        });

        syncTableSequence(EhMessageBoxs.class, EhMessageBoxs.class, com.everhomes.schema.Tables.EH_MESSAGE_BOXS.getName(), (dbContext) -> {
            return dbContext.select(com.everhomes.schema.Tables.EH_MESSAGE_BOXS.ID.max()).from(com.everhomes.schema.Tables.EH_MESSAGE_BOXS).fetchOne().value1();
        });


        syncTableSequence(EhRepeatSettings.class, EhRepeatSettings.class, Tables.EH_REPEAT_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_REPEAT_SETTINGS.ID.max()).from(Tables.EH_REPEAT_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionStandards.class, EhQualityInspectionStandards.class, Tables.EH_QUALITY_INSPECTION_STANDARDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_STANDARDS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_STANDARDS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionStandardGroupMap.class, EhQualityInspectionStandardGroupMap.class, Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_STANDARD_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionTasks.class, EhQualityInspectionTasks.class, Tables.EH_QUALITY_INSPECTION_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASKS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASKS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionEvaluationFactors.class, EhQualityInspectionEvaluationFactors.class, Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_EVALUATION_FACTORS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionEvaluations.class, EhQualityInspectionEvaluations.class, Tables.EH_QUALITY_INSPECTION_EVALUATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_EVALUATIONS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_EVALUATIONS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionTaskRecords.class, EhQualityInspectionTaskRecords.class, Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASK_RECORDS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionTaskAttachments.class, EhQualityInspectionTaskAttachments.class, Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASK_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(EhQualityInspectionCategories.class, EhQualityInspectionCategories.class, Tables.EH_QUALITY_INSPECTION_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_CATEGORIES.ID.max()).from(Tables.EH_QUALITY_INSPECTION_CATEGORIES).fetchOne().value1();
        });

        // 同步主表sequence时，第一个参数需要指定主表对应的pojo class by lqs 20160430
        //syncTableSequence(null, EhDoorAccess.class, Tables.EH_DOOR_ACCESS.getName(), (dbContext) -> {
        syncTableSequence(EhDoorAccess.class, EhDoorAccess.class, Tables.EH_DOOR_ACCESS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_ACCESS.ID.max()).from(Tables.EH_DOOR_ACCESS).fetchOne().value1();
        });
        syncTableSequence(null, EhOwnerDoors.class, Tables.EH_OWNER_DOORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OWNER_DOORS.ID.max()).from(Tables.EH_OWNER_DOORS).fetchOne().value1();
        });
        syncTableSequence(null, EhDoorAuth.class, Tables.EH_DOOR_AUTH.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_AUTH.ID.max()).from(Tables.EH_DOOR_AUTH).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkFirmware.class, Tables.EH_ACLINK_FIRMWARE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_FIRMWARE.ID.max()).from(Tables.EH_ACLINK_FIRMWARE).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAclinks.class, Tables.EH_ACLINKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINKS.ID.max()).from(Tables.EH_ACLINKS).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAesServerKey.class, Tables.EH_AES_SERVER_KEY.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_AES_SERVER_KEY.ID.max()).from(Tables.EH_AES_SERVER_KEY).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAesUserKey.class, Tables.EH_AES_USER_KEY.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_AES_USER_KEY.ID.max()).from(Tables.EH_AES_USER_KEY).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhAclinkUndoKey.class, Tables.EH_ACLINK_UNDO_KEY.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_UNDO_KEY.ID.max()).from(Tables.EH_ACLINK_UNDO_KEY).fetchOne().value1();
        });
        syncTableSequence(EhDoorAccess.class, EhDoorCommand.class, Tables.EH_DOOR_COMMAND.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_COMMAND.ID.max()).from(Tables.EH_DOOR_COMMAND).fetchOne().value1();
        });

        syncTableSequence(null, EhOpPromotionAssignedScopes.class, Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_OP_PROMOTION_ASSIGNED_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhOpPromotionActivities.class, Tables.EH_OP_PROMOTION_ACTIVITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OP_PROMOTION_ACTIVITIES.ID.max()).from(Tables.EH_OP_PROMOTION_ACTIVITIES).fetchOne().value1();
        });


        syncTableSequence(null, EhScheduleTasks.class, Tables.EH_SCHEDULE_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SCHEDULE_TASKS.ID.max()).from(Tables.EH_SCHEDULE_TASKS).fetchOne().value1();
        });


        syncTableSequence(null, EhScheduleTaskLogs.class, Tables.EH_SCHEDULE_TASK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SCHEDULE_TASK_LOGS.ID.max()).from(Tables.EH_SCHEDULE_TASK_LOGS).fetchOne().value1();
        });


        syncTableSequence(null, EhOpPromotionMessages.class, Tables.EH_OP_PROMOTION_MESSAGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OP_PROMOTION_MESSAGES.ID.max()).from(Tables.EH_OP_PROMOTION_MESSAGES).fetchOne().value1();
        });

        syncTableSequence(null, EhBusinessAssignedNamespaces.class, Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.ID.max()).from(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES).fetchOne().value1();
        });

        syncTableSequence(null, EhParkingRechargeOrders.class, Tables.EH_PARKING_RECHARGE_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_RECHARGE_ORDERS.ID.max()).from(Tables.EH_PARKING_RECHARGE_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingRechargeRates.class, Tables.EH_PARKING_RECHARGE_RATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_RECHARGE_RATES.ID.max()).from(Tables.EH_PARKING_RECHARGE_RATES).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingCardRequests.class, Tables.EH_PARKING_CARD_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CARD_REQUESTS.ID.max()).from(Tables.EH_PARKING_CARD_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingLots.class, Tables.EH_PARKING_LOTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_LOTS.ID.max()).from(Tables.EH_PARKING_LOTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingVendors.class, Tables.EH_PARKING_VENDORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_VENDORS.ID.max()).from(Tables.EH_PARKING_VENDORS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmsyPayers.class, Tables.EH_PMSY_PAYERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_PAYERS.ID.max()).from(Tables.EH_PMSY_PAYERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmsyOrders.class, Tables.EH_PMSY_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_ORDERS.ID.max()).from(Tables.EH_PMSY_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmsyOrderItems.class, Tables.EH_PMSY_ORDER_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_ORDER_ITEMS.ID.max()).from(Tables.EH_PMSY_ORDER_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmsyCommunities.class, Tables.EH_PMSY_COMMUNITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PMSY_COMMUNITIES.ID.max()).from(Tables.EH_PMSY_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhWifiSettings.class, Tables.EH_WIFI_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WIFI_SETTINGS.ID.max()).from(Tables.EH_WIFI_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCards.class, Tables.EH_PAYMENT_CARDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARDS.ID.max()).from(Tables.EH_PAYMENT_CARDS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardIssuers.class, Tables.EH_PAYMENT_CARD_ISSUERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_ISSUERS.ID.max()).from(Tables.EH_PAYMENT_CARD_ISSUERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardIssuerCommunities.class, Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.ID.max()).from(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardRechargeOrders.class, Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.ID.max()).from(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhPaymentCardTransactions.class, Tables.EH_PAYMENT_CARD_TRANSACTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PAYMENT_CARD_TRANSACTIONS.ID.max()).from(Tables.EH_PAYMENT_CARD_TRANSACTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionLogs.class, Tables.EH_QUALITY_INSPECTION_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_LOGS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhHotTags.class, Tables.EH_HOT_TAGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_HOT_TAGS.ID.max()).from(Tables.EH_HOT_TAGS).fetchOne().value1();
        });
        syncTableSequence(null, EhNews.class, Tables.EH_NEWS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS.ID.max()).from(Tables.EH_NEWS).fetchOne().value1();
        });
        syncTableSequence(null, EhNewsComment.class, Tables.EH_NEWS_COMMENT.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS_COMMENT.ID.max()).from(Tables.EH_NEWS_COMMENT).fetchOne().value1();
        });

        //小区导入时添加
        syncTableSequence(null, EhNamespaces.class, com.everhomes.schema.Tables.EH_NAMESPACES.getName(), (dbContext) -> {
            Integer maxId = dbContext.select(com.everhomes.schema.Tables.EH_NAMESPACES.ID.max()).from(com.everhomes.schema.Tables.EH_NAMESPACES).fetchOne().value1();
            if (maxId != null) {
                return Long.valueOf(maxId.longValue());
            }
            return null;
        });
        syncTableSequence(null, EhNamespaceDetails.class, Tables.EH_NAMESPACE_DETAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NAMESPACE_DETAILS.ID.max()).from(Tables.EH_NAMESPACE_DETAILS).fetchOne().value1();
        });
        syncTableSequence(null, EhConfigurations.class, com.everhomes.schema.Tables.EH_CONFIGURATIONS.getName(), (dbContext) -> {
            Integer maxId = dbContext.select(com.everhomes.schema.Tables.EH_CONFIGURATIONS.ID.max()).from(com.everhomes.schema.Tables.EH_CONFIGURATIONS).fetchOne().value1();
            if (maxId != null) {
                return Long.valueOf(maxId.longValue());
            }
            return null;
        });

        syncTableSequence(null, EhEquipmentInspectionStandards.class, Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_STANDARDS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipments.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionAccessories.class, Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionAccessoryMap.class, Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ACCESSORY_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentParameters.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_PARAMETERS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentAttachments.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhLocaleTemplates.class, Tables.EH_LOCALE_TEMPLATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LOCALE_TEMPLATES.ID.max()).from(Tables.EH_LOCALE_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhCategories.class, Tables.EH_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CATEGORIES.ID.max()).from(Tables.EH_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTasks.class, Tables.EH_EQUIPMENT_INSPECTION_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TASKS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TASKS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTaskAttachments.class, Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TASK_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTaskLogs.class, Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TASK_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhOfficeCubicleOrders.class, Tables.EH_OFFICE_CUBICLE_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_ORDERS.ID.max()).from(Tables.EH_OFFICE_CUBICLE_ORDERS).fetchOne().value1();
        });

        syncTableSequence(null, EhOfficeCubicleCategories.class, Tables.EH_OFFICE_CUBICLE_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_CATEGORIES.ID.max()).from(Tables.EH_OFFICE_CUBICLE_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhOfficeCubicleSpaces.class, Tables.EH_OFFICE_CUBICLE_SPACES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_SPACES.ID.max()).from(Tables.EH_OFFICE_CUBICLE_SPACES).fetchOne().value1();
        });
        syncTableSequence(null, EhOfficeCubicleAttachments.class, Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.ID.max()).from(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMembers.class, Tables.EH_ORGANIZATION_MEMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBERS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBERS).fetchOne().value1();
        });
        syncTableSequence(null, EhRichTexts.class, Tables.EH_RICH_TEXTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RICH_TEXTS.ID.max()).from(Tables.EH_RICH_TEXTS).fetchOne().value1();
        });
        syncTableSequence(null, EhJournals.class, Tables.EH_JOURNALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_JOURNALS.ID.max()).from(Tables.EH_JOURNALS).fetchOne().value1();
        });
        syncTableSequence(null, EhJournalConfigs.class, Tables.EH_JOURNAL_CONFIGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_JOURNAL_CONFIGS.ID.max()).from(Tables.EH_JOURNAL_CONFIGS).fetchOne().value1();
        });

        syncTableSequence(null, EhCategories.class, Tables.EH_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CATEGORIES.ID.max()).from(Tables.EH_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTasks.class, Tables.EH_PM_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASKS.ID.max()).from(Tables.EH_PM_TASKS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskAttachments.class, Tables.EH_PM_TASK_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_ATTACHMENTS.ID.max()).from(Tables.EH_PM_TASK_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskLogs.class, Tables.EH_PM_TASK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_LOGS.ID.max()).from(Tables.EH_PM_TASK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskStatistics.class, Tables.EH_PM_TASK_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_STATISTICS.ID.max()).from(Tables.EH_PM_TASK_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhUserImpersonations.class, Tables.EH_USER_IMPERSONATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_IMPERSONATIONS.ID.max()).from(Tables.EH_USER_IMPERSONATIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMembers.class, Tables.EH_ORGANIZATION_MEMBERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBERS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBERS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatTaskLogs.class, Tables.EH_STAT_TASK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_TASK_LOGS.ID.max()).from(Tables.EH_STAT_TASK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatOrders.class, Tables.EH_STAT_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_ORDERS.ID.max()).from(Tables.EH_STAT_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatRefunds.class, Tables.EH_STAT_REFUNDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_REFUNDS.ID.max()).from(Tables.EH_STAT_REFUNDS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatTransactions.class, Tables.EH_STAT_TRANSACTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_TRANSACTIONS.ID.max()).from(Tables.EH_STAT_TRANSACTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatSettlements.class, Tables.EH_STAT_SETTLEMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_SETTLEMENTS.ID.max()).from(Tables.EH_STAT_SETTLEMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatServiceSettlementResults.class, Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.ID.max()).from(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAlliances.class, Tables.EH_SERVICE_ALLIANCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCES.ID.max()).from(Tables.EH_SERVICE_ALLIANCES).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceCategories.class, Tables.EH_SERVICE_ALLIANCE_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_CATEGORIES.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceAttachments.class, Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceSkipRule.class, Tables.EH_SERVICE_ALLIANCE_SKIP_RULE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_SKIP_RULE.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_SKIP_RULE).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkLogs.class, Tables.EH_ACLINK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_LOGS.ID.max()).from(Tables.EH_ACLINK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkLogs.class, Tables.EH_ACLINK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_LOGS.ID.max()).from(Tables.EH_ACLINK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhAclinkLogs.class, Tables.EH_ACLINK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACLINK_LOGS.ID.max()).from(Tables.EH_ACLINK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhYzbDevices.class, Tables.EH_YZB_DEVICES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_YZB_DEVICES.ID.max()).from(Tables.EH_YZB_DEVICES).fetchOne().value1();
        });
        syncTableSequence(null, EhSearchTypes.class, Tables.EH_SEARCH_TYPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SEARCH_TYPES.ID.max()).from(Tables.EH_SEARCH_TYPES).fetchOne().value1();
        });
        //审批相关表, add by tt, 160907
        syncTableSequence(null, EhApprovalDayActualTime.class, Tables.EH_APPROVAL_DAY_ACTUAL_TIME.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_DAY_ACTUAL_TIME.ID.max()).from(Tables.EH_APPROVAL_DAY_ACTUAL_TIME).fetchOne().value1();
        });

        syncTableSequence(null, EhApprovalFlows.class, Tables.EH_APPROVAL_FLOWS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_FLOWS.ID.max()).from(Tables.EH_APPROVAL_FLOWS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalFlowLevels.class, Tables.EH_APPROVAL_FLOW_LEVELS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_FLOW_LEVELS.ID.max()).from(Tables.EH_APPROVAL_FLOW_LEVELS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRules.class, Tables.EH_APPROVAL_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_RULES.ID.max()).from(Tables.EH_APPROVAL_RULES).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRuleFlowMap.class, Tables.EH_APPROVAL_RULE_FLOW_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_RULE_FLOW_MAP.ID.max()).from(Tables.EH_APPROVAL_RULE_FLOW_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalCategories.class, Tables.EH_APPROVAL_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_CATEGORIES.ID.max()).from(Tables.EH_APPROVAL_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRequests.class, Tables.EH_APPROVAL_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_REQUESTS.ID.max()).from(Tables.EH_APPROVAL_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalAttachments.class, Tables.EH_APPROVAL_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_ATTACHMENTS.ID.max()).from(Tables.EH_APPROVAL_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalTimeRanges.class, Tables.EH_APPROVAL_TIME_RANGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_TIME_RANGES.ID.max()).from(Tables.EH_APPROVAL_TIME_RANGES).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalOpRequests.class, Tables.EH_APPROVAL_OP_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_OP_REQUESTS.ID.max()).from(Tables.EH_APPROVAL_OP_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhApprovalRangeStatistics.class, Tables.EH_APPROVAL_RANGE_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APPROVAL_RANGE_STATISTICS.ID.max()).from(Tables.EH_APPROVAL_RANGE_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhRequestTemplates.class, Tables.EH_REQUEST_TEMPLATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_REQUEST_TEMPLATES.ID.max()).from(Tables.EH_REQUEST_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhRequestTemplatesNamespaceMapping.class, Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING.ID.max()).from(Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING).fetchOne().value1();
        });
        syncTableSequence(null, EhRequestAttachments.class, Tables.EH_REQUEST_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_REQUEST_ATTACHMENTS.ID.max()).from(Tables.EH_REQUEST_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceRequests.class, Tables.EH_SERVICE_ALLIANCE_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceNotifyTargets.class, Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS).fetchOne().value1();
        });
        syncTableSequence(null, EhSettleRequests.class, Tables.EH_SETTLE_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SETTLE_REQUESTS.ID.max()).from(Tables.EH_SETTLE_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTaskTargets.class, Tables.EH_PM_TASK_TARGETS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_TARGETS.ID.max()).from(Tables.EH_PM_TASK_TARGETS).fetchOne().value1();
        });

        syncTableSequence(null, EhStatActiveUsers.class, Tables.EH_STAT_ACTIVE_USERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_ACTIVE_USERS.ID.max()).from(Tables.EH_STAT_ACTIVE_USERS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarningSettings.class, Tables.EH_WARNING_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WARNING_SETTINGS.ID.max()).from(Tables.EH_WARNING_SETTINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhDoorUserPermission.class, Tables.EH_DOOR_USER_PERMISSION.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_USER_PERMISSION.ID.max()).from(Tables.EH_DOOR_USER_PERMISSION).fetchOne().value1();
        });

        syncTableSequence(null, EhGroupSettings.class, Tables.EH_GROUP_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_SETTINGS.ID.max()).from(Tables.EH_GROUP_SETTINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhBroadcasts.class, Tables.EH_BROADCASTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BROADCASTS.ID.max()).from(Tables.EH_BROADCASTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceReservationRequests.class, Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_RESERVATION_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhResourceCategories.class, Tables.EH_RESOURCE_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RESOURCE_CATEGORIES.ID.max()).from(Tables.EH_RESOURCE_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhResourceCategoryAssignments.class, Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS.ID.max()).from(Tables.EH_RESOURCE_CATEGORY_ASSIGNMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationJobPositions.class, Tables.EH_ORGANIZATION_JOB_POSITIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_JOB_POSITIONS.ID.max()).from(Tables.EH_ORGANIZATION_JOB_POSITIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationJobPositionMaps.class, Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS.ID.max()).from(Tables.EH_ORGANIZATION_JOB_POSITION_MAPS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModules.class, Tables.EH_SERVICE_MODULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULES.ID.max()).from(Tables.EH_SERVICE_MODULES).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModulePrivileges.class, Tables.EH_SERVICE_MODULE_PRIVILEGES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_PRIVILEGES.ID.max()).from(Tables.EH_SERVICE_MODULE_PRIVILEGES).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModuleAssignments.class, Tables.EH_SERVICE_MODULE_ASSIGNMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.ID.max()).from(Tables.EH_SERVICE_MODULE_ASSIGNMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionEquipmentStandardMap.class, Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_EQUIPMENT_STANDARD_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTemplates.class, Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionTemplateItemMap.class, Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_TEMPLATE_ITEM_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionItems.class, Tables.EH_EQUIPMENT_INSPECTION_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ITEMS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionItemResults.class, Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_ITEM_RESULTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerAddress.class, Tables.EH_ORGANIZATION_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESSES.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerCars.class, Tables.EH_ORGANIZATION_OWNER_CARS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_CARS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_CARS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerOwnerCar.class, Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_OWNER_CAR).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerAttachments.class, Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerCarAttachments.class, Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerCarAttachments.class, Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_CAR_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerBehaviors.class, Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_BEHAVIORS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerType.class, Tables.EH_ORGANIZATION_OWNER_TYPE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_TYPE.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_TYPE).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingCardCategories.class, Tables.EH_PARKING_CARD_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CARD_CATEGORIES.ID.max()).from(Tables.EH_PARKING_CARD_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlows.class, Tables.EH_FLOWS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOWS.ID.max()).from(Tables.EH_FLOWS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowStats.class, Tables.EH_FLOW_STATS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_STATS.ID.max()).from(Tables.EH_FLOW_STATS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowNodes.class, Tables.EH_FLOW_NODES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_NODES.ID.max()).from(Tables.EH_FLOW_NODES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowButtons.class, Tables.EH_FLOW_BUTTONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_BUTTONS.ID.max()).from(Tables.EH_FLOW_BUTTONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowForms.class, Tables.EH_FLOW_FORMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_FORMS.ID.max()).from(Tables.EH_FLOW_FORMS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowActions.class, Tables.EH_FLOW_ACTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_ACTIONS.ID.max()).from(Tables.EH_FLOW_ACTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowUserSelections.class, Tables.EH_FLOW_USER_SELECTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_USER_SELECTIONS.ID.max()).from(Tables.EH_FLOW_USER_SELECTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowCases.class, Tables.EH_FLOW_CASES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_CASES.ID.max()).from(Tables.EH_FLOW_CASES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowEventLogs.class, Tables.EH_FLOW_EVENT_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_EVENT_LOGS.ID.max()).from(Tables.EH_FLOW_EVENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowVariables.class, Tables.EH_FLOW_VARIABLES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_VARIABLES.ID.max()).from(Tables.EH_FLOW_VARIABLES).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowEvaluates.class, Tables.EH_FLOW_EVALUATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_EVALUATES.ID.max()).from(Tables.EH_FLOW_EVALUATES).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceApartmentRequests.class, Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_APARTMENT_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeters.class, Tables.EH_ENERGY_METERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METERS.ID.max()).from(Tables.EH_ENERGY_METERS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterCategories.class, Tables.EH_ENERGY_METER_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_CATEGORIES.ID.max()).from(Tables.EH_ENERGY_METER_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterFormulas.class, Tables.EH_ENERGY_METER_FORMULAS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_FORMULAS.ID.max()).from(Tables.EH_ENERGY_METER_FORMULAS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyMeterChangeLogs.class, Tables.EH_ENERGY_METER_CHANGE_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_CHANGE_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_CHANGE_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyMeterReadingLogs.class, Tables.EH_ENERGY_METER_READING_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_READING_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_READING_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyDateStatistics.class, Tables.EH_ENERGY_DATE_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_DATE_STATISTICS.ID.max()).from(Tables.EH_ENERGY_DATE_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyCountStatistics.class, Tables.EH_ENERGY_COUNT_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_COUNT_STATISTICS.ID.max()).from(Tables.EH_ENERGY_COUNT_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyYoyStatistics.class, Tables.EH_ENERGY_YOY_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_YOY_STATISTICS.ID.max()).from(Tables.EH_ENERGY_YOY_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnergyMeterSettingLogs.class, Tables.EH_ENERGY_METER_SETTING_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_SETTING_LOGS.ID.max()).from(Tables.EH_ENERGY_METER_SETTING_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowScripts.class, Tables.EH_FLOW_SCRIPTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_SCRIPTS.ID.max()).from(Tables.EH_FLOW_SCRIPTS).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowSubjects.class, Tables.EH_FLOW_SUBJECTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_SUBJECTS.ID.max()).from(Tables.EH_FLOW_SUBJECTS).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowAttachments.class, Tables.EH_FLOW_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_ATTACHMENTS.ID.max()).from(Tables.EH_FLOW_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalAppVersionCumulatives.class, Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES.ID.max()).from(Tables.EH_TERMINAL_APP_VERSION_CUMULATIVES).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalAppVersionActives.class, Tables.EH_TERMINAL_APP_VERSION_ACTIVES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.ID.max()).from(Tables.EH_TERMINAL_APP_VERSION_ACTIVES).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalAppVersionStatistics.class, Tables.EH_TERMINAL_APP_VERSION_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_APP_VERSION_STATISTICS.ID.max()).from(Tables.EH_TERMINAL_APP_VERSION_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalDayStatistics.class, Tables.EH_TERMINAL_DAY_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_DAY_STATISTICS.ID.max()).from(Tables.EH_TERMINAL_DAY_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalHourStatistics.class, Tables.EH_TERMINAL_HOUR_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_HOUR_STATISTICS.ID.max()).from(Tables.EH_TERMINAL_HOUR_STATISTICS).fetchOne().value1();
        });

        syncTableSequence(null, EhTerminalStatisticsTasks.class, Tables.EH_TERMINAL_STATISTICS_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TERMINAL_STATISTICS_TASKS.ID.max()).from(Tables.EH_TERMINAL_STATISTICS_TASKS).fetchOne().value1();
        });

        syncTableSequence(null, EhAppVersion.class, Tables.EH_APP_VERSION.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APP_VERSION.ID.max()).from(Tables.EH_APP_VERSION).fetchOne().value1();
        });


        syncTableSequence(null, EhServiceHotlines.class, Tables.EH_SERVICE_HOTLINES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_HOTLINES.ID.max()).from(Tables.EH_SERVICE_HOTLINES).fetchOne().value1();
        });
        syncTableSequence(null, EhPmTaskTargetStatistics.class, Tables.EH_PM_TASK_TARGET_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_TARGET_STATISTICS.ID.max()).from(Tables.EH_PM_TASK_TARGET_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhContracts.class, Tables.EH_CONTRACTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACTS.ID.max()).from(Tables.EH_CONTRACTS).fetchOne().value1();
        });

        syncTableSequence(null, EhContractBuildingMappings.class, Tables.EH_CONTRACT_BUILDING_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ID.max()).from(Tables.EH_CONTRACT_BUILDING_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhAppNamespaceMappings.class, Tables.EH_APP_NAMESPACE_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_APP_NAMESPACE_MAPPINGS.ID.max()).from(Tables.EH_APP_NAMESPACE_MAPPINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhFlowTimeouts.class, Tables.EH_FLOW_TIMEOUTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_TIMEOUTS.ID.max()).from(Tables.EH_FLOW_TIMEOUTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingCarSeries.class, Tables.EH_PARKING_CAR_SERIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CAR_SERIES.ID.max()).from(Tables.EH_PARKING_CAR_SERIES).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingAttachments.class, Tables.EH_PARKING_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_ATTACHMENTS.ID.max()).from(Tables.EH_PARKING_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingStatistics.class, Tables.EH_PARKING_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_STATISTICS.ID.max()).from(Tables.EH_PARKING_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingFlow.class, Tables.EH_PARKING_FLOW.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_FLOW.ID.max()).from(Tables.EH_PARKING_FLOW).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAddressMappings.class, Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhUserBlacklists.class, Tables.EH_USER_BLACKLISTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_BLACKLISTS.ID.max()).from(Tables.EH_USER_BLACKLISTS).fetchOne().value1();
        });
        syncTableSequence(null, EhActivityAttachments.class, Tables.EH_ACTIVITY_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_ATTACHMENTS.ID.max()).from(Tables.EH_ACTIVITY_ATTACHMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhActivityGoods.class, Tables.EH_ACTIVITY_GOODS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_GOODS.ID.max()).from(Tables.EH_ACTIVITY_GOODS).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowTimeouts.class, Tables.EH_FLOW_TIMEOUTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_TIMEOUTS.ID.max()).from(Tables.EH_FLOW_TIMEOUTS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingClearanceOperators.class, Tables.EH_PARKING_CLEARANCE_OPERATORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CLEARANCE_OPERATORS.ID.max()).from(Tables.EH_PARKING_CLEARANCE_OPERATORS).fetchOne().value1();
        });
        syncTableSequence(null, EhParkingClearanceLogs.class, Tables.EH_PARKING_CLEARANCE_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PARKING_CLEARANCE_LOGS.ID.max()).from(Tables.EH_PARKING_CLEARANCE_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationAddressMappings.class, Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS.ID.max()).from(Tables.EH_ORGANIZATION_ADDRESS_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhLaunchAdvertisements.class, Tables.EH_LAUNCH_ADVERTISEMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LAUNCH_ADVERTISEMENTS.ID.max()).from(Tables.EH_LAUNCH_ADVERTISEMENTS).fetchOne().value1();
        });

        syncTableSequence(null, EhTechparkSyncdataBackup.class, Tables.EH_TECHPARK_SYNCDATA_BACKUP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_TECHPARK_SYNCDATA_BACKUP.ID.max()).from(Tables.EH_TECHPARK_SYNCDATA_BACKUP).fetchOne().value1();
        });

        syncTableSequence(null, EhFlowEvaluateItems.class, Tables.EH_FLOW_EVALUATE_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_FLOW_EVALUATE_ITEMS.ID.max()).from(Tables.EH_FLOW_EVALUATE_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMemberLogs.class, Tables.EH_ORGANIZATION_MEMBER_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_LOGS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceJumpModule.class, Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_JUMP_MODULE).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceInvestRequests.class, Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_INVEST_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhGeneralApprovals.class, Tables.EH_GENERAL_APPROVALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GENERAL_APPROVALS.ID.max()).from(Tables.EH_GENERAL_APPROVALS).fetchOne().value1();
        });
        syncTableSequence(null, EhGeneralForms.class, Tables.EH_GENERAL_FORMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GENERAL_FORMS.ID.max()).from(Tables.EH_GENERAL_FORMS).fetchOne().value1();
        });
        syncTableSequence(null, EhGeneralApprovalVals.class, Tables.EH_GENERAL_APPROVAL_VALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GENERAL_APPROVAL_VALS.ID.max()).from(Tables.EH_GENERAL_APPROVAL_VALS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionStandardSpecificationMap.class, Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_STANDARD_SPECIFICATION_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhBusinessPromotions.class, Tables.EH_BUSINESS_PROMOTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_BUSINESS_PROMOTIONS.ID.max()).from(Tables.EH_BUSINESS_PROMOTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionTaskTemplates.class, Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES.ID.max()).from(Tables.EH_QUALITY_INSPECTION_TASK_TEMPLATES).fetchOne().value1();
        });
        syncTableSequence(null, EhDoorAuthLogs.class, Tables.EH_DOOR_AUTH_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOOR_AUTH_LOGS.ID.max()).from(Tables.EH_DOOR_AUTH_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSpecifications.class, Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SPECIFICATIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhAssetBills.class, Tables.EH_ASSET_BILLS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ASSET_BILLS.ID.max()).from(Tables.EH_ASSET_BILLS).fetchOne().value1();
        });
        syncTableSequence(null, EhAssetBillTemplateFields.class, Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.ID.max()).from(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionCategories.class, Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhEquipmentInspectionStandardGroupMap.class, Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP.ID.max()).from(Tables.EH_EQUIPMENT_INSPECTION_STANDARD_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaires.class, Tables.EH_QUESTIONNAIRES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRES.ID.max()).from(Tables.EH_QUESTIONNAIRES).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireQuestions.class, Tables.EH_QUESTIONNAIRE_QUESTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_QUESTIONS.ID.max()).from(Tables.EH_QUESTIONNAIRE_QUESTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireOptions.class, Tables.EH_QUESTIONNAIRE_OPTIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_OPTIONS.ID.max()).from(Tables.EH_QUESTIONNAIRE_OPTIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhQuestionnaireAnswers.class, Tables.EH_QUESTIONNAIRE_ANSWERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUESTIONNAIRE_ANSWERS.ID.max()).from(Tables.EH_QUESTIONNAIRE_ANSWERS).fetchOne().value1();
        });
        syncTableSequence(null, EhOsObjects.class, Tables.EH_OS_OBJECTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OS_OBJECTS.ID.max()).from(Tables.EH_OS_OBJECTS).fetchOne().value1();
        });
        syncTableSequence(null, EhOsObjectDownloadLogs.class, Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.ID.max()).from(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhDockingMappings.class, Tables.EH_DOCKING_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_DOCKING_MAPPINGS.ID.max()).from(Tables.EH_DOCKING_MAPPINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseIssuers.class, Tables.EH_LEASE_ISSUERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_ISSUERS.ID.max()).from(Tables.EH_LEASE_ISSUERS).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseIssuerAddresses.class, Tables.EH_LEASE_ISSUER_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_ISSUER_ADDRESSES.ID.max()).from(Tables.EH_LEASE_ISSUER_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseConfigs.class, Tables.EH_LEASE_CONFIGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_CONFIGS.ID.max()).from(Tables.EH_LEASE_CONFIGS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceGolfRequests.class, Tables.EH_SERVICE_ALLIANCE_GOLF_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_GOLF_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_GOLF_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceGymRequests.class, Tables.EH_SERVICE_ALLIANCE_GYM_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_GYM_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_GYM_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhServiceAllianceServerRequests.class, Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceAllianceServerRequests.class, Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS.ID.max()).from(Tables.EH_SERVICE_ALLIANCE_SERVER_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhEnterpriseOpRequestBuildings.class, Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.ID.max()).from(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhOauth2Servers.class, Tables.EH_OAUTH2_SERVERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_SERVERS.ID.max()).from(Tables.EH_OAUTH2_SERVERS).fetchOne().value1();
        });
        syncTableSequence(null, EhOauth2ClientTokens.class, Tables.EH_OAUTH2_CLIENT_TOKENS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_OAUTH2_CLIENT_TOKENS.ID.max()).from(Tables.EH_OAUTH2_CLIENT_TOKENS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterPriceConfig.class, Tables.EH_ENERGY_METER_PRICE_CONFIG.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_PRICE_CONFIG.ID.max()).from(Tables.EH_ENERGY_METER_PRICE_CONFIG).fetchOne().value1();
        });
        syncTableSequence(null, EhActivityVideo.class, Tables.EH_ACTIVITY_VIDEO.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_VIDEO.ID.max()).from(Tables.EH_ACTIVITY_VIDEO).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMeterDefaultSettings.class, Tables.EH_ENERGY_METER_DEFAULT_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_METER_DEFAULT_SETTINGS.ID.max()).from(Tables.EH_ENERGY_METER_DEFAULT_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhEnergyMonthStatistics.class, Tables.EH_ENERGY_MONTH_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENERGY_MONTH_STATISTICS.ID.max()).from(Tables.EH_ENERGY_MONTH_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhPreviews.class, Tables.EH_PREVIEWS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PREVIEWS.ID.max()).from(Tables.EH_PREVIEWS).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressCompanies.class, Tables.EH_EXPRESS_COMPANIES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_COMPANIES.ID.max()).from(Tables.EH_EXPRESS_COMPANIES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressUsers.class, Tables.EH_EXPRESS_USERS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_USERS.ID.max()).from(Tables.EH_EXPRESS_USERS).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressAddresses.class, Tables.EH_EXPRESS_ADDRESSES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_ADDRESSES.ID.max()).from(Tables.EH_EXPRESS_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressOrders.class, Tables.EH_EXPRESS_ORDERS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_ORDERS.ID.max()).from(Tables.EH_EXPRESS_ORDERS).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressQueryHistories.class, Tables.EH_EXPRESS_QUERY_HISTORIES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_QUERY_HISTORIES.ID.max()).from(Tables.EH_EXPRESS_QUERY_HISTORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressOrderLogs.class, Tables.EH_EXPRESS_ORDER_LOGS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_EXPRESS_ORDER_LOGS.ID.max()).from(Tables.EH_EXPRESS_ORDER_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhImportFileTasks.class, Tables.EH_IMPORT_FILE_TASKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_IMPORT_FILE_TASKS.ID.max()).from(Tables.EH_IMPORT_FILE_TASKS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTaskHistoryAddresses.class, Tables.EH_PM_TASK_HISTORY_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_HISTORY_ADDRESSES.ID.max()).from(Tables.EH_PM_TASK_HISTORY_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMemberDetails.class, Tables.EH_ORGANIZATION_MEMBER_DETAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_DETAILS).fetchOne().value1();
        });
        syncTableSequence(null, EhUserNotificationSettings.class, Tables.EH_USER_NOTIFICATION_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_NOTIFICATION_SETTINGS.ID.max()).from(Tables.EH_USER_NOTIFICATION_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationMemberEducations.class, Tables.EH_ORGANIZATION_MEMBER_EDUCATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_EDUCATIONS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_EDUCATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberWorkExperiences.class, Tables.EH_ORGANIZATION_MEMBER_WORK_EXPERIENCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_WORK_EXPERIENCES.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_WORK_EXPERIENCES).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberInsurances.class, Tables.EH_ORGANIZATION_MEMBER_INSURANCES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_INSURANCES.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_INSURANCES).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberContracts.class, Tables.EH_ORGANIZATION_MEMBER_CONTRACTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_CONTRACTS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_CONTRACTS).fetchOne().value1();
        });

        syncTableSequence(null, EhGroupMemberLogs.class, Tables.EH_GROUP_MEMBER_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_GROUP_MEMBER_LOGS.ID.max()).from(Tables.EH_GROUP_MEMBER_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhOrganizationMemberProfileLogs.class, Tables.EH_ORGANIZATION_MEMBER_PROFILE_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_MEMBER_PROFILE_LOGS.ID.max()).from(Tables.EH_ORGANIZATION_MEMBER_PROFILE_LOGS).fetchOne().value1();
        });
        //added by Janson
        syncTableSequence(null, EhWebMenus.class, Tables.EH_WEB_MENUS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WEB_MENUS.ID.max()).from(Tables.EH_WEB_MENUS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhServiceModuleAssignmentRelations.class, Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS.ID.max()).from(Tables.EH_SERVICE_MODULE_ASSIGNMENT_RELATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhAuthorizations.class, Tables.EH_AUTHORIZATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_AUTHORIZATIONS.ID.max()).from(Tables.EH_AUTHORIZATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhAuthorizationRelations.class, Tables.EH_AUTHORIZATION_RELATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_AUTHORIZATION_RELATIONS.ID.max()).from(Tables.EH_AUTHORIZATION_RELATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmTaskHistoryAddresses.class, Tables.EH_PM_TASK_HISTORY_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_TASK_HISTORY_ADDRESSES.ID.max()).from(Tables.EH_PM_TASK_HISTORY_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhRosterOrderSettings.class, Tables.EH_ROSTER_ORDER_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ROSTER_ORDER_SETTINGS.ID.max()).from(Tables.EH_ROSTER_ORDER_SETTINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhWarehouses.class, Tables.EH_WAREHOUSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSES.ID.max()).from(Tables.EH_WAREHOUSES).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTalents.class, Tables.EH_TALENTS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENTS.ID.max()).from(Tables.EH_TALENTS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTalentCategories.class, Tables.EH_TALENT_CATEGORIES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_CATEGORIES.ID.max()).from(Tables.EH_TALENT_CATEGORIES).fetchOne().value1();
        });
        
        syncTableSequence(null, EhTalentQueryHistories.class, Tables.EH_TALENT_QUERY_HISTORIES.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_QUERY_HISTORIES.ID.max()).from(Tables.EH_TALENT_QUERY_HISTORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseMaterials.class, Tables.EH_WAREHOUSE_MATERIALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_MATERIALS.ID.max()).from(Tables.EH_WAREHOUSE_MATERIALS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseMaterialCategories.class, Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.ID.max()).from(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseStocks.class, Tables.EH_WAREHOUSE_STOCKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_STOCKS.ID.max()).from(Tables.EH_WAREHOUSE_STOCKS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseStockLogs.class, Tables.EH_WAREHOUSE_STOCK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_STOCK_LOGS.ID.max()).from(Tables.EH_WAREHOUSE_STOCK_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseRequests.class, Tables.EH_WAREHOUSE_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_REQUESTS.ID.max()).from(Tables.EH_WAREHOUSE_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseRequestMaterials.class, Tables.EH_WAREHOUSE_REQUEST_MATERIALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.ID.max()).from(Tables.EH_WAREHOUSE_REQUEST_MATERIALS).fetchOne().value1();
        });

        syncTableSequence(null, EhWarehouseUnits.class, Tables.EH_WAREHOUSE_UNITS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_WAREHOUSE_UNITS.ID.max()).from(Tables.EH_WAREHOUSE_UNITS).fetchOne().value1();

        });

        syncTableSequence(null, EhSmsLogs.class, Tables.EH_SMS_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SMS_LOGS.ID.max()).from(Tables.EH_SMS_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSamples.class, Tables.EH_QUALITY_INSPECTION_SAMPLES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLES.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLES).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleGroupMap.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_GROUP_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleCommunityMap.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_MAP).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleScoreStat.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_SCORE_STAT).fetchOne().value1();
        });
        syncTableSequence(null, EhQualityInspectionSampleCommunitySpecificationStat.class, Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SAMPLE_COMMUNITY_SPECIFICATION_STAT).fetchOne().value1();

        });
        syncTableSequence(null, EhQualityInspectionSpecificationItemResults.class, Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS.ID.max()).from(Tables.EH_QUALITY_INSPECTION_SPECIFICATION_ITEM_RESULTS).fetchOne().value1();

        });
        syncTableSequence(null, EhNewsCommentRule.class, Tables.EH_NEWS_COMMENT_RULE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_NEWS_COMMENT_RULE.ID.max()).from(Tables.EH_NEWS_COMMENT_RULE).fetchOne().value1();

        });
        syncTableSequence(null, EhNewsCommunities.class, Tables.EH_NEWS_COMMUNITIES.getName(), (dbContext) -> {
                    return dbContext.select(Tables.EH_NEWS_COMMUNITIES.ID.max()).from(Tables.EH_NEWS_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhUserOrganizations.class, Tables.EH_USER_ORGANIZATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_ORGANIZATIONS.ID.max()).from(Tables.EH_USER_ORGANIZATIONS).fetchOne().value1();

        });

        syncTableSequence(null, EhRentalv2PriceRules.class, Tables.EH_RENTALV2_PRICE_RULES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_RENTALV2_PRICE_RULES.ID.max()).from(Tables.EH_RENTALV2_PRICE_RULES).fetchOne().value1();
        });

        syncTableSequence(null, EhCommunityApprove.class, Tables.EH_COMMUNITY_APPROVE.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_APPROVE.ID.max()).from(Tables.EH_COMMUNITY_APPROVE).fetchOne().value1();
        });

        syncTableSequence(null, EhCommunityApproveRequests.class, Tables.EH_COMMUNITY_APPROVE_REQUESTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_APPROVE_REQUESTS.ID.max()).from(Tables.EH_COMMUNITY_APPROVE_REQUESTS).fetchOne().value1();
        });
        syncTableSequence(null, EhSmsLogs.class, Tables.EH_USER_IDENTIFIER_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_IDENTIFIER_LOGS.ID.max()).from(Tables.EH_USER_IDENTIFIER_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhSmsLogs.class, Tables.EH_USER_APPEAL_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_APPEAL_LOGS.ID.max()).from(Tables.EH_USER_APPEAL_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhTalentMessageSenders.class, Tables.EH_TALENT_MESSAGE_SENDERS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_MESSAGE_SENDERS.ID.max()).from(Tables.EH_TALENT_MESSAGE_SENDERS).fetchOne().value1();
        });

        syncTableSequence(null, EhTalentRequests.class, Tables.EH_TALENT_REQUESTS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_TALENT_REQUESTS.ID.max()).from(Tables.EH_TALENT_REQUESTS).fetchOne().value1();
        });

        syncTableSequence(null, EhGeneralFormVals.class, Tables.EH_GENERAL_FORM_VALS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_GENERAL_FORM_VALS.ID.max()).from(Tables.EH_GENERAL_FORM_VALS).fetchOne().value1();
        });
        syncTableSequence(null, EhYzxSmsLogs.class, Tables.EH_YZX_SMS_LOGS.getName(), (dbContext) -> {
        	return dbContext.select(Tables.EH_YZX_SMS_LOGS.ID.max()).from(Tables.EH_YZX_SMS_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwnerAddress.class, Tables.EH_ORGANIZATION_OWNER_ADDRESS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ID.max()).from(Tables.EH_ORGANIZATION_OWNER_ADDRESS).fetchOne().value1();
        });
        syncTableSequence(null, EhZjSyncdataBackup.class, Tables.EH_ZJ_SYNCDATA_BACKUP.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ZJ_SYNCDATA_BACKUP.ID.max()).from(Tables.EH_ZJ_SYNCDATA_BACKUP).fetchOne().value1();
        });
        syncTableSequence(null, EhThirdpartConfigurations.class, Tables.EH_THIRDPART_CONFIGURATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_THIRDPART_CONFIGURATIONS.ID.max()).from(Tables.EH_THIRDPART_CONFIGURATIONS).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldGroups.class, Tables.EH_VAR_FIELD_GROUPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_GROUPS.ID.max()).from(Tables.EH_VAR_FIELD_GROUPS).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFields.class, Tables.EH_VAR_FIELDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELDS.ID.max()).from(Tables.EH_VAR_FIELDS).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldItems.class, Tables.EH_VAR_FIELD_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_ITEMS.ID.max()).from(Tables.EH_VAR_FIELD_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldGroupScopes.class, Tables.EH_VAR_FIELD_GROUP_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_GROUP_SCOPES.ID.max()).from(Tables.EH_VAR_FIELD_GROUP_SCOPES).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldScopes.class, Tables.EH_VAR_FIELD_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_SCOPES.ID.max()).from(Tables.EH_VAR_FIELD_SCOPES).fetchOne().value1();
        });
        syncTableSequence(null, EhVarFieldItemScopes.class, Tables.EH_VAR_FIELD_ITEM_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_VAR_FIELD_ITEM_SCOPES.ID.max()).from(Tables.EH_VAR_FIELD_ITEM_SCOPES).fetchOne().value1();
        });
        syncTableSequence(null, EhEnterpriseCustomers.class, Tables.EH_ENTERPRISE_CUSTOMERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ENTERPRISE_CUSTOMERS.ID.max()).from(Tables.EH_ENTERPRISE_CUSTOMERS).fetchOne().value1();
        });
        syncTableSequence(null, EhContractAttachments.class, Tables.EH_CONTRACT_ATTACHMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_ATTACHMENTS.ID.max()).from(Tables.EH_CONTRACT_ATTACHMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhContractChargingItems.class, Tables.EH_CONTRACT_CHARGING_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_CHARGING_ITEMS.ID.max()).from(Tables.EH_CONTRACT_CHARGING_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhContractChargingItemAddresses.class, Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.ID.max()).from(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES).fetchOne().value1();
        });
        syncTableSequence(null, EhContractParams.class, Tables.EH_CONTRACT_PARAMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CONTRACT_PARAMS.ID.max()).from(Tables.EH_CONTRACT_PARAMS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerTalents.class, Tables.EH_CUSTOMER_TALENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_TALENTS.ID.max()).from(Tables.EH_CUSTOMER_TALENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerTrademarks.class, Tables.EH_CUSTOMER_TRADEMARKS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_TRADEMARKS.ID.max()).from(Tables.EH_CUSTOMER_TRADEMARKS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerPatents.class, Tables.EH_CUSTOMER_PATENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_PATENTS.ID.max()).from(Tables.EH_CUSTOMER_PATENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerApplyProjects.class, Tables.EH_CUSTOMER_APPLY_PROJECTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_APPLY_PROJECTS.ID.max()).from(Tables.EH_CUSTOMER_APPLY_PROJECTS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerCommercials.class, Tables.EH_CUSTOMER_COMMERCIALS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_COMMERCIALS.ID.max()).from(Tables.EH_CUSTOMER_COMMERCIALS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerInvestments.class, Tables.EH_CUSTOMER_INVESTMENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_INVESTMENTS.ID.max()).from(Tables.EH_CUSTOMER_INVESTMENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhCustomerEconomicIndicators.class, Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS.ID.max()).from(Tables.EH_CUSTOMER_ECONOMIC_INDICATORS).fetchOne().value1();
        });
        syncTableSequence(null, EhOrganizationOwners.class, Tables.EH_ORGANIZATION_OWNERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ORGANIZATION_OWNERS.ID.max()).from(Tables.EH_ORGANIZATION_OWNERS).fetchOne().value1();
        });

        syncTableSequence(null, EhCommunityMapInfos.class, Tables.EH_COMMUNITY_MAP_INFOS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_MAP_INFOS.ID.max()).from(Tables.EH_COMMUNITY_MAP_INFOS).fetchOne().value1();
        });
        syncTableSequence(null, EhCommunityMapSearchTypes.class, Tables.EH_COMMUNITY_MAP_SEARCH_TYPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_MAP_SEARCH_TYPES.ID.max()).from(Tables.EH_COMMUNITY_MAP_SEARCH_TYPES).fetchOne().value1();
        });
        syncTableSequence(null, EhCommunityBuildingGeos.class, Tables.EH_COMMUNITY_BUILDING_GEOS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_COMMUNITY_BUILDING_GEOS.ID.max()).from(Tables.EH_COMMUNITY_BUILDING_GEOS).fetchOne().value1();
        });
        syncTableSequence(null, EhLeasePromotionCommunities.class, Tables.EH_LEASE_PROMOTION_COMMUNITIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LEASE_PROMOTION_COMMUNITIES.ID.max()).from(Tables.EH_LEASE_PROMOTION_COMMUNITIES).fetchOne().value1();
        });
        syncTableSequence(null, EhLeaseBuildings.class, Tables.EH_LEASE_BUILDINGS.getName(), (dbContext) -> {
                    return dbContext.select(Tables.EH_LEASE_BUILDINGS.ID.max()).from(Tables.EH_LEASE_BUILDINGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventDeviceLogs.class, Tables.EH_STAT_EVENT_DEVICE_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_DEVICE_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_DEVICE_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventUploadStrategies.class, Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES.ID.max()).from(Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEvents.class, Tables.EH_STAT_EVENTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENTS.ID.max()).from(Tables.EH_STAT_EVENTS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventParams.class, Tables.EH_STAT_EVENT_PARAMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_PARAMS.ID.max()).from(Tables.EH_STAT_EVENT_PARAMS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventContentLogs.class, Tables.EH_STAT_EVENT_CONTENT_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_CONTENT_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_CONTENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventLogs.class, Tables.EH_STAT_EVENT_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventParamLogs.class, Tables.EH_STAT_EVENT_PARAM_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_PARAM_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_PARAM_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventAppAttachmentLogs.class, Tables.EH_STAT_EVENT_APP_ATTACHMENT_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_APP_ATTACHMENT_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_APP_ATTACHMENT_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventPortalConfigs.class, Tables.EH_STAT_EVENT_PORTAL_CONFIGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_PORTAL_CONFIGS.ID.max()).from(Tables.EH_STAT_EVENT_PORTAL_CONFIGS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventPortalStatistics.class, Tables.EH_STAT_EVENT_PORTAL_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID.max()).from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventStatistics.class, Tables.EH_STAT_EVENT_STATISTICS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_STATISTICS.ID.max()).from(Tables.EH_STAT_EVENT_STATISTICS).fetchOne().value1();
        });
        syncTableSequence(null, EhStatEventTaskLogs.class, Tables.EH_STAT_EVENT_TASK_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_STAT_EVENT_TASK_LOGS.ID.max()).from(Tables.EH_STAT_EVENT_TASK_LOGS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalItemGroups.class, Tables.EH_PORTAL_ITEM_GROUPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEM_GROUPS.ID.max()).from(Tables.EH_PORTAL_ITEM_GROUPS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalItems.class, Tables.EH_PORTAL_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEMS.ID.max()).from(Tables.EH_PORTAL_ITEMS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalNavigationBars.class, Tables.EH_PORTAL_NAVIGATION_BARS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_NAVIGATION_BARS.ID.max()).from(Tables.EH_PORTAL_NAVIGATION_BARS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalLayouts.class, Tables.EH_PORTAL_LAYOUTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAYOUTS.ID.max()).from(Tables.EH_PORTAL_LAYOUTS).fetchOne().value1();
        });
        syncTableSequence(null, EhPortalLaunchPadMappings.class, Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.ID.max()).from(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalLayouts.class, Tables.EH_PORTAL_LAYOUTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAYOUTS.ID.max()).from(Tables.EH_PORTAL_LAYOUTS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalItems.class, Tables.EH_PORTAL_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEMS.ID.max()).from(Tables.EH_PORTAL_ITEMS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalItemGroups.class, Tables.EH_PORTAL_ITEM_GROUPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEM_GROUPS.ID.max()).from(Tables.EH_PORTAL_ITEM_GROUPS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalItemCategories.class, Tables.EH_PORTAL_ITEM_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_ITEM_CATEGORIES.ID.max()).from(Tables.EH_PORTAL_ITEM_CATEGORIES).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalContentScopes.class, Tables.EH_PORTAL_CONTENT_SCOPES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_CONTENT_SCOPES.ID.max()).from(Tables.EH_PORTAL_CONTENT_SCOPES).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalLayoutTemplates.class, Tables.EH_PORTAL_LAYOUT_TEMPLATES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAYOUT_TEMPLATES.ID.max()).from(Tables.EH_PORTAL_LAYOUT_TEMPLATES).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalNavigationBars.class, Tables.EH_PORTAL_NAVIGATION_BARS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_NAVIGATION_BARS.ID.max()).from(Tables.EH_PORTAL_NAVIGATION_BARS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalLaunchPadMappings.class, Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS.ID.max()).from(Tables.EH_PORTAL_LAUNCH_PAD_MAPPINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhPortalPublishLogs.class, Tables.EH_PORTAL_PUBLISH_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PORTAL_PUBLISH_LOGS.ID.max()).from(Tables.EH_PORTAL_PUBLISH_LOGS).fetchOne().value1();
        });

        syncTableSequence(null, EhServiceModuleApps.class, Tables.EH_SERVICE_MODULE_APPS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SERVICE_MODULE_APPS.ID.max()).from(Tables.EH_SERVICE_MODULE_APPS).fetchOne().value1();
        });

        syncTableSequence(null, EhItemServiceCategries.class, Tables.EH_ITEM_SERVICE_CATEGRIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ITEM_SERVICE_CATEGRIES.ID.max()).from(Tables.EH_ITEM_SERVICE_CATEGRIES).fetchOne().value1();
        });

        syncTableSequence(null, EhLaunchPadItems.class, Tables.EH_LAUNCH_PAD_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LAUNCH_PAD_ITEMS.ID.max()).from(Tables.EH_LAUNCH_PAD_ITEMS).fetchOne().value1();
        });

        syncTableSequence(null, EhLaunchPadLayouts.class, Tables.EH_LAUNCH_PAD_LAYOUTS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_LAUNCH_PAD_LAYOUTS.ID.max()).from(Tables.EH_LAUNCH_PAD_LAYOUTS).fetchOne().value1();
        });

        syncTableSequence(null, EhUserLaunchPadItems.class, Tables.EH_USER_LAUNCH_PAD_ITEMS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_USER_LAUNCH_PAD_ITEMS.ID.max()).from(Tables.EH_USER_LAUNCH_PAD_ITEMS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintEmails.class, Tables.EH_SIYIN_PRINT_EMAILS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_EMAILS.ID.max()).from(Tables.EH_SIYIN_PRINT_EMAILS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintOrders.class, Tables.EH_SIYIN_PRINT_ORDERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_ORDERS.ID.max()).from(Tables.EH_SIYIN_PRINT_ORDERS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintPrinters.class, Tables.EH_SIYIN_PRINT_PRINTERS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_PRINTERS.ID.max()).from(Tables.EH_SIYIN_PRINT_PRINTERS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintRecords.class, Tables.EH_SIYIN_PRINT_RECORDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_RECORDS.ID.max()).from(Tables.EH_SIYIN_PRINT_RECORDS).fetchOne().value1();
        });

        syncTableSequence(null, EhSiyinPrintSettings.class, Tables.EH_SIYIN_PRINT_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_SIYIN_PRINT_SETTINGS.ID.max()).from(Tables.EH_SIYIN_PRINT_SETTINGS).fetchOne().value1();
        });

        syncTableSequence(null, EhActivityCategories.class, Tables.EH_ACTIVITY_CATEGORIES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_ACTIVITY_CATEGORIES.ID.max()).from(Tables.EH_ACTIVITY_CATEGORIES).fetchOne().value1();
        });
        syncTableSequence(null, EhExpressParamSettings.class, Tables.EH_EXPRESS_PARAM_SETTINGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EXPRESS_PARAM_SETTINGS.ID.max()).from(Tables.EH_EXPRESS_PARAM_SETTINGS).fetchOne().value1();
        });
        
        syncTableSequence(null, EhExpressCompanyBusinesses.class, Tables.EH_EXPRESS_COMPANY_BUSINESSES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EXPRESS_COMPANY_BUSINESSES.ID.max()).from(Tables.EH_EXPRESS_COMPANY_BUSINESSES).fetchOne().value1();
        });
        
        syncTableSequence(null, EhExpressHotlines.class, Tables.EH_EXPRESS_HOTLINES.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_EXPRESS_HOTLINES.ID.max()).from(Tables.EH_EXPRESS_HOTLINES).fetchOne().value1();
        });

        syncTableSequence(null, EhPmNotifyConfigurations.class, Tables.EH_PM_NOTIFY_CONFIGURATIONS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_NOTIFY_CONFIGURATIONS.ID.max()).from(Tables.EH_PM_NOTIFY_CONFIGURATIONS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmNotifyRecords.class, Tables.EH_PM_NOTIFY_RECORDS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_NOTIFY_RECORDS.ID.max()).from(Tables.EH_PM_NOTIFY_RECORDS).fetchOne().value1();
        });

        syncTableSequence(null, EhPmNotifyLogs.class, Tables.EH_PM_NOTIFY_LOGS.getName(), (dbContext) -> {
            return dbContext.select(Tables.EH_PM_NOTIFY_LOGS.ID.max()).from(Tables.EH_PM_NOTIFY_LOGS).fetchOne().value1();
        });
    }

    @SuppressWarnings("rawtypes")
    private void syncTableSequence(Class keytableCls, Class pojoClass, String tableName, SequenceQueryCallback callback) {
        AccessSpec spec = null;
        if(keytableCls == null) {
            spec = AccessSpec.readOnly();
        } else {
            spec = AccessSpec.readOnlyWith(keytableCls);
        }

        Long result[] = new Long[1];
        result[0] = 0L;
        dbProvider.mapReduce(spec, null, (dbContext, contextObj) -> {
            //Long max = dbContext.select(Tables.EH_USERS.ID.max()).from(Tables.EH_USERS).fetchOne().value1();
            Long max = callback.maxSequence(dbContext);

            if(max != null && max.longValue() > result[0].longValue()) {
                result[0] = max;
            }
            return true;
        });
        String key = NameMapper.getSequenceDomainFromTablePojo(pojoClass);
        long nextSequenceBeforeReset = sequenceProvider.getNextSequence(key);
        sequenceProvider.resetSequence(key, result[0].longValue() + 1);
        long nextSequenceAfterReset = sequenceProvider.getNextSequence(key);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sync table sequence, tableName=" + tableName + ", key=" + key + ", newSequence=" + (result[0].longValue() + 1)
                + ", nextSequenceBeforeReset=" + nextSequenceBeforeReset + ", nextSequenceAfterReset=" + nextSequenceAfterReset);
        }
    }

    private void syncUserAccountName() {
        int pageCount = 1000;
        int pageNum = 1;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<User> userList = null;
        long maxAccountSequence = 0;
        long temp = 0;
        long startTime = 0;
        long endTime = 0;
        do {
            startTime = System.currentTimeMillis();
            userList = userProvider.queryUsers(locator, pageCount, null);
            for(User user : userList) {
                try {
                    temp = Long.parseLong(user.getAccountName());
                    if(temp > maxAccountSequence) {
                        maxAccountSequence = temp;
                    }
                } catch(Exception e) {
                    // no need to log and do nothing
                }
            }
            endTime = System.currentTimeMillis();
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Sync user account name sequence, pageNum=" + pageNum + ", pageCount=" + pageCount
                    + ", dbSize=" + userList.size() + ", elapse=" + (endTime - startTime));
            }
            pageNum++;
        } while(userList != null && userList.size() > 0);

        if(maxAccountSequence > 0) {
            String key = "usr";
            long nextSequenceBeforeReset = sequenceProvider.getNextSequence(key);
            sequenceProvider.resetSequence(key, maxAccountSequence + 1);
            long nextSequenceAfterReset = sequenceProvider.getNextSequence(key);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Sync user account name sequence, key=" + key + ", newSequence=" + (maxAccountSequence + 1)
                    + ", nextSequenceBeforeReset=" + nextSequenceBeforeReset + ", nextSequenceAfterReset=" + nextSequenceAfterReset);
            }
        }
    }

    @Override
    public GetSequenceDTO getSequence(GetSequenceCommand cmd) {
        long startSequence = 0L;
        
        long blockSize = (cmd.getBlockSize() == null ? 0L : cmd.getBlockSize());
        if(blockSize <= 1) {
            startSequence = sequenceProvider.getNextSequence(cmd.getSequenceDomain());
            blockSize = 1;
        } else {
            startSequence = sequenceProvider.getNextSequenceBlock(cmd.getSequenceDomain(), blockSize);
        }
        
        GetSequenceDTO dto = new GetSequenceDTO();
        dto.setSequenceDomain(cmd.getSequenceDomain());
        dto.setStartSequence(startSequence);
        dto.setBlockSize(blockSize);
        
        return dto;
    }
}
