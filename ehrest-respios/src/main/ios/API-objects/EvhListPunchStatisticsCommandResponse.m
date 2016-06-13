//
// EvhListPunchStatisticsCommandResponse.m
//
#import "EvhListPunchStatisticsCommandResponse.h"
#import "EvhPunchStatisticsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPunchStatisticsCommandResponse
//

@implementation EvhListPunchStatisticsCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPunchStatisticsCommandResponse* obj = [EvhListPunchStatisticsCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _punchList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
    if(self.punchList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPunchStatisticsDTO* item in self.punchList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"punchList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"punchList"];
            for(id itemJson in jsonArray) {
                EvhPunchStatisticsDTO* item = [EvhPunchStatisticsDTO new];
                
                [item fromJson: itemJson];
                [self.punchList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
