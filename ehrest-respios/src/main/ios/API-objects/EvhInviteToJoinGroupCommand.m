//
// EvhInviteToJoinGroupCommand.m
//
#import "EvhInviteToJoinGroupCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInviteToJoinGroupCommand
//

@implementation EvhInviteToJoinGroupCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInviteToJoinGroupCommand* obj = [EvhInviteToJoinGroupCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _userIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.userIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.userIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"userIds"];
    }
    if(self.invitationText)
        [jsonObject setObject: self.invitationText forKey: @"invitationText"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"userIds"];
            for(id itemJson in jsonArray) {
                [self.userIds addObject: itemJson];
            }
        }
        self.invitationText = [jsonObject objectForKey: @"invitationText"];
        if(self.invitationText && [self.invitationText isEqual:[NSNull null]])
            self.invitationText = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
