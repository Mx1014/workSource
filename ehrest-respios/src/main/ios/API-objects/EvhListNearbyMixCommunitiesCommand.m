//
// EvhListNearbyMixCommunitiesCommand.m
//
#import "EvhListNearbyMixCommunitiesCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearbyMixCommunitiesCommand
//

@implementation EvhListNearbyMixCommunitiesCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListNearbyMixCommunitiesCommand* obj = [EvhListNearbyMixCommunitiesCommand new];
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
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latigtue)
        [jsonObject setObject: self.latigtue forKey: @"latigtue"];
    if(self.communityType)
        [jsonObject setObject: self.communityType forKey: @"communityType"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latigtue = [jsonObject objectForKey: @"latigtue"];
        if(self.latigtue && [self.latigtue isEqual:[NSNull null]])
            self.latigtue = nil;

        self.communityType = [jsonObject objectForKey: @"communityType"];
        if(self.communityType && [self.communityType isEqual:[NSNull null]])
            self.communityType = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
