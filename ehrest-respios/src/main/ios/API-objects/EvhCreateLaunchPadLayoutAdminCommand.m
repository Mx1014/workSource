//
// EvhCreateLaunchPadLayoutAdminCommand.m
//
#import "EvhCreateLaunchPadLayoutAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLaunchPadLayoutAdminCommand
//

@implementation EvhCreateLaunchPadLayoutAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateLaunchPadLayoutAdminCommand* obj = [EvhCreateLaunchPadLayoutAdminCommand new];
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
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.layoutJson)
        [jsonObject setObject: self.layoutJson forKey: @"layoutJson"];
    if(self.versionCode)
        [jsonObject setObject: self.versionCode forKey: @"versionCode"];
    if(self.minVersionCode)
        [jsonObject setObject: self.minVersionCode forKey: @"minVersionCode"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.layoutJson = [jsonObject objectForKey: @"layoutJson"];
        if(self.layoutJson && [self.layoutJson isEqual:[NSNull null]])
            self.layoutJson = nil;

        self.versionCode = [jsonObject objectForKey: @"versionCode"];
        if(self.versionCode && [self.versionCode isEqual:[NSNull null]])
            self.versionCode = nil;

        self.minVersionCode = [jsonObject objectForKey: @"minVersionCode"];
        if(self.minVersionCode && [self.minVersionCode isEqual:[NSNull null]])
            self.minVersionCode = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
