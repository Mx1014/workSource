//
// EvhRequestAdminRoleCommand.m
//
#import "EvhRequestAdminRoleCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRequestAdminRoleCommand
//

@implementation EvhRequestAdminRoleCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRequestAdminRoleCommand* obj = [EvhRequestAdminRoleCommand new];
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
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.requestText)
        [jsonObject setObject: self.requestText forKey: @"requestText"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.requestText = [jsonObject objectForKey: @"requestText"];
        if(self.requestText && [self.requestText isEqual:[NSNull null]])
            self.requestText = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
