//
// EvhUserInvitationRosterDTO.m
//
#import "EvhUserInvitationRosterDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserInvitationRosterDTO
//

@implementation EvhUserInvitationRosterDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserInvitationRosterDTO* obj = [EvhUserInvitationRosterDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.inviteId)
        [jsonObject setObject: self.inviteId forKey: @"inviteId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.inviteId = [jsonObject objectForKey: @"inviteId"];
        if(self.inviteId && [self.inviteId isEqual:[NSNull null]])
            self.inviteId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
