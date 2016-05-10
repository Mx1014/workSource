//
// EvhPostDTO.m
//
#import "EvhPostDTO.h"
#import "EvhAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPostDTO
//

@implementation EvhPostDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPostDTO* obj = [EvhPostDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _attachments = [NSMutableArray new];
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
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.creatorNickName)
        [jsonObject setObject: self.creatorNickName forKey: @"creatorNickName"];
    if(self.creatorAvatar)
        [jsonObject setObject: self.creatorAvatar forKey: @"creatorAvatar"];
    if(self.creatorAvatarUrl)
        [jsonObject setObject: self.creatorAvatarUrl forKey: @"creatorAvatarUrl"];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
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
    if(self.embeddedJson)
        [jsonObject setObject: self.embeddedJson forKey: @"embeddedJson"];
    if(self.isForwarded)
        [jsonObject setObject: self.isForwarded forKey: @"isForwarded"];
    if(self.childCount)
        [jsonObject setObject: self.childCount forKey: @"childCount"];
    if(self.forwardCount)
        [jsonObject setObject: self.forwardCount forKey: @"forwardCount"];
    if(self.likeCount)
        [jsonObject setObject: self.likeCount forKey: @"likeCount"];
    if(self.dislikeCount)
        [jsonObject setObject: self.dislikeCount forKey: @"dislikeCount"];
    if(self.viewCount)
        [jsonObject setObject: self.viewCount forKey: @"viewCount"];
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAttachmentDTO* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
    if(self.assignedFlag)
        [jsonObject setObject: self.assignedFlag forKey: @"assignedFlag"];
    if(self.forumName)
        [jsonObject setObject: self.forumName forKey: @"forumName"];
    if(self.likeFlag)
        [jsonObject setObject: self.likeFlag forKey: @"likeFlag"];
    if(self.favoriteFlag)
        [jsonObject setObject: self.favoriteFlag forKey: @"favoriteFlag"];
    if(self.shareUrl)
        [jsonObject setObject: self.shareUrl forKey: @"shareUrl"];
    if(self.privateFlag)
        [jsonObject setObject: self.privateFlag forKey: @"privateFlag"];
    if(self.floorNumber)
        [jsonObject setObject: self.floorNumber forKey: @"floorNumber"];
    if(self.publishStatus)
        [jsonObject setObject: self.publishStatus forKey: @"publishStatus"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
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

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.creatorNickName = [jsonObject objectForKey: @"creatorNickName"];
        if(self.creatorNickName && [self.creatorNickName isEqual:[NSNull null]])
            self.creatorNickName = nil;

        self.creatorAvatar = [jsonObject objectForKey: @"creatorAvatar"];
        if(self.creatorAvatar && [self.creatorAvatar isEqual:[NSNull null]])
            self.creatorAvatar = nil;

        self.creatorAvatarUrl = [jsonObject objectForKey: @"creatorAvatarUrl"];
        if(self.creatorAvatarUrl && [self.creatorAvatarUrl isEqual:[NSNull null]])
            self.creatorAvatarUrl = nil;

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

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

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

        self.embeddedJson = [jsonObject objectForKey: @"embeddedJson"];
        if(self.embeddedJson && [self.embeddedJson isEqual:[NSNull null]])
            self.embeddedJson = nil;

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

        self.dislikeCount = [jsonObject objectForKey: @"dislikeCount"];
        if(self.dislikeCount && [self.dislikeCount isEqual:[NSNull null]])
            self.dislikeCount = nil;

        self.viewCount = [jsonObject objectForKey: @"viewCount"];
        if(self.viewCount && [self.viewCount isEqual:[NSNull null]])
            self.viewCount = nil;

        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhAttachmentDTO* item = [EvhAttachmentDTO new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        self.assignedFlag = [jsonObject objectForKey: @"assignedFlag"];
        if(self.assignedFlag && [self.assignedFlag isEqual:[NSNull null]])
            self.assignedFlag = nil;

        self.forumName = [jsonObject objectForKey: @"forumName"];
        if(self.forumName && [self.forumName isEqual:[NSNull null]])
            self.forumName = nil;

        self.likeFlag = [jsonObject objectForKey: @"likeFlag"];
        if(self.likeFlag && [self.likeFlag isEqual:[NSNull null]])
            self.likeFlag = nil;

        self.favoriteFlag = [jsonObject objectForKey: @"favoriteFlag"];
        if(self.favoriteFlag && [self.favoriteFlag isEqual:[NSNull null]])
            self.favoriteFlag = nil;

        self.shareUrl = [jsonObject objectForKey: @"shareUrl"];
        if(self.shareUrl && [self.shareUrl isEqual:[NSNull null]])
            self.shareUrl = nil;

        self.privateFlag = [jsonObject objectForKey: @"privateFlag"];
        if(self.privateFlag && [self.privateFlag isEqual:[NSNull null]])
            self.privateFlag = nil;

        self.floorNumber = [jsonObject objectForKey: @"floorNumber"];
        if(self.floorNumber && [self.floorNumber isEqual:[NSNull null]])
            self.floorNumber = nil;

        self.publishStatus = [jsonObject objectForKey: @"publishStatus"];
        if(self.publishStatus && [self.publishStatus isEqual:[NSNull null]])
            self.publishStatus = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
