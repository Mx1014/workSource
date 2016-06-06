//
// EvhEnterpriseDTO.m
//
#import "EvhEnterpriseDTO.h"
#import "EvhAddressAddressDTO.h"
#import "EvhEnterpriseAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseDTO
//

@implementation EvhEnterpriseDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseDTO* obj = [EvhEnterpriseDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _contactGroupPrivileges = [NSMutableArray new];
        _contactForumPrivileges = [NSMutableArray new];
        _address = [NSMutableArray new];
        _attachments = [NSMutableArray new];
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
    if(self.contactCount)
        [jsonObject setObject: self.contactCount forKey: @"contactCount"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.owningForumId)
        [jsonObject setObject: self.owningForumId forKey: @"owningForumId"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
    if(self.contactOf)
        [jsonObject setObject: self.contactOf forKey: @"contactOf"];
    if(self.contactStatus)
        [jsonObject setObject: self.contactStatus forKey: @"contactStatus"];
    if(self.contactNickName)
        [jsonObject setObject: self.contactNickName forKey: @"contactNickName"];
    if(self.contactRole)
        [jsonObject setObject: self.contactRole forKey: @"contactRole"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.creatorName)
        [jsonObject setObject: self.creatorName forKey: @"creatorName"];
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
    if(self.contactGroupPrivileges) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.contactGroupPrivileges) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"contactGroupPrivileges"];
    }
    if(self.contactForumPrivileges) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.contactForumPrivileges) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"contactForumPrivileges"];
    }
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.contactsPhone)
        [jsonObject setObject: self.contactsPhone forKey: @"contactsPhone"];
    if(self.contactor)
        [jsonObject setObject: self.contactor forKey: @"contactor"];
    if(self.entries)
        [jsonObject setObject: self.entries forKey: @"entries"];
    if(self.enterpriseCheckinDate)
        [jsonObject setObject: self.enterpriseCheckinDate forKey: @"enterpriseCheckinDate"];
    if(self.enterpriseAddress)
        [jsonObject setObject: self.enterpriseAddress forKey: @"enterpriseAddress"];
    if(self.postUri)
        [jsonObject setObject: self.postUri forKey: @"postUri"];
    if(self.postUrl)
        [jsonObject setObject: self.postUrl forKey: @"postUrl"];
    if(self.communityType)
        [jsonObject setObject: self.communityType forKey: @"communityType"];
    if(self.defaultForumId)
        [jsonObject setObject: self.defaultForumId forKey: @"defaultForumId"];
    if(self.feedbackForumId)
        [jsonObject setObject: self.feedbackForumId forKey: @"feedbackForumId"];
    if(self.address) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAddressAddressDTO* item in self.address) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"address"];
    }
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhEnterpriseAttachmentDTO* item in self.attachments) {
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

        self.contactCount = [jsonObject objectForKey: @"contactCount"];
        if(self.contactCount && [self.contactCount isEqual:[NSNull null]])
            self.contactCount = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.owningForumId = [jsonObject objectForKey: @"owningForumId"];
        if(self.owningForumId && [self.owningForumId isEqual:[NSNull null]])
            self.owningForumId = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        self.contactOf = [jsonObject objectForKey: @"contactOf"];
        if(self.contactOf && [self.contactOf isEqual:[NSNull null]])
            self.contactOf = nil;

        self.contactStatus = [jsonObject objectForKey: @"contactStatus"];
        if(self.contactStatus && [self.contactStatus isEqual:[NSNull null]])
            self.contactStatus = nil;

        self.contactNickName = [jsonObject objectForKey: @"contactNickName"];
        if(self.contactNickName && [self.contactNickName isEqual:[NSNull null]])
            self.contactNickName = nil;

        self.contactRole = [jsonObject objectForKey: @"contactRole"];
        if(self.contactRole && [self.contactRole isEqual:[NSNull null]])
            self.contactRole = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.creatorName = [jsonObject objectForKey: @"creatorName"];
        if(self.creatorName && [self.creatorName isEqual:[NSNull null]])
            self.creatorName = nil;

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

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"contactGroupPrivileges"];
            for(id itemJson in jsonArray) {
                [self.contactGroupPrivileges addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"contactForumPrivileges"];
            for(id itemJson in jsonArray) {
                [self.contactForumPrivileges addObject: itemJson];
            }
        }
        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.contactsPhone = [jsonObject objectForKey: @"contactsPhone"];
        if(self.contactsPhone && [self.contactsPhone isEqual:[NSNull null]])
            self.contactsPhone = nil;

        self.contactor = [jsonObject objectForKey: @"contactor"];
        if(self.contactor && [self.contactor isEqual:[NSNull null]])
            self.contactor = nil;

        self.entries = [jsonObject objectForKey: @"entries"];
        if(self.entries && [self.entries isEqual:[NSNull null]])
            self.entries = nil;

        self.enterpriseCheckinDate = [jsonObject objectForKey: @"enterpriseCheckinDate"];
        if(self.enterpriseCheckinDate && [self.enterpriseCheckinDate isEqual:[NSNull null]])
            self.enterpriseCheckinDate = nil;

        self.enterpriseAddress = [jsonObject objectForKey: @"enterpriseAddress"];
        if(self.enterpriseAddress && [self.enterpriseAddress isEqual:[NSNull null]])
            self.enterpriseAddress = nil;

        self.postUri = [jsonObject objectForKey: @"postUri"];
        if(self.postUri && [self.postUri isEqual:[NSNull null]])
            self.postUri = nil;

        self.postUrl = [jsonObject objectForKey: @"postUrl"];
        if(self.postUrl && [self.postUrl isEqual:[NSNull null]])
            self.postUrl = nil;

        self.communityType = [jsonObject objectForKey: @"communityType"];
        if(self.communityType && [self.communityType isEqual:[NSNull null]])
            self.communityType = nil;

        self.defaultForumId = [jsonObject objectForKey: @"defaultForumId"];
        if(self.defaultForumId && [self.defaultForumId isEqual:[NSNull null]])
            self.defaultForumId = nil;

        self.feedbackForumId = [jsonObject objectForKey: @"feedbackForumId"];
        if(self.feedbackForumId && [self.feedbackForumId isEqual:[NSNull null]])
            self.feedbackForumId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"address"];
            for(id itemJson in jsonArray) {
                EvhAddressAddressDTO* item = [EvhAddressAddressDTO new];
                
                [item fromJson: itemJson];
                [self.address addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhEnterpriseAttachmentDTO* item = [EvhEnterpriseAttachmentDTO new];
                
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
