//
// EvhGetBannersBySceneCommand.m
//
#import "EvhGetBannersBySceneCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBannersBySceneCommand
//

@implementation EvhGetBannersBySceneCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetBannersBySceneCommand* obj = [EvhGetBannersBySceneCommand new];
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
    if(self.bannerLocation)
        [jsonObject setObject: self.bannerLocation forKey: @"bannerLocation"];
    if(self.bannerGroup)
        [jsonObject setObject: self.bannerGroup forKey: @"bannerGroup"];
    if(self.sceneToken)
        [jsonObject setObject: self.sceneToken forKey: @"sceneToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.bannerLocation = [jsonObject objectForKey: @"bannerLocation"];
        if(self.bannerLocation && [self.bannerLocation isEqual:[NSNull null]])
            self.bannerLocation = nil;

        self.bannerGroup = [jsonObject objectForKey: @"bannerGroup"];
        if(self.bannerGroup && [self.bannerGroup isEqual:[NSNull null]])
            self.bannerGroup = nil;

        self.sceneToken = [jsonObject objectForKey: @"sceneToken"];
        if(self.sceneToken && [self.sceneToken isEqual:[NSNull null]])
            self.sceneToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
