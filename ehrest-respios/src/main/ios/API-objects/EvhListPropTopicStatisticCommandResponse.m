//
// EvhListPropTopicStatisticCommandResponse.m
//
#import "EvhListPropTopicStatisticCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropTopicStatisticCommandResponse
//

@implementation EvhListPropTopicStatisticCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPropTopicStatisticCommandResponse* obj = [EvhListPropTopicStatisticCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _todayList = [NSMutableArray new];
        _yesterdayList = [NSMutableArray new];
        _weekList = [NSMutableArray new];
        _monthList = [NSMutableArray new];
        _dateList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.todayList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.todayList) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"todayList"];
    }
    if(self.yesterdayList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.yesterdayList) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"yesterdayList"];
    }
    if(self.weekList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.weekList) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"weekList"];
    }
    if(self.monthList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.monthList) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"monthList"];
    }
    if(self.dateList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.dateList) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"dateList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"todayList"];
            for(id itemJson in jsonArray) {
                [self.todayList addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"yesterdayList"];
            for(id itemJson in jsonArray) {
                [self.yesterdayList addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"weekList"];
            for(id itemJson in jsonArray) {
                [self.weekList addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"monthList"];
            for(id itemJson in jsonArray) {
                [self.monthList addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"dateList"];
            for(id itemJson in jsonArray) {
                [self.dateList addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
