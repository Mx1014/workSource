//
// EvhPunchStatisticsDTO.m
//
#import "EvhPunchStatisticsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchStatisticsDTO
//

@implementation EvhPunchStatisticsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchStatisticsDTO* obj = [EvhPunchStatisticsDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.userEnterpriseGroup)
        [jsonObject setObject: self.userEnterpriseGroup forKey: @"userEnterpriseGroup"];
    if(self.userDepartment)
        [jsonObject setObject: self.userDepartment forKey: @"userDepartment"];
    if(self.userPhoneNumber)
        [jsonObject setObject: self.userPhoneNumber forKey: @"userPhoneNumber"];
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.punchDate)
        [jsonObject setObject: self.punchDate forKey: @"punchDate"];
    if(self.arriveTime)
        [jsonObject setObject: self.arriveTime forKey: @"arriveTime"];
    if(self.noonLeaveTime)
        [jsonObject setObject: self.noonLeaveTime forKey: @"noonLeaveTime"];
    if(self.afternoonArriveTime)
        [jsonObject setObject: self.afternoonArriveTime forKey: @"afternoonArriveTime"];
    if(self.leaveTime)
        [jsonObject setObject: self.leaveTime forKey: @"leaveTime"];
    if(self.workTime)
        [jsonObject setObject: self.workTime forKey: @"workTime"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.morningStatus)
        [jsonObject setObject: self.morningStatus forKey: @"morningStatus"];
    if(self.afternoonStatus)
        [jsonObject setObject: self.afternoonStatus forKey: @"afternoonStatus"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.operatorName)
        [jsonObject setObject: self.operatorName forKey: @"operatorName"];
    if(self.approvalStatus)
        [jsonObject setObject: self.approvalStatus forKey: @"approvalStatus"];
    if(self.morningApprovalStatus)
        [jsonObject setObject: self.morningApprovalStatus forKey: @"morningApprovalStatus"];
    if(self.afternoonApprovalStatus)
        [jsonObject setObject: self.afternoonApprovalStatus forKey: @"afternoonApprovalStatus"];
    if(self.viewFlag)
        [jsonObject setObject: self.viewFlag forKey: @"viewFlag"];
    if(self.punchTimesPerDay)
        [jsonObject setObject: self.punchTimesPerDay forKey: @"punchTimesPerDay"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.userEnterpriseGroup = [jsonObject objectForKey: @"userEnterpriseGroup"];
        if(self.userEnterpriseGroup && [self.userEnterpriseGroup isEqual:[NSNull null]])
            self.userEnterpriseGroup = nil;

        self.userDepartment = [jsonObject objectForKey: @"userDepartment"];
        if(self.userDepartment && [self.userDepartment isEqual:[NSNull null]])
            self.userDepartment = nil;

        self.userPhoneNumber = [jsonObject objectForKey: @"userPhoneNumber"];
        if(self.userPhoneNumber && [self.userPhoneNumber isEqual:[NSNull null]])
            self.userPhoneNumber = nil;

        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.punchDate = [jsonObject objectForKey: @"punchDate"];
        if(self.punchDate && [self.punchDate isEqual:[NSNull null]])
            self.punchDate = nil;

        self.arriveTime = [jsonObject objectForKey: @"arriveTime"];
        if(self.arriveTime && [self.arriveTime isEqual:[NSNull null]])
            self.arriveTime = nil;

        self.noonLeaveTime = [jsonObject objectForKey: @"noonLeaveTime"];
        if(self.noonLeaveTime && [self.noonLeaveTime isEqual:[NSNull null]])
            self.noonLeaveTime = nil;

        self.afternoonArriveTime = [jsonObject objectForKey: @"afternoonArriveTime"];
        if(self.afternoonArriveTime && [self.afternoonArriveTime isEqual:[NSNull null]])
            self.afternoonArriveTime = nil;

        self.leaveTime = [jsonObject objectForKey: @"leaveTime"];
        if(self.leaveTime && [self.leaveTime isEqual:[NSNull null]])
            self.leaveTime = nil;

        self.workTime = [jsonObject objectForKey: @"workTime"];
        if(self.workTime && [self.workTime isEqual:[NSNull null]])
            self.workTime = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.morningStatus = [jsonObject objectForKey: @"morningStatus"];
        if(self.morningStatus && [self.morningStatus isEqual:[NSNull null]])
            self.morningStatus = nil;

        self.afternoonStatus = [jsonObject objectForKey: @"afternoonStatus"];
        if(self.afternoonStatus && [self.afternoonStatus isEqual:[NSNull null]])
            self.afternoonStatus = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.operatorName = [jsonObject objectForKey: @"operatorName"];
        if(self.operatorName && [self.operatorName isEqual:[NSNull null]])
            self.operatorName = nil;

        self.approvalStatus = [jsonObject objectForKey: @"approvalStatus"];
        if(self.approvalStatus && [self.approvalStatus isEqual:[NSNull null]])
            self.approvalStatus = nil;

        self.morningApprovalStatus = [jsonObject objectForKey: @"morningApprovalStatus"];
        if(self.morningApprovalStatus && [self.morningApprovalStatus isEqual:[NSNull null]])
            self.morningApprovalStatus = nil;

        self.afternoonApprovalStatus = [jsonObject objectForKey: @"afternoonApprovalStatus"];
        if(self.afternoonApprovalStatus && [self.afternoonApprovalStatus isEqual:[NSNull null]])
            self.afternoonApprovalStatus = nil;

        self.viewFlag = [jsonObject objectForKey: @"viewFlag"];
        if(self.viewFlag && [self.viewFlag isEqual:[NSNull null]])
            self.viewFlag = nil;

        self.punchTimesPerDay = [jsonObject objectForKey: @"punchTimesPerDay"];
        if(self.punchTimesPerDay && [self.punchTimesPerDay isEqual:[NSNull null]])
            self.punchTimesPerDay = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
