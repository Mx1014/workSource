//
// EvhFetchPastToRecentMessageCommand.m
//
#import "EvhFetchPastToRecentMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchPastToRecentMessageCommand
//

@implementation EvhFetchPastToRecentMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFetchPastToRecentMessageCommand* obj = [EvhFetchPastToRecentMessageCommand new];
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
    if(self.appId)
        [jsonObject setObject: self.appId forKey: @"appId"];
    if(self.anchor)
        [jsonObject setObject: self.anchor forKey: @"anchor"];
    if(self.count)
        [jsonObject setObject: self.count forKey: @"count"];
    if(self.removeOld)
        [jsonObject setObject: self.removeOld forKey: @"removeOld"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.appId = [jsonObject objectForKey: @"appId"];
        if(self.appId && [self.appId isEqual:[NSNull null]])
            self.appId = nil;

        self.anchor = [jsonObject objectForKey: @"anchor"];
        if(self.anchor && [self.anchor isEqual:[NSNull null]])
            self.anchor = nil;

        self.count = [jsonObject objectForKey: @"count"];
        if(self.count && [self.count isEqual:[NSNull null]])
            self.count = nil;

        self.removeOld = [jsonObject objectForKey: @"removeOld"];
        if(self.removeOld && [self.removeOld isEqual:[NSNull null]])
            self.removeOld = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
