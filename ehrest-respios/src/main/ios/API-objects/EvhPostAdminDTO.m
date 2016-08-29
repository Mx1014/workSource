//
// EvhPostAdminDTO.m
//
#import "EvhPostAdminDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPostAdminDTO
//

@implementation EvhPostAdminDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPostAdminDTO* obj = [EvhPostAdminDTO new];
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
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.parentPostId)
        [jsonObject setObject: self.parentPostId forKey: @"parentPostId"];
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.forumName)
        [jsonObject setObject: self.forumName forKey: @"forumName"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.creatorNickName)
        [jsonObject setObject: self.creatorNickName forKey: @"creatorNickName"];
    if(self.creatorPhone)
        [jsonObject setObject: self.creatorPhone forKey: @"creatorPhone"];
    if(self.creatorAdminFlag)
        [jsonObject setObject: self.creatorAdminFlag forKey: @"creatorAdminFlag"];
    if(self.creatorTag)
        [jsonObject setObject: self.creatorTag forKey: @"creatorTag"];
    if(self.targetTag)
        [jsonObject setObject: self.targetTag forKey: @"targetTag"];
    if(self.contentCategory)
        [jsonObject setObject: self.contentCategory forKey: @"contentCategory"];
    if(self.actionCategory)
        [jsonObject setObject: self.actionCategory forKey: @"actionCategory"];
    if(self.visibleRegionType)
        [jsonObject setObject: self.visibleRegionType forKey: @"visibleRegionType"];
    if(self.visibleRegionId)
        [jsonObject setObject: self.visibleRegionId forKey: @"visibleRegionId"];
    if(self.visibleRegionName)
        [jsonObject setObject: self.visibleRegionName forKey: @"visibleRegionName"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.contentType)
        [jsonObject setObject: self.contentType forKey: @"contentType"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.embeddedAppId)
        [jsonObject setObject: self.embeddedAppId forKey: @"embeddedAppId"];
    if(self.embeddedId)
        [jsonObject setObject: self.embeddedId forKey: @"embeddedId"];
    if(self.isForwarded)
        [jsonObject setObject: self.isForwarded forKey: @"isForwarded"];
    if(self.childCount)
        [jsonObject setObject: self.childCount forKey: @"childCount"];
    if(self.forwardCount)
        [jsonObject setObject: self.forwardCount forKey: @"forwardCount"];
    if(self.likeCount)
        [jsonObject setObject: self.likeCount forKey: @"likeCount"];
    if(self.viewCount)
        [jsonObject setObject: self.viewCount forKey: @"viewCount"];
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.assignedFlag)
        [jsonObject setObject: self.assignedFlag forKey: @"assignedFlag"];
    if(self.creatorAddress)
        [jsonObject setObject: self.creatorAddress forKey: @"creatorAddress"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        self.parentPostId = [jsonObject objectForKey: @"parentPostId"];
        if(self.parentPostId && [self.parentPostId isEqual:[NSNull null]])
            self.parentPostId = nil;

        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.forumName = [jsonObject objectForKey: @"forumName"];
        if(self.forumName && [self.forumName isEqual:[NSNull null]])
            self.forumName = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.creatorNickName = [jsonObject objectForKey: @"creatorNickName"];
        if(self.creatorNickName && [self.creatorNickName isEqual:[NSNull null]])
            self.creatorNickName = nil;

        self.creatorPhone = [jsonObject objectForKey: @"creatorPhone"];
        if(self.creatorPhone && [self.creatorPhone isEqual:[NSNull null]])
            self.creatorPhone = nil;

        self.creatorAdminFlag = [jsonObject objectForKey: @"creatorAdminFlag"];
        if(self.creatorAdminFlag && [self.creatorAdminFlag isEqual:[NSNull null]])
            self.creatorAdminFlag = nil;

        self.creatorTag = [jsonObject objectForKey: @"creatorTag"];
        if(self.creatorTag && [self.creatorTag isEqual:[NSNull null]])
            self.creatorTag = nil;

        self.targetTag = [jsonObject objectForKey: @"targetTag"];
        if(self.targetTag && [self.targetTag isEqual:[NSNull null]])
            self.targetTag = nil;

        self.contentCategory = [jsonObject objectForKey: @"contentCategory"];
        if(self.contentCategory && [self.contentCategory isEqual:[NSNull null]])
            self.contentCategory = nil;

        self.actionCategory = [jsonObject objectForKey: @"actionCategory"];
        if(self.actionCategory && [self.actionCategory isEqual:[NSNull null]])
            self.actionCategory = nil;

        self.visibleRegionType = [jsonObject objectForKey: @"visibleRegionType"];
        if(self.visibleRegionType && [self.visibleRegionType isEqual:[NSNull null]])
            self.visibleRegionType = nil;

        self.visibleRegionId = [jsonObject objectForKey: @"visibleRegionId"];
        if(self.visibleRegionId && [self.visibleRegionId isEqual:[NSNull null]])
            self.visibleRegionId = nil;

        self.visibleRegionName = [jsonObject objectForKey: @"visibleRegionName"];
        if(self.visibleRegionName && [self.visibleRegionName isEqual:[NSNull null]])
            self.visibleRegionName = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.contentType = [jsonObject objectForKey: @"contentType"];
        if(self.contentType && [self.contentType isEqual:[NSNull null]])
            self.contentType = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.embeddedAppId = [jsonObject objectForKey: @"embeddedAppId"];
        if(self.embeddedAppId && [self.embeddedAppId isEqual:[NSNull null]])
            self.embeddedAppId = nil;

        self.embeddedId = [jsonObject objectForKey: @"embeddedId"];
        if(self.embeddedId && [self.embeddedId isEqual:[NSNull null]])
            self.embeddedId = nil;

        self.isForwarded = [jsonObject objectForKey: @"isForwarded"];
        if(self.isForwarded && [self.isForwarded isEqual:[NSNull null]])
            self.isForwarded = nil;

        self.childCount = [jsonObject objectForKey: @"childCount"];
        if(self.childCount && [self.childCount isEqual:[NSNull null]])
            self.childCount = nil;

        self.forwardCount = [jsonObject objectForKey: @"forwardCount"];
        if(self.forwardCount && [self.forwardCount isEqual:[NSNull null]])
            self.forwardCount = nil;

        self.likeCount = [jsonObject objectForKey: @"likeCount"];
        if(self.likeCount && [self.likeCount isEqual:[NSNull null]])
            self.likeCount = nil;

        self.viewCount = [jsonObject objectForKey: @"viewCount"];
        if(self.viewCount && [self.viewCount isEqual:[NSNull null]])
            self.viewCount = nil;

        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.assignedFlag = [jsonObject objectForKey: @"assignedFlag"];
        if(self.assignedFlag && [self.assignedFlag isEqual:[NSNull null]])
            self.assignedFlag = nil;

        self.creatorAddress = [jsonObject objectForKey: @"creatorAddress"];
        if(self.creatorAddress && [self.creatorAddress isEqual:[NSNull null]])
            self.creatorAddress = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
