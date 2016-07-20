//
// EvhGetLaunchPadLayoutByVersionCodeCommand.m
//
#import "EvhGetLaunchPadLayoutByVersionCodeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadLayoutByVersionCodeCommand
//

@implementation EvhGetLaunchPadLayoutByVersionCodeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetLaunchPadLayoutByVersionCodeCommand* obj = [EvhGetLaunchPadLayoutByVersionCodeCommand new];
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
    if(self.versionCode)
        [jsonObject setObject: self.versionCode forKey: @"versionCode"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.sceneType)
        [jsonObject setObject: self.sceneType forKey: @"sceneType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.versionCode = [jsonObject objectForKey: @"versionCode"];
        if(self.versionCode && [self.versionCode isEqual:[NSNull null]])
            self.versionCode = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

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
