//
// EvhGetAppVersion.m
//
#import "EvhGetAppVersion.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetAppVersion
//

@implementation EvhGetAppVersion

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetAppVersion* obj = [EvhGetAppVersion new];
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
    if(self.downloadLink)
        [jsonObject setObject: self.downloadLink forKey: @"downloadLink"];
    if(self.versionCode)
        [jsonObject setObject: self.versionCode forKey: @"versionCode"];
    if(self.versionName)
        [jsonObject setObject: self.versionName forKey: @"versionName"];
    if(self.versionDescLink)
        [jsonObject setObject: self.versionDescLink forKey: @"versionDescLink"];
    if(self.operation)
        [jsonObject setObject: self.operation forKey: @"operation"];
    if(self.mktDataVersion)
        [jsonObject setObject: self.mktDataVersion forKey: @"mktDataVersion"];
    if(self.userLocRptFreq)
        [jsonObject setObject: self.userLocRptFreq forKey: @"userLocRptFreq"];
    if(self.userContactRptFreq)
        [jsonObject setObject: self.userContactRptFreq forKey: @"userContactRptFreq"];
    if(self.userRptConfigVersion)
        [jsonObject setObject: self.userRptConfigVersion forKey: @"userRptConfigVersion"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.downloadLink = [jsonObject objectForKey: @"downloadLink"];
        if(self.downloadLink && [self.downloadLink isEqual:[NSNull null]])
            self.downloadLink = nil;

        self.versionCode = [jsonObject objectForKey: @"versionCode"];
        if(self.versionCode && [self.versionCode isEqual:[NSNull null]])
            self.versionCode = nil;

        self.versionName = [jsonObject objectForKey: @"versionName"];
        if(self.versionName && [self.versionName isEqual:[NSNull null]])
            self.versionName = nil;

        self.versionDescLink = [jsonObject objectForKey: @"versionDescLink"];
        if(self.versionDescLink && [self.versionDescLink isEqual:[NSNull null]])
            self.versionDescLink = nil;

        self.operation = [jsonObject objectForKey: @"operation"];
        if(self.operation && [self.operation isEqual:[NSNull null]])
            self.operation = nil;

        self.mktDataVersion = [jsonObject objectForKey: @"mktDataVersion"];
        if(self.mktDataVersion && [self.mktDataVersion isEqual:[NSNull null]])
            self.mktDataVersion = nil;

        self.userLocRptFreq = [jsonObject objectForKey: @"userLocRptFreq"];
        if(self.userLocRptFreq && [self.userLocRptFreq isEqual:[NSNull null]])
            self.userLocRptFreq = nil;

        self.userContactRptFreq = [jsonObject objectForKey: @"userContactRptFreq"];
        if(self.userContactRptFreq && [self.userContactRptFreq isEqual:[NSNull null]])
            self.userContactRptFreq = nil;

        self.userRptConfigVersion = [jsonObject objectForKey: @"userRptConfigVersion"];
        if(self.userRptConfigVersion && [self.userRptConfigVersion isEqual:[NSNull null]])
            self.userRptConfigVersion = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
