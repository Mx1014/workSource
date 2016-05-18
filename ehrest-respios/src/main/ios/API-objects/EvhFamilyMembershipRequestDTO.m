//
// EvhFamilyMembershipRequestDTO.m
//
#import "EvhFamilyMembershipRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyMembershipRequestDTO
//

@implementation EvhFamilyMembershipRequestDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFamilyMembershipRequestDTO* obj = [EvhFamilyMembershipRequestDTO new];
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
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
    if(self.familyName)
        [jsonObject setObject: self.familyName forKey: @"familyName"];
    if(self.familyAvatarUri)
        [jsonObject setObject: self.familyAvatarUri forKey: @"familyAvatarUri"];
    if(self.familyAvatarUrl)
        [jsonObject setObject: self.familyAvatarUrl forKey: @"familyAvatarUrl"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.requestorUid)
        [jsonObject setObject: self.requestorUid forKey: @"requestorUid"];
    if(self.requestorName)
        [jsonObject setObject: self.requestorName forKey: @"requestorName"];
    if(self.requestorAvatar)
        [jsonObject setObject: self.requestorAvatar forKey: @"requestorAvatar"];
    if(self.requestingTime)
        [jsonObject setObject: self.requestingTime forKey: @"requestingTime"];
    if(self.requestorComment)
        [jsonObject setObject: self.requestorComment forKey: @"requestorComment"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        self.familyName = [jsonObject objectForKey: @"familyName"];
        if(self.familyName && [self.familyName isEqual:[NSNull null]])
            self.familyName = nil;

        self.familyAvatarUri = [jsonObject objectForKey: @"familyAvatarUri"];
        if(self.familyAvatarUri && [self.familyAvatarUri isEqual:[NSNull null]])
            self.familyAvatarUri = nil;

        self.familyAvatarUrl = [jsonObject objectForKey: @"familyAvatarUrl"];
        if(self.familyAvatarUrl && [self.familyAvatarUrl isEqual:[NSNull null]])
            self.familyAvatarUrl = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.requestorUid = [jsonObject objectForKey: @"requestorUid"];
        if(self.requestorUid && [self.requestorUid isEqual:[NSNull null]])
            self.requestorUid = nil;

        self.requestorName = [jsonObject objectForKey: @"requestorName"];
        if(self.requestorName && [self.requestorName isEqual:[NSNull null]])
            self.requestorName = nil;

        self.requestorAvatar = [jsonObject objectForKey: @"requestorAvatar"];
        if(self.requestorAvatar && [self.requestorAvatar isEqual:[NSNull null]])
            self.requestorAvatar = nil;

        self.requestingTime = [jsonObject objectForKey: @"requestingTime"];
        if(self.requestingTime && [self.requestingTime isEqual:[NSNull null]])
            self.requestingTime = nil;

        self.requestorComment = [jsonObject objectForKey: @"requestorComment"];
        if(self.requestorComment && [self.requestorComment isEqual:[NSNull null]])
            self.requestorComment = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
