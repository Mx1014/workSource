//
// EvhActivityPostCommand.m
//
#import "EvhActivityPostCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityPostCommand
//

@implementation EvhActivityPostCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivityPostCommand* obj = [EvhActivityPostCommand new];
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
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.location)
        [jsonObject setObject: self.location forKey: @"location"];
    if(self.contactPerson)
        [jsonObject setObject: self.contactPerson forKey: @"contactPerson"];
    if(self.contactNumber)
        [jsonObject setObject: self.contactNumber forKey: @"contactNumber"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.checkinFlag)
        [jsonObject setObject: self.checkinFlag forKey: @"checkinFlag"];
    if(self.confirmFlag)
        [jsonObject setObject: self.confirmFlag forKey: @"confirmFlag"];
    if(self.maxAttendeeCount)
        [jsonObject setObject: self.maxAttendeeCount forKey: @"maxAttendeeCount"];
    if(self.creatorFamilyId)
        [jsonObject setObject: self.creatorFamilyId forKey: @"creatorFamilyId"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.changeVersion)
        [jsonObject setObject: self.changeVersion forKey: @"changeVersion"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.posterUri)
        [jsonObject setObject: self.posterUri forKey: @"posterUri"];
    if(self.guest)
        [jsonObject setObject: self.guest forKey: @"guest"];
    if(self.mediaUrl)
        [jsonObject setObject: self.mediaUrl forKey: @"mediaUrl"];
    if(self.officialFlag)
        [jsonObject setObject: self.officialFlag forKey: @"officialFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.location = [jsonObject objectForKey: @"location"];
        if(self.location && [self.location isEqual:[NSNull null]])
            self.location = nil;

        self.contactPerson = [jsonObject objectForKey: @"contactPerson"];
        if(self.contactPerson && [self.contactPerson isEqual:[NSNull null]])
            self.contactPerson = nil;

        self.contactNumber = [jsonObject objectForKey: @"contactNumber"];
        if(self.contactNumber && [self.contactNumber isEqual:[NSNull null]])
            self.contactNumber = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.checkinFlag = [jsonObject objectForKey: @"checkinFlag"];
        if(self.checkinFlag && [self.checkinFlag isEqual:[NSNull null]])
            self.checkinFlag = nil;

        self.confirmFlag = [jsonObject objectForKey: @"confirmFlag"];
        if(self.confirmFlag && [self.confirmFlag isEqual:[NSNull null]])
            self.confirmFlag = nil;

        self.maxAttendeeCount = [jsonObject objectForKey: @"maxAttendeeCount"];
        if(self.maxAttendeeCount && [self.maxAttendeeCount isEqual:[NSNull null]])
            self.maxAttendeeCount = nil;

        self.creatorFamilyId = [jsonObject objectForKey: @"creatorFamilyId"];
        if(self.creatorFamilyId && [self.creatorFamilyId isEqual:[NSNull null]])
            self.creatorFamilyId = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.changeVersion = [jsonObject objectForKey: @"changeVersion"];
        if(self.changeVersion && [self.changeVersion isEqual:[NSNull null]])
            self.changeVersion = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.posterUri = [jsonObject objectForKey: @"posterUri"];
        if(self.posterUri && [self.posterUri isEqual:[NSNull null]])
            self.posterUri = nil;

        self.guest = [jsonObject objectForKey: @"guest"];
        if(self.guest && [self.guest isEqual:[NSNull null]])
            self.guest = nil;

        self.mediaUrl = [jsonObject objectForKey: @"mediaUrl"];
        if(self.mediaUrl && [self.mediaUrl isEqual:[NSNull null]])
            self.mediaUrl = nil;

        self.officialFlag = [jsonObject objectForKey: @"officialFlag"];
        if(self.officialFlag && [self.officialFlag isEqual:[NSNull null]])
            self.officialFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
