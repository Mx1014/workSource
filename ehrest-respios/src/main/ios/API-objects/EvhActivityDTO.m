//
// EvhActivityDTO.m
//
#import "EvhActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityDTO
//

@implementation EvhActivityDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivityDTO* obj = [EvhActivityDTO new];
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
    if(self.activityId)
        [jsonObject setObject: self.activityId forKey: @"activityId"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.postId)
        [jsonObject setObject: self.postId forKey: @"postId"];
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.stopTime)
        [jsonObject setObject: self.stopTime forKey: @"stopTime"];
    if(self.location)
        [jsonObject setObject: self.location forKey: @"location"];
    if(self.checkinFlag)
        [jsonObject setObject: self.checkinFlag forKey: @"checkinFlag"];
    if(self.confirmFlag)
        [jsonObject setObject: self.confirmFlag forKey: @"confirmFlag"];
    if(self.enrollUserCount)
        [jsonObject setObject: self.enrollUserCount forKey: @"enrollUserCount"];
    if(self.enrollFamilyCount)
        [jsonObject setObject: self.enrollFamilyCount forKey: @"enrollFamilyCount"];
    if(self.checkinUserCount)
        [jsonObject setObject: self.checkinUserCount forKey: @"checkinUserCount"];
    if(self.checkinFamilyCount)
        [jsonObject setObject: self.checkinFamilyCount forKey: @"checkinFamilyCount"];
    if(self.confirmUserCount)
        [jsonObject setObject: self.confirmUserCount forKey: @"confirmUserCount"];
    if(self.confirmFamilyCount)
        [jsonObject setObject: self.confirmFamilyCount forKey: @"confirmFamilyCount"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.posterUrl)
        [jsonObject setObject: self.posterUrl forKey: @"posterUrl"];
    if(self.userActivityStatus)
        [jsonObject setObject: self.userActivityStatus forKey: @"userActivityStatus"];
    if(self.processStatus)
        [jsonObject setObject: self.processStatus forKey: @"processStatus"];
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.guest)
        [jsonObject setObject: self.guest forKey: @"guest"];
    if(self.mediaUrl)
        [jsonObject setObject: self.mediaUrl forKey: @"mediaUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.activityId = [jsonObject objectForKey: @"activityId"];
        if(self.activityId && [self.activityId isEqual:[NSNull null]])
            self.activityId = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.postId = [jsonObject objectForKey: @"postId"];
        if(self.postId && [self.postId isEqual:[NSNull null]])
            self.postId = nil;

        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.stopTime = [jsonObject objectForKey: @"stopTime"];
        if(self.stopTime && [self.stopTime isEqual:[NSNull null]])
            self.stopTime = nil;

        self.location = [jsonObject objectForKey: @"location"];
        if(self.location && [self.location isEqual:[NSNull null]])
            self.location = nil;

        self.checkinFlag = [jsonObject objectForKey: @"checkinFlag"];
        if(self.checkinFlag && [self.checkinFlag isEqual:[NSNull null]])
            self.checkinFlag = nil;

        self.confirmFlag = [jsonObject objectForKey: @"confirmFlag"];
        if(self.confirmFlag && [self.confirmFlag isEqual:[NSNull null]])
            self.confirmFlag = nil;

        self.enrollUserCount = [jsonObject objectForKey: @"enrollUserCount"];
        if(self.enrollUserCount && [self.enrollUserCount isEqual:[NSNull null]])
            self.enrollUserCount = nil;

        self.enrollFamilyCount = [jsonObject objectForKey: @"enrollFamilyCount"];
        if(self.enrollFamilyCount && [self.enrollFamilyCount isEqual:[NSNull null]])
            self.enrollFamilyCount = nil;

        self.checkinUserCount = [jsonObject objectForKey: @"checkinUserCount"];
        if(self.checkinUserCount && [self.checkinUserCount isEqual:[NSNull null]])
            self.checkinUserCount = nil;

        self.checkinFamilyCount = [jsonObject objectForKey: @"checkinFamilyCount"];
        if(self.checkinFamilyCount && [self.checkinFamilyCount isEqual:[NSNull null]])
            self.checkinFamilyCount = nil;

        self.confirmUserCount = [jsonObject objectForKey: @"confirmUserCount"];
        if(self.confirmUserCount && [self.confirmUserCount isEqual:[NSNull null]])
            self.confirmUserCount = nil;

        self.confirmFamilyCount = [jsonObject objectForKey: @"confirmFamilyCount"];
        if(self.confirmFamilyCount && [self.confirmFamilyCount isEqual:[NSNull null]])
            self.confirmFamilyCount = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.posterUrl = [jsonObject objectForKey: @"posterUrl"];
        if(self.posterUrl && [self.posterUrl isEqual:[NSNull null]])
            self.posterUrl = nil;

        self.userActivityStatus = [jsonObject objectForKey: @"userActivityStatus"];
        if(self.userActivityStatus && [self.userActivityStatus isEqual:[NSNull null]])
            self.userActivityStatus = nil;

        self.processStatus = [jsonObject objectForKey: @"processStatus"];
        if(self.processStatus && [self.processStatus isEqual:[NSNull null]])
            self.processStatus = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        self.guest = [jsonObject objectForKey: @"guest"];
        if(self.guest && [self.guest isEqual:[NSNull null]])
            self.guest = nil;

        self.mediaUrl = [jsonObject objectForKey: @"mediaUrl"];
        if(self.mediaUrl && [self.mediaUrl isEqual:[NSNull null]])
            self.mediaUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
