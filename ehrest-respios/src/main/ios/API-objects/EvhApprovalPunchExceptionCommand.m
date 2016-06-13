//
// EvhApprovalPunchExceptionCommand.m
//
#import "EvhApprovalPunchExceptionCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApprovalPunchExceptionCommand
//

@implementation EvhApprovalPunchExceptionCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhApprovalPunchExceptionCommand* obj = [EvhApprovalPunchExceptionCommand new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.punchDate)
        [jsonObject setObject: self.punchDate forKey: @"punchDate"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.morningApprovalStatus)
        [jsonObject setObject: self.morningApprovalStatus forKey: @"morningApprovalStatus"];
    if(self.afternoonApprovalStatus)
        [jsonObject setObject: self.afternoonApprovalStatus forKey: @"afternoonApprovalStatus"];
    if(self.approvalStatus)
        [jsonObject setObject: self.approvalStatus forKey: @"approvalStatus"];
    if(self.processDetails)
        [jsonObject setObject: self.processDetails forKey: @"processDetails"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.operatorUid)
        [jsonObject setObject: self.operatorUid forKey: @"operatorUid"];
    if(self.punchTimesPerDay)
        [jsonObject setObject: self.punchTimesPerDay forKey: @"punchTimesPerDay"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.punchDate = [jsonObject objectForKey: @"punchDate"];
        if(self.punchDate && [self.punchDate isEqual:[NSNull null]])
            self.punchDate = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.morningApprovalStatus = [jsonObject objectForKey: @"morningApprovalStatus"];
        if(self.morningApprovalStatus && [self.morningApprovalStatus isEqual:[NSNull null]])
            self.morningApprovalStatus = nil;

        self.afternoonApprovalStatus = [jsonObject objectForKey: @"afternoonApprovalStatus"];
        if(self.afternoonApprovalStatus && [self.afternoonApprovalStatus isEqual:[NSNull null]])
            self.afternoonApprovalStatus = nil;

        self.approvalStatus = [jsonObject objectForKey: @"approvalStatus"];
        if(self.approvalStatus && [self.approvalStatus isEqual:[NSNull null]])
            self.approvalStatus = nil;

        self.processDetails = [jsonObject objectForKey: @"processDetails"];
        if(self.processDetails && [self.processDetails isEqual:[NSNull null]])
            self.processDetails = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.operatorUid = [jsonObject objectForKey: @"operatorUid"];
        if(self.operatorUid && [self.operatorUid isEqual:[NSNull null]])
            self.operatorUid = nil;

        self.punchTimesPerDay = [jsonObject objectForKey: @"punchTimesPerDay"];
        if(self.punchTimesPerDay && [self.punchTimesPerDay isEqual:[NSNull null]])
            self.punchTimesPerDay = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
