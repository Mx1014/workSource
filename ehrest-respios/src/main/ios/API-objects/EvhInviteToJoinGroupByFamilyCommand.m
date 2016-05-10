//
// EvhInviteToJoinGroupByFamilyCommand.m
//
#import "EvhInviteToJoinGroupByFamilyCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInviteToJoinGroupByFamilyCommand
//

@implementation EvhInviteToJoinGroupByFamilyCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInviteToJoinGroupByFamilyCommand* obj = [EvhInviteToJoinGroupByFamilyCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _familyIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.familyIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.familyIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"familyIds"];
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
            NSArray* jsonArray = [jsonObject objectForKey: @"familyIds"];
            for(id itemJson in jsonArray) {
                [self.familyIds addObject: itemJson];
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
