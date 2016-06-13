//
// EvhGetUpgradeFileInfoCommand.m
//
#import "EvhGetUpgradeFileInfoCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUpgradeFileInfoCommand
//

@implementation EvhGetUpgradeFileInfoCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetUpgradeFileInfoCommand* obj = [EvhGetUpgradeFileInfoCommand new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.versionCode)
        [jsonObject setObject: self.versionCode forKey: @"versionCode"];
    if(self.packageEdition)
        [jsonObject setObject: self.packageEdition forKey: @"packageEdition"];
    if(self.devicePlatform)
        [jsonObject setObject: self.devicePlatform forKey: @"devicePlatform"];
    if(self.distributionChannel)
        [jsonObject setObject: self.distributionChannel forKey: @"distributionChannel"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
    if(self.jsonParams)
        [jsonObject setObject: self.jsonParams forKey: @"jsonParams"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.versionCode = [jsonObject objectForKey: @"versionCode"];
        if(self.versionCode && [self.versionCode isEqual:[NSNull null]])
            self.versionCode = nil;

        self.packageEdition = [jsonObject objectForKey: @"packageEdition"];
        if(self.packageEdition && [self.packageEdition isEqual:[NSNull null]])
            self.packageEdition = nil;

        self.devicePlatform = [jsonObject objectForKey: @"devicePlatform"];
        if(self.devicePlatform && [self.devicePlatform isEqual:[NSNull null]])
            self.devicePlatform = nil;

        self.distributionChannel = [jsonObject objectForKey: @"distributionChannel"];
        if(self.distributionChannel && [self.distributionChannel isEqual:[NSNull null]])
            self.distributionChannel = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        self.jsonParams = [jsonObject objectForKey: @"jsonParams"];
        if(self.jsonParams && [self.jsonParams isEqual:[NSNull null]])
            self.jsonParams = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
