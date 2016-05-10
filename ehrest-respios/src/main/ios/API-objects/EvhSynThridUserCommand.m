//
// EvhSynThridUserCommand.m
//
#import "EvhSynThridUserCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSynThridUserCommand
//

@implementation EvhSynThridUserCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSynThridUserCommand* obj = [EvhSynThridUserCommand new];
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
    if(self.namespaceUserToken)
        [jsonObject setObject: self.namespaceUserToken forKey: @"namespaceUserToken"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.randomNum)
        [jsonObject setObject: self.randomNum forKey: @"randomNum"];
    if(self.timestamp)
        [jsonObject setObject: self.timestamp forKey: @"timestamp"];
    if(self.sign)
        [jsonObject setObject: self.sign forKey: @"sign"];
    if(self.key)
        [jsonObject setObject: self.key forKey: @"key"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.deviceIdentifier)
        [jsonObject setObject: self.deviceIdentifier forKey: @"deviceIdentifier"];
    if(self.pusherIdentify)
        [jsonObject setObject: self.pusherIdentify forKey: @"pusherIdentify"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceUserToken = [jsonObject objectForKey: @"namespaceUserToken"];
        if(self.namespaceUserToken && [self.namespaceUserToken isEqual:[NSNull null]])
            self.namespaceUserToken = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.randomNum = [jsonObject objectForKey: @"randomNum"];
        if(self.randomNum && [self.randomNum isEqual:[NSNull null]])
            self.randomNum = nil;

        self.timestamp = [jsonObject objectForKey: @"timestamp"];
        if(self.timestamp && [self.timestamp isEqual:[NSNull null]])
            self.timestamp = nil;

        self.sign = [jsonObject objectForKey: @"sign"];
        if(self.sign && [self.sign isEqual:[NSNull null]])
            self.sign = nil;

        self.key = [jsonObject objectForKey: @"key"];
        if(self.key && [self.key isEqual:[NSNull null]])
            self.key = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.deviceIdentifier = [jsonObject objectForKey: @"deviceIdentifier"];
        if(self.deviceIdentifier && [self.deviceIdentifier isEqual:[NSNull null]])
            self.deviceIdentifier = nil;

        self.pusherIdentify = [jsonObject objectForKey: @"pusherIdentify"];
        if(self.pusherIdentify && [self.pusherIdentify isEqual:[NSNull null]])
            self.pusherIdentify = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
