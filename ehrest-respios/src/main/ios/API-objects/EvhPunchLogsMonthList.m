//
// EvhPunchLogsMonthList.m
//
#import "EvhPunchLogsMonthList.h"
#import "EvhPunchLogsDay.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchLogsMonthList
//

@implementation EvhPunchLogsMonthList

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchLogsMonthList* obj = [EvhPunchLogsMonthList new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _punchLogsDayList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.punchMonth)
        [jsonObject setObject: self.punchMonth forKey: @"punchMonth"];
    if(self.punchLogsDayList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPunchLogsDay* item in self.punchLogsDayList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"punchLogsDayList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.punchMonth = [jsonObject objectForKey: @"punchMonth"];
        if(self.punchMonth && [self.punchMonth isEqual:[NSNull null]])
            self.punchMonth = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"punchLogsDayList"];
            for(id itemJson in jsonArray) {
                EvhPunchLogsDay* item = [EvhPunchLogsDay new];
                
                [item fromJson: itemJson];
                [self.punchLogsDayList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
