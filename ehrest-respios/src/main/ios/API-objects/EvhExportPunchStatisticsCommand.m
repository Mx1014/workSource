//
// EvhExportPunchStatisticsCommand.m
//
#import "EvhExportPunchStatisticsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExportPunchStatisticsCommand
//

@implementation EvhExportPunchStatisticsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhExportPunchStatisticsCommand* obj = [EvhExportPunchStatisticsCommand new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
    if(self.startDay)
        [jsonObject setObject: self.startDay forKey: @"startDay"];
    if(self.endDay)
        [jsonObject setObject: self.endDay forKey: @"endDay"];
    if(self.arriveTimeCompareFlag)
        [jsonObject setObject: self.arriveTimeCompareFlag forKey: @"arriveTimeCompareFlag"];
    if(self.arriveTime)
        [jsonObject setObject: self.arriveTime forKey: @"arriveTime"];
    if(self.leaveTimeCompareFlag)
        [jsonObject setObject: self.leaveTimeCompareFlag forKey: @"leaveTimeCompareFlag"];
    if(self.leaveTime)
        [jsonObject setObject: self.leaveTime forKey: @"leaveTime"];
    if(self.workTimeCompareFlag)
        [jsonObject setObject: self.workTimeCompareFlag forKey: @"workTimeCompareFlag"];
    if(self.workTime)
        [jsonObject setObject: self.workTime forKey: @"workTime"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        self.startDay = [jsonObject objectForKey: @"startDay"];
        if(self.startDay && [self.startDay isEqual:[NSNull null]])
            self.startDay = nil;

        self.endDay = [jsonObject objectForKey: @"endDay"];
        if(self.endDay && [self.endDay isEqual:[NSNull null]])
            self.endDay = nil;

        self.arriveTimeCompareFlag = [jsonObject objectForKey: @"arriveTimeCompareFlag"];
        if(self.arriveTimeCompareFlag && [self.arriveTimeCompareFlag isEqual:[NSNull null]])
            self.arriveTimeCompareFlag = nil;

        self.arriveTime = [jsonObject objectForKey: @"arriveTime"];
        if(self.arriveTime && [self.arriveTime isEqual:[NSNull null]])
            self.arriveTime = nil;

        self.leaveTimeCompareFlag = [jsonObject objectForKey: @"leaveTimeCompareFlag"];
        if(self.leaveTimeCompareFlag && [self.leaveTimeCompareFlag isEqual:[NSNull null]])
            self.leaveTimeCompareFlag = nil;

        self.leaveTime = [jsonObject objectForKey: @"leaveTime"];
        if(self.leaveTime && [self.leaveTime isEqual:[NSNull null]])
            self.leaveTime = nil;

        self.workTimeCompareFlag = [jsonObject objectForKey: @"workTimeCompareFlag"];
        if(self.workTimeCompareFlag && [self.workTimeCompareFlag isEqual:[NSNull null]])
            self.workTimeCompareFlag = nil;

        self.workTime = [jsonObject objectForKey: @"workTime"];
        if(self.workTime && [self.workTime isEqual:[NSNull null]])
            self.workTime = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
