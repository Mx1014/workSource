//
// EvhListYearPunchLogsCommandResponse.m
//
#import "EvhListYearPunchLogsCommandResponse.h"
#import "EvhPunchLogsMonthList.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListYearPunchLogsCommandResponse
//

@implementation EvhListYearPunchLogsCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListYearPunchLogsCommandResponse* obj = [EvhListYearPunchLogsCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _punchLogsMonthList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.punchYear)
        [jsonObject setObject: self.punchYear forKey: @"punchYear"];
    if(self.punchLogsMonthList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPunchLogsMonthList* item in self.punchLogsMonthList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"punchLogsMonthList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.punchYear = [jsonObject objectForKey: @"punchYear"];
        if(self.punchYear && [self.punchYear isEqual:[NSNull null]])
            self.punchYear = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"punchLogsMonthList"];
            for(id itemJson in jsonArray) {
                EvhPunchLogsMonthList* item = [EvhPunchLogsMonthList new];
                
                [item fromJson: itemJson];
                [self.punchLogsMonthList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
