//
// EvhGroupDTO.m
//
#import "EvhGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupDTO
//

@implementation EvhGroupDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGroupDTO* obj = [EvhGroupDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _memberGroupPrivileges = [NSMutableArray new];
        _memberForumPrivileges = [NSMutableArray new];
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
    if(self.owningForumId)
        [jsonObject setObject: self.owningForumId forKey: @"owningForumId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.privateFlag)
        [jsonObject setObject: self.privateFlag forKey: @"privateFlag"];
    if(self.joinPolicy)
        [jsonObject setObject: self.joinPolicy forKey: @"joinPolicy"];
    if(self.memberCount)
        [jsonObject setObject: self.memberCount forKey: @"memberCount"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
    if(self.categoryName)
        [jsonObject setObject: self.categoryName forKey: @"categoryName"];
    if(self.memberOf)
        [jsonObject setObject: self.memberOf forKey: @"memberOf"];
    if(self.memberStatus)
        [jsonObject setObject: self.memberStatus forKey: @"memberStatus"];
    if(self.memberNickName)
        [jsonObject setObject: self.memberNickName forKey: @"memberNickName"];
    if(self.memberRole)
        [jsonObject setObject: self.memberRole forKey: @"memberRole"];
    if(self.phonePrivateFlag)
        [jsonObject setObject: self.phonePrivateFlag forKey: @"phonePrivateFlag"];
    if(self.muteNotificationFlag)
        [jsonObject setObject: self.muteNotificationFlag forKey: @"muteNotificationFlag"];
    if(self.postFlag)
        [jsonObject setObject: self.postFlag forKey: @"postFlag"];
    if(self.creatorName)
        [jsonObject setObject: self.creatorName forKey: @"creatorName"];
    if(self.creatorFamilyName)
        [jsonObject setObject: self.creatorFamilyName forKey: @"creatorFamilyName"];
    if(self.memberGroupPrivileges) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.memberGroupPrivileges) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"memberGroupPrivileges"];
    }
    if(self.memberForumPrivileges) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.memberForumPrivileges) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"memberForumPrivileges"];
    }
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.discriminator)
        [jsonObject setObject: self.discriminator forKey: @"discriminator"];
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

        self.owningForumId = [jsonObject objectForKey: @"owningForumId"];
        if(self.owningForumId && [self.owningForumId isEqual:[NSNull null]])
            self.owningForumId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.privateFlag = [jsonObject objectForKey: @"privateFlag"];
        if(self.privateFlag && [self.privateFlag isEqual:[NSNull null]])
            self.privateFlag = nil;

        self.joinPolicy = [jsonObject objectForKey: @"joinPolicy"];
        if(self.joinPolicy && [self.joinPolicy isEqual:[NSNull null]])
            self.joinPolicy = nil;

        self.memberCount = [jsonObject objectForKey: @"memberCount"];
        if(self.memberCount && [self.memberCount isEqual:[NSNull null]])
            self.memberCount = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        self.categoryName = [jsonObject objectForKey: @"categoryName"];
        if(self.categoryName && [self.categoryName isEqual:[NSNull null]])
            self.categoryName = nil;

        self.memberOf = [jsonObject objectForKey: @"memberOf"];
        if(self.memberOf && [self.memberOf isEqual:[NSNull null]])
            self.memberOf = nil;

        self.memberStatus = [jsonObject objectForKey: @"memberStatus"];
        if(self.memberStatus && [self.memberStatus isEqual:[NSNull null]])
            self.memberStatus = nil;

        self.memberNickName = [jsonObject objectForKey: @"memberNickName"];
        if(self.memberNickName && [self.memberNickName isEqual:[NSNull null]])
            self.memberNickName = nil;

        self.memberRole = [jsonObject objectForKey: @"memberRole"];
        if(self.memberRole && [self.memberRole isEqual:[NSNull null]])
            self.memberRole = nil;

        self.phonePrivateFlag = [jsonObject objectForKey: @"phonePrivateFlag"];
        if(self.phonePrivateFlag && [self.phonePrivateFlag isEqual:[NSNull null]])
            self.phonePrivateFlag = nil;

        self.muteNotificationFlag = [jsonObject objectForKey: @"muteNotificationFlag"];
        if(self.muteNotificationFlag && [self.muteNotificationFlag isEqual:[NSNull null]])
            self.muteNotificationFlag = nil;

        self.postFlag = [jsonObject objectForKey: @"postFlag"];
        if(self.postFlag && [self.postFlag isEqual:[NSNull null]])
            self.postFlag = nil;

        self.creatorName = [jsonObject objectForKey: @"creatorName"];
        if(self.creatorName && [self.creatorName isEqual:[NSNull null]])
            self.creatorName = nil;

        self.creatorFamilyName = [jsonObject objectForKey: @"creatorFamilyName"];
        if(self.creatorFamilyName && [self.creatorFamilyName isEqual:[NSNull null]])
            self.creatorFamilyName = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"memberGroupPrivileges"];
            for(id itemJson in jsonArray) {
                [self.memberGroupPrivileges addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"memberForumPrivileges"];
            for(id itemJson in jsonArray) {
                [self.memberForumPrivileges addObject: itemJson];
            }
        }
        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.discriminator = [jsonObject objectForKey: @"discriminator"];
        if(self.discriminator && [self.discriminator isEqual:[NSNull null]])
            self.discriminator = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
