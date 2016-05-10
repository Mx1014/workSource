//
// EvhInvitationDTO.m
//
#import "EvhInvitationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInvitationDTO
//

@implementation EvhInvitationDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInvitationDTO* obj = [EvhInvitationDTO new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.identifier)
        [jsonObject setObject: self.identifier forKey: @"identifier"];
    if(self.inviteType)
        [jsonObject setObject: self.inviteType forKey: @"inviteType"];
    if(self.inviteName)
        [jsonObject setObject: self.inviteName forKey: @"inviteName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.identifier = [jsonObject objectForKey: @"identifier"];
        if(self.identifier && [self.identifier isEqual:[NSNull null]])
            self.identifier = nil;

        self.inviteType = [jsonObject objectForKey: @"inviteType"];
        if(self.inviteType && [self.inviteType isEqual:[NSNull null]])
            self.inviteType = nil;

        self.inviteName = [jsonObject objectForKey: @"inviteName"];
        if(self.inviteName && [self.inviteName isEqual:[NSNull null]])
            self.inviteName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
