//
// EvhDoorAccessDTO.m
//
#import "EvhDoorAccessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessDTO
//

@implementation EvhDoorAccessDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDoorAccessDTO* obj = [EvhDoorAccessDTO new];
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
    if(self.activeUserId)
        [jsonObject setObject: self.activeUserId forKey: @"activeUserId"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.creatorUserId)
        [jsonObject setObject: self.creatorUserId forKey: @"creatorUserId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.doorType)
        [jsonObject setObject: self.doorType forKey: @"doorType"];
    if(self.geohash)
        [jsonObject setObject: self.geohash forKey: @"geohash"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.role)
        [jsonObject setObject: self.role forKey: @"role"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.hardwareId)
        [jsonObject setObject: self.hardwareId forKey: @"hardwareId"];
    if(self.creatorName)
        [jsonObject setObject: self.creatorName forKey: @"creatorName"];
    if(self.creatorPhone)
        [jsonObject setObject: self.creatorPhone forKey: @"creatorPhone"];
    if(self.linkStatus)
        [jsonObject setObject: self.linkStatus forKey: @"linkStatus"];
    if(self.version)
        [jsonObject setObject: self.version forKey: @"version"];
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.activeUserId = [jsonObject objectForKey: @"activeUserId"];
        if(self.activeUserId && [self.activeUserId isEqual:[NSNull null]])
            self.activeUserId = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.creatorUserId = [jsonObject objectForKey: @"creatorUserId"];
        if(self.creatorUserId && [self.creatorUserId isEqual:[NSNull null]])
            self.creatorUserId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.doorType = [jsonObject objectForKey: @"doorType"];
        if(self.doorType && [self.doorType isEqual:[NSNull null]])
            self.doorType = nil;

        self.geohash = [jsonObject objectForKey: @"geohash"];
        if(self.geohash && [self.geohash isEqual:[NSNull null]])
            self.geohash = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.role = [jsonObject objectForKey: @"role"];
        if(self.role && [self.role isEqual:[NSNull null]])
            self.role = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.hardwareId = [jsonObject objectForKey: @"hardwareId"];
        if(self.hardwareId && [self.hardwareId isEqual:[NSNull null]])
            self.hardwareId = nil;

        self.creatorName = [jsonObject objectForKey: @"creatorName"];
        if(self.creatorName && [self.creatorName isEqual:[NSNull null]])
            self.creatorName = nil;

        self.creatorPhone = [jsonObject objectForKey: @"creatorPhone"];
        if(self.creatorPhone && [self.creatorPhone isEqual:[NSNull null]])
            self.creatorPhone = nil;

        self.linkStatus = [jsonObject objectForKey: @"linkStatus"];
        if(self.linkStatus && [self.linkStatus isEqual:[NSNull null]])
            self.linkStatus = nil;

        self.version = [jsonObject objectForKey: @"version"];
        if(self.version && [self.version isEqual:[NSNull null]])
            self.version = nil;

        self.groupName = [jsonObject objectForKey: @"groupName"];
        if(self.groupName && [self.groupName isEqual:[NSNull null]])
            self.groupName = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
