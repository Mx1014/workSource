//
// EvhGetLaunchPadItemsByOrgCommand.m
//
#import "EvhGetLaunchPadItemsByOrgCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadItemsByOrgCommand
//

@implementation EvhGetLaunchPadItemsByOrgCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetLaunchPadItemsByOrgCommand* obj = [EvhGetLaunchPadItemsByOrgCommand new];
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
    if(self.itemLocation)
        [jsonObject setObject: self.itemLocation forKey: @"itemLocation"];
    if(self.itemGroup)
        [jsonObject setObject: self.itemGroup forKey: @"itemGroup"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.sceneType)
        [jsonObject setObject: self.sceneType forKey: @"sceneType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.itemLocation = [jsonObject objectForKey: @"itemLocation"];
        if(self.itemLocation && [self.itemLocation isEqual:[NSNull null]])
            self.itemLocation = nil;

        self.itemGroup = [jsonObject objectForKey: @"itemGroup"];
        if(self.itemGroup && [self.itemGroup isEqual:[NSNull null]])
            self.itemGroup = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.sceneType = [jsonObject objectForKey: @"sceneType"];
        if(self.sceneType && [self.sceneType isEqual:[NSNull null]])
            self.sceneType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
