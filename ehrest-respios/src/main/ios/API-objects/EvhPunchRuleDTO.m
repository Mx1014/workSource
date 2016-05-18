//
// EvhPunchRuleDTO.m
//
#import "EvhPunchRuleDTO.h"
#import "EvhPunchGeoPointDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchRuleDTO
//

@implementation EvhPunchRuleDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchRuleDTO* obj = [EvhPunchRuleDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _punchGeoPoints = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.EnterpriseId)
        [jsonObject setObject: self.EnterpriseId forKey: @"EnterpriseId"];
    if(self.startEarlyTime)
        [jsonObject setObject: self.startEarlyTime forKey: @"startEarlyTime"];
    if(self.startLateTime)
        [jsonObject setObject: self.startLateTime forKey: @"startLateTime"];
    if(self.endEarlyTime)
        [jsonObject setObject: self.endEarlyTime forKey: @"endEarlyTime"];
    if(self.noonLeaveTime)
        [jsonObject setObject: self.noonLeaveTime forKey: @"noonLeaveTime"];
    if(self.afternoonArriveTime)
        [jsonObject setObject: self.afternoonArriveTime forKey: @"afternoonArriveTime"];
    if(self.punchTimesPerDay)
        [jsonObject setObject: self.punchTimesPerDay forKey: @"punchTimesPerDay"];
    if(self.punchGeoPoints) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPunchGeoPointDTO* item in self.punchGeoPoints) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"punchGeoPoints"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.EnterpriseId = [jsonObject objectForKey: @"EnterpriseId"];
        if(self.EnterpriseId && [self.EnterpriseId isEqual:[NSNull null]])
            self.EnterpriseId = nil;

        self.startEarlyTime = [jsonObject objectForKey: @"startEarlyTime"];
        if(self.startEarlyTime && [self.startEarlyTime isEqual:[NSNull null]])
            self.startEarlyTime = nil;

        self.startLateTime = [jsonObject objectForKey: @"startLateTime"];
        if(self.startLateTime && [self.startLateTime isEqual:[NSNull null]])
            self.startLateTime = nil;

        self.endEarlyTime = [jsonObject objectForKey: @"endEarlyTime"];
        if(self.endEarlyTime && [self.endEarlyTime isEqual:[NSNull null]])
            self.endEarlyTime = nil;

        self.noonLeaveTime = [jsonObject objectForKey: @"noonLeaveTime"];
        if(self.noonLeaveTime && [self.noonLeaveTime isEqual:[NSNull null]])
            self.noonLeaveTime = nil;

        self.afternoonArriveTime = [jsonObject objectForKey: @"afternoonArriveTime"];
        if(self.afternoonArriveTime && [self.afternoonArriveTime isEqual:[NSNull null]])
            self.afternoonArriveTime = nil;

        self.punchTimesPerDay = [jsonObject objectForKey: @"punchTimesPerDay"];
        if(self.punchTimesPerDay && [self.punchTimesPerDay isEqual:[NSNull null]])
            self.punchTimesPerDay = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"punchGeoPoints"];
            for(id itemJson in jsonArray) {
                EvhPunchGeoPointDTO* item = [EvhPunchGeoPointDTO new];
                
                [item fromJson: itemJson];
                [self.punchGeoPoints addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
