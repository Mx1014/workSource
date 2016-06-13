//
// EvhListNearbyCommunityCommand.m
//
#import "EvhListNearbyCommunityCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearbyCommunityCommand
//

@implementation EvhListNearbyCommunityCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListNearbyCommunityCommand* obj = [EvhListNearbyCommunityCommand new];
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
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latigtue)
        [jsonObject setObject: self.latigtue forKey: @"latigtue"];
    if(self.pageOffset)
        [jsonObject setObject: self.pageOffset forKey: @"pageOffset"];
    if(self.communityType)
        [jsonObject setObject: self.communityType forKey: @"communityType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latigtue = [jsonObject objectForKey: @"latigtue"];
        if(self.latigtue && [self.latigtue isEqual:[NSNull null]])
            self.latigtue = nil;

        self.pageOffset = [jsonObject objectForKey: @"pageOffset"];
        if(self.pageOffset && [self.pageOffset isEqual:[NSNull null]])
            self.pageOffset = nil;

        self.communityType = [jsonObject objectForKey: @"communityType"];
        if(self.communityType && [self.communityType isEqual:[NSNull null]])
            self.communityType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
