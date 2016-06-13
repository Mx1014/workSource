//
// EvhGetBannersByOrgCommand.m
//
#import "EvhGetBannersByOrgCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBannersByOrgCommand
//

@implementation EvhGetBannersByOrgCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetBannersByOrgCommand* obj = [EvhGetBannersByOrgCommand new];
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
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.bannerLocation)
        [jsonObject setObject: self.bannerLocation forKey: @"bannerLocation"];
    if(self.bannerGroup)
        [jsonObject setObject: self.bannerGroup forKey: @"bannerGroup"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.sceneType)
        [jsonObject setObject: self.sceneType forKey: @"sceneType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.bannerLocation = [jsonObject objectForKey: @"bannerLocation"];
        if(self.bannerLocation && [self.bannerLocation isEqual:[NSNull null]])
            self.bannerLocation = nil;

        self.bannerGroup = [jsonObject objectForKey: @"bannerGroup"];
        if(self.bannerGroup && [self.bannerGroup isEqual:[NSNull null]])
            self.bannerGroup = nil;

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
