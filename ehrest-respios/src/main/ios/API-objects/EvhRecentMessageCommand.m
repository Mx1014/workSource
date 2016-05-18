//
// EvhRecentMessageCommand.m
//
#import "EvhRecentMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecentMessageCommand
//

@implementation EvhRecentMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRecentMessageCommand* obj = [EvhRecentMessageCommand new];
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
    if(self.deviceId)
        [jsonObject setObject: self.deviceId forKey: @"deviceId"];
    if(self.anchor)
        [jsonObject setObject: self.anchor forKey: @"anchor"];
    if(self.count)
        [jsonObject setObject: self.count forKey: @"count"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.deviceId = [jsonObject objectForKey: @"deviceId"];
        if(self.deviceId && [self.deviceId isEqual:[NSNull null]])
            self.deviceId = nil;

        self.anchor = [jsonObject objectForKey: @"anchor"];
        if(self.anchor && [self.anchor isEqual:[NSNull null]])
            self.anchor = nil;

        self.count = [jsonObject objectForKey: @"count"];
        if(self.count && [self.count isEqual:[NSNull null]])
            self.count = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
