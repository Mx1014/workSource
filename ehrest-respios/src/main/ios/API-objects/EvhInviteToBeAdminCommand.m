//
// EvhInviteToBeAdminCommand.m
//
#import "EvhInviteToBeAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInviteToBeAdminCommand
//

@implementation EvhInviteToBeAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInviteToBeAdminCommand* obj = [EvhInviteToBeAdminCommand new];
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
    if(self.invitationText)
        [jsonObject setObject: self.invitationText forKey: @"invitationText"];
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

        self.invitationText = [jsonObject objectForKey: @"invitationText"];
        if(self.invitationText && [self.invitationText isEqual:[NSNull null]])
            self.invitationText = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
