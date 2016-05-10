//
// EvhRevokeGroupMemberCommand.m
//
#import "EvhRevokeGroupMemberCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRevokeGroupMemberCommand
//

@implementation EvhRevokeGroupMemberCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRevokeGroupMemberCommand* obj = [EvhRevokeGroupMemberCommand new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.revokeText)
        [jsonObject setObject: self.revokeText forKey: @"revokeText"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.revokeText = [jsonObject objectForKey: @"revokeText"];
        if(self.revokeText && [self.revokeText isEqual:[NSNull null]])
            self.revokeText = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
