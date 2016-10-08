//
// EvhDoorAuthDTO.m
//
#import "EvhDoorAuthDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAuthDTO
//

@implementation EvhDoorAuthDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDoorAuthDTO* obj = [EvhDoorAuthDTO new];
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
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.authType)
        [jsonObject setObject: self.authType forKey: @"authType"];
    if(self.validFromMs)
        [jsonObject setObject: self.validFromMs forKey: @"validFromMs"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.validEndMs)
        [jsonObject setObject: self.validEndMs forKey: @"validEndMs"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.approveUserId)
        [jsonObject setObject: self.approveUserId forKey: @"approveUserId"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.doorName)
        [jsonObject setObject: self.doorName forKey: @"doorName"];
    if(self.hardwareId)
        [jsonObject setObject: self.hardwareId forKey: @"hardwareId"];
    if(self.nickname)
        [jsonObject setObject: self.nickname forKey: @"nickname"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.approveUserName)
        [jsonObject setObject: self.approveUserName forKey: @"approveUserName"];
    if(self.organization)
        [jsonObject setObject: self.organization forKey: @"organization"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.authType = [jsonObject objectForKey: @"authType"];
        if(self.authType && [self.authType isEqual:[NSNull null]])
            self.authType = nil;

        self.validFromMs = [jsonObject objectForKey: @"validFromMs"];
        if(self.validFromMs && [self.validFromMs isEqual:[NSNull null]])
            self.validFromMs = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.validEndMs = [jsonObject objectForKey: @"validEndMs"];
        if(self.validEndMs && [self.validEndMs isEqual:[NSNull null]])
            self.validEndMs = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.approveUserId = [jsonObject objectForKey: @"approveUserId"];
        if(self.approveUserId && [self.approveUserId isEqual:[NSNull null]])
            self.approveUserId = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.doorName = [jsonObject objectForKey: @"doorName"];
        if(self.doorName && [self.doorName isEqual:[NSNull null]])
            self.doorName = nil;

        self.hardwareId = [jsonObject objectForKey: @"hardwareId"];
        if(self.hardwareId && [self.hardwareId isEqual:[NSNull null]])
            self.hardwareId = nil;

        self.nickname = [jsonObject objectForKey: @"nickname"];
        if(self.nickname && [self.nickname isEqual:[NSNull null]])
            self.nickname = nil;

        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        self.approveUserName = [jsonObject objectForKey: @"approveUserName"];
        if(self.approveUserName && [self.approveUserName isEqual:[NSNull null]])
            self.approveUserName = nil;

        self.organization = [jsonObject objectForKey: @"organization"];
        if(self.organization && [self.organization isEqual:[NSNull null]])
            self.organization = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
