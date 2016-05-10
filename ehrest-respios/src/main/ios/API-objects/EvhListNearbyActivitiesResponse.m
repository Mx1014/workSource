//
// EvhListNearbyActivitiesResponse.m
//
#import "EvhListNearbyActivitiesResponse.h"
#import "EvhActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearbyActivitiesResponse
//

@implementation EvhListNearbyActivitiesResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListNearbyActivitiesResponse* obj = [EvhListNearbyActivitiesResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _activities = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.activities) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhActivityDTO* item in self.activities) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"activities"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"activities"];
            for(id itemJson in jsonArray) {
                EvhActivityDTO* item = [EvhActivityDTO new];
                
                [item fromJson: itemJson];
                [self.activities addObject: item];
            }
        }
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
