//
// EvhInviteToJoinGroupByPhoneCommand.m
//
#import "EvhInviteToJoinGroupByPhoneCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInviteToJoinGroupByPhoneCommand
//

@implementation EvhInviteToJoinGroupByPhoneCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInviteToJoinGroupByPhoneCommand* obj = [EvhInviteToJoinGroupByPhoneCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _phones = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.phones) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.phones) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"phones"];
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
            NSArray* jsonArray = [jsonObject objectForKey: @"phones"];
            for(id itemJson in jsonArray) {
                [self.phones addObject: itemJson];
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
