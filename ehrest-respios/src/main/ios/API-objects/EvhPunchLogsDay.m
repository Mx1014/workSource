//
// EvhPunchLogsDay.m
//
#import "EvhPunchLogsDay.h"
#import "EvhPunchLogDTO.h"
#import "EvhPunchExceptionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchLogsDay
//

@implementation EvhPunchLogsDay

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchLogsDay* obj = [EvhPunchLogsDay new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _punchLogs = [NSMutableArray new];
        _punchExceptionDTOs = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.punchDay)
        [jsonObject setObject: self.punchDay forKey: @"punchDay"];
    if(self.punchStatus)
        [jsonObject setObject: self.punchStatus forKey: @"punchStatus"];
    if(self.morningPunchStatus)
        [jsonObject setObject: self.morningPunchStatus forKey: @"morningPunchStatus"];
    if(self.afternoonPunchStatus)
        [jsonObject setObject: self.afternoonPunchStatus forKey: @"afternoonPunchStatus"];
    if(self.approvalStatus)
        [jsonObject setObject: self.approvalStatus forKey: @"approvalStatus"];
    if(self.morningApprovalStatus)
        [jsonObject setObject: self.morningApprovalStatus forKey: @"morningApprovalStatus"];
    if(self.afternoonApprovalStatus)
        [jsonObject setObject: self.afternoonApprovalStatus forKey: @"afternoonApprovalStatus"];
    if(self.exceptionStatus)
        [jsonObject setObject: self.exceptionStatus forKey: @"exceptionStatus"];
    if(self.punchTimesPerDay)
        [jsonObject setObject: self.punchTimesPerDay forKey: @"punchTimesPerDay"];
    if(self.punchLogs) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPunchLogDTO* item in self.punchLogs) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"punchLogs"];
    }
    if(self.punchExceptionDTOs) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPunchExceptionDTO* item in self.punchExceptionDTOs) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"punchExceptionDTOs"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.punchDay = [jsonObject objectForKey: @"punchDay"];
        if(self.punchDay && [self.punchDay isEqual:[NSNull null]])
            self.punchDay = nil;

        self.punchStatus = [jsonObject objectForKey: @"punchStatus"];
        if(self.punchStatus && [self.punchStatus isEqual:[NSNull null]])
            self.punchStatus = nil;

        self.morningPunchStatus = [jsonObject objectForKey: @"morningPunchStatus"];
        if(self.morningPunchStatus && [self.morningPunchStatus isEqual:[NSNull null]])
            self.morningPunchStatus = nil;

        self.afternoonPunchStatus = [jsonObject objectForKey: @"afternoonPunchStatus"];
        if(self.afternoonPunchStatus && [self.afternoonPunchStatus isEqual:[NSNull null]])
            self.afternoonPunchStatus = nil;

        self.approvalStatus = [jsonObject objectForKey: @"approvalStatus"];
        if(self.approvalStatus && [self.approvalStatus isEqual:[NSNull null]])
            self.approvalStatus = nil;

        self.morningApprovalStatus = [jsonObject objectForKey: @"morningApprovalStatus"];
        if(self.morningApprovalStatus && [self.morningApprovalStatus isEqual:[NSNull null]])
            self.morningApprovalStatus = nil;

        self.afternoonApprovalStatus = [jsonObject objectForKey: @"afternoonApprovalStatus"];
        if(self.afternoonApprovalStatus && [self.afternoonApprovalStatus isEqual:[NSNull null]])
            self.afternoonApprovalStatus = nil;

        self.exceptionStatus = [jsonObject objectForKey: @"exceptionStatus"];
        if(self.exceptionStatus && [self.exceptionStatus isEqual:[NSNull null]])
            self.exceptionStatus = nil;

        self.punchTimesPerDay = [jsonObject objectForKey: @"punchTimesPerDay"];
        if(self.punchTimesPerDay && [self.punchTimesPerDay isEqual:[NSNull null]])
            self.punchTimesPerDay = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"punchLogs"];
            for(id itemJson in jsonArray) {
                EvhPunchLogDTO* item = [EvhPunchLogDTO new];
                
                [item fromJson: itemJson];
                [self.punchLogs addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"punchExceptionDTOs"];
            for(id itemJson in jsonArray) {
                EvhPunchExceptionDTO* item = [EvhPunchExceptionDTO new];
                
                [item fromJson: itemJson];
                [self.punchExceptionDTOs addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
