//
// EvhCommunityBuildingDTO.m
//
#import "EvhCommunityBuildingDTO.h"
#import "EvhBuildingAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityBuildingDTO
//

@implementation EvhCommunityBuildingDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCommunityBuildingDTO* obj = [EvhCommunityBuildingDTO new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.aliasName)
        [jsonObject setObject: self.aliasName forKey: @"aliasName"];
    if(self.managerUid)
        [jsonObject setObject: self.managerUid forKey: @"managerUid"];
    if(self.managerNickName)
        [jsonObject setObject: self.managerNickName forKey: @"managerNickName"];
    if(self.managerAvatar)
        [jsonObject setObject: self.managerAvatar forKey: @"managerAvatar"];
    if(self.managerAvatarUrl)
        [jsonObject setObject: self.managerAvatarUrl forKey: @"managerAvatarUrl"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.areaSize)
        [jsonObject setObject: self.areaSize forKey: @"areaSize"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.posterUri)
        [jsonObject setObject: self.posterUri forKey: @"posterUri"];
    if(self.posterUrl)
        [jsonObject setObject: self.posterUrl forKey: @"posterUrl"];
    if(self.operatorUid)
        [jsonObject setObject: self.operatorUid forKey: @"operatorUid"];
    if(self.operateTime)
        [jsonObject setObject: self.operateTime forKey: @"operateTime"];
    if(self.operateNickName)
        [jsonObject setObject: self.operateNickName forKey: @"operateNickName"];
    if(self.operateAvatar)
        [jsonObject setObject: self.operateAvatar forKey: @"operateAvatar"];
    if(self.operateAvatarUrl)
        [jsonObject setObject: self.operateAvatarUrl forKey: @"operateAvatarUrl"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.creatorNickName)
        [jsonObject setObject: self.creatorNickName forKey: @"creatorNickName"];
    if(self.creatorAvatar)
        [jsonObject setObject: self.creatorAvatar forKey: @"creatorAvatar"];
    if(self.creatorAvatarUrl)
        [jsonObject setObject: self.creatorAvatarUrl forKey: @"creatorAvatarUrl"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhBuildingAttachmentDTO* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.aliasName = [jsonObject objectForKey: @"aliasName"];
        if(self.aliasName && [self.aliasName isEqual:[NSNull null]])
            self.aliasName = nil;

        self.managerUid = [jsonObject objectForKey: @"managerUid"];
        if(self.managerUid && [self.managerUid isEqual:[NSNull null]])
            self.managerUid = nil;

        self.managerNickName = [jsonObject objectForKey: @"managerNickName"];
        if(self.managerNickName && [self.managerNickName isEqual:[NSNull null]])
            self.managerNickName = nil;

        self.managerAvatar = [jsonObject objectForKey: @"managerAvatar"];
        if(self.managerAvatar && [self.managerAvatar isEqual:[NSNull null]])
            self.managerAvatar = nil;

        self.managerAvatarUrl = [jsonObject objectForKey: @"managerAvatarUrl"];
        if(self.managerAvatarUrl && [self.managerAvatarUrl isEqual:[NSNull null]])
            self.managerAvatarUrl = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.areaSize = [jsonObject objectForKey: @"areaSize"];
        if(self.areaSize && [self.areaSize isEqual:[NSNull null]])
            self.areaSize = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.posterUri = [jsonObject objectForKey: @"posterUri"];
        if(self.posterUri && [self.posterUri isEqual:[NSNull null]])
            self.posterUri = nil;

        self.posterUrl = [jsonObject objectForKey: @"posterUrl"];
        if(self.posterUrl && [self.posterUrl isEqual:[NSNull null]])
            self.posterUrl = nil;

        self.operatorUid = [jsonObject objectForKey: @"operatorUid"];
        if(self.operatorUid && [self.operatorUid isEqual:[NSNull null]])
            self.operatorUid = nil;

        self.operateTime = [jsonObject objectForKey: @"operateTime"];
        if(self.operateTime && [self.operateTime isEqual:[NSNull null]])
            self.operateTime = nil;

        self.operateNickName = [jsonObject objectForKey: @"operateNickName"];
        if(self.operateNickName && [self.operateNickName isEqual:[NSNull null]])
            self.operateNickName = nil;

        self.operateAvatar = [jsonObject objectForKey: @"operateAvatar"];
        if(self.operateAvatar && [self.operateAvatar isEqual:[NSNull null]])
            self.operateAvatar = nil;

        self.operateAvatarUrl = [jsonObject objectForKey: @"operateAvatarUrl"];
        if(self.operateAvatarUrl && [self.operateAvatarUrl isEqual:[NSNull null]])
            self.operateAvatarUrl = nil;

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

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhBuildingAttachmentDTO* item = [EvhBuildingAttachmentDTO new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
