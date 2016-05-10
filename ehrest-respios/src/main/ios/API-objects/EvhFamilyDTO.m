//
// EvhFamilyDTO.m
//
#import "EvhFamilyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyDTO
//

@implementation EvhFamilyDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFamilyDTO* obj = [EvhFamilyDTO new];
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
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.areaId)
        [jsonObject setObject: self.areaId forKey: @"areaId"];
    if(self.areaName)
        [jsonObject setObject: self.areaName forKey: @"areaName"];
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
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.apartmentName)
        [jsonObject setObject: self.apartmentName forKey: @"apartmentName"];
    if(self.addressStatus)
        [jsonObject setObject: self.addressStatus forKey: @"addressStatus"];
    if(self.proofResourceUri)
        [jsonObject setObject: self.proofResourceUri forKey: @"proofResourceUri"];
    if(self.proofResourceUrl)
        [jsonObject setObject: self.proofResourceUrl forKey: @"proofResourceUrl"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.communityType)
        [jsonObject setObject: self.communityType forKey: @"communityType"];
    if(self.defaultForumId)
        [jsonObject setObject: self.defaultForumId forKey: @"defaultForumId"];
    if(self.feedbackForumId)
        [jsonObject setObject: self.feedbackForumId forKey: @"feedbackForumId"];
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

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.areaId = [jsonObject objectForKey: @"areaId"];
        if(self.areaId && [self.areaId isEqual:[NSNull null]])
            self.areaId = nil;

        self.areaName = [jsonObject objectForKey: @"areaName"];
        if(self.areaName && [self.areaName isEqual:[NSNull null]])
            self.areaName = nil;

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

        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.apartmentName = [jsonObject objectForKey: @"apartmentName"];
        if(self.apartmentName && [self.apartmentName isEqual:[NSNull null]])
            self.apartmentName = nil;

        self.addressStatus = [jsonObject objectForKey: @"addressStatus"];
        if(self.addressStatus && [self.addressStatus isEqual:[NSNull null]])
            self.addressStatus = nil;

        self.proofResourceUri = [jsonObject objectForKey: @"proofResourceUri"];
        if(self.proofResourceUri && [self.proofResourceUri isEqual:[NSNull null]])
            self.proofResourceUri = nil;

        self.proofResourceUrl = [jsonObject objectForKey: @"proofResourceUrl"];
        if(self.proofResourceUrl && [self.proofResourceUrl isEqual:[NSNull null]])
            self.proofResourceUrl = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.communityType = [jsonObject objectForKey: @"communityType"];
        if(self.communityType && [self.communityType isEqual:[NSNull null]])
            self.communityType = nil;

        self.defaultForumId = [jsonObject objectForKey: @"defaultForumId"];
        if(self.defaultForumId && [self.defaultForumId isEqual:[NSNull null]])
            self.defaultForumId = nil;

        self.feedbackForumId = [jsonObject objectForKey: @"feedbackForumId"];
        if(self.feedbackForumId && [self.feedbackForumId isEqual:[NSNull null]])
            self.feedbackForumId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
