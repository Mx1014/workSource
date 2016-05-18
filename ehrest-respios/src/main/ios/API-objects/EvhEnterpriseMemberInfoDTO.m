//
// EvhEnterpriseMemberInfoDTO.m
//
#import "EvhEnterpriseMemberInfoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseMemberInfoDTO
//

@implementation EvhEnterpriseMemberInfoDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseMemberInfoDTO* obj = [EvhEnterpriseMemberInfoDTO new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.avatarUri)
        [jsonObject setObject: self.avatarUri forKey: @"avatarUri"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.memberCount)
        [jsonObject setObject: self.memberCount forKey: @"memberCount"];
    if(self.membershipStatus)
        [jsonObject setObject: self.membershipStatus forKey: @"membershipStatus"];
    if(self.primaryFlag)
        [jsonObject setObject: self.primaryFlag forKey: @"primaryFlag"];
    if(self.adminStatus)
        [jsonObject setObject: self.adminStatus forKey: @"adminStatus"];
    if(self.memberUid)
        [jsonObject setObject: self.memberUid forKey: @"memberUid"];
    if(self.memberNickName)
        [jsonObject setObject: self.memberNickName forKey: @"memberNickName"];
    if(self.memberAvatarUri)
        [jsonObject setObject: self.memberAvatarUri forKey: @"memberAvatarUri"];
    if(self.memberAvatarUrl)
        [jsonObject setObject: self.memberAvatarUrl forKey: @"memberAvatarUrl"];
    if(self.cellPhone)
        [jsonObject setObject: self.cellPhone forKey: @"cellPhone"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        self.avatarUri = [jsonObject objectForKey: @"avatarUri"];
        if(self.avatarUri && [self.avatarUri isEqual:[NSNull null]])
            self.avatarUri = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.memberCount = [jsonObject objectForKey: @"memberCount"];
        if(self.memberCount && [self.memberCount isEqual:[NSNull null]])
            self.memberCount = nil;

        self.membershipStatus = [jsonObject objectForKey: @"membershipStatus"];
        if(self.membershipStatus && [self.membershipStatus isEqual:[NSNull null]])
            self.membershipStatus = nil;

        self.primaryFlag = [jsonObject objectForKey: @"primaryFlag"];
        if(self.primaryFlag && [self.primaryFlag isEqual:[NSNull null]])
            self.primaryFlag = nil;

        self.adminStatus = [jsonObject objectForKey: @"adminStatus"];
        if(self.adminStatus && [self.adminStatus isEqual:[NSNull null]])
            self.adminStatus = nil;

        self.memberUid = [jsonObject objectForKey: @"memberUid"];
        if(self.memberUid && [self.memberUid isEqual:[NSNull null]])
            self.memberUid = nil;

        self.memberNickName = [jsonObject objectForKey: @"memberNickName"];
        if(self.memberNickName && [self.memberNickName isEqual:[NSNull null]])
            self.memberNickName = nil;

        self.memberAvatarUri = [jsonObject objectForKey: @"memberAvatarUri"];
        if(self.memberAvatarUri && [self.memberAvatarUri isEqual:[NSNull null]])
            self.memberAvatarUri = nil;

        self.memberAvatarUrl = [jsonObject objectForKey: @"memberAvatarUrl"];
        if(self.memberAvatarUrl && [self.memberAvatarUrl isEqual:[NSNull null]])
            self.memberAvatarUrl = nil;

        self.cellPhone = [jsonObject objectForKey: @"cellPhone"];
        if(self.cellPhone && [self.cellPhone isEqual:[NSNull null]])
            self.cellPhone = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
