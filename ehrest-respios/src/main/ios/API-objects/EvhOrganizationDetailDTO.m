//
// EvhOrganizationDetailDTO.m
//
#import "EvhOrganizationDetailDTO.h"
#import "EvhOrganizationMemberDTO.h"
#import "EvhCommunityDTO.h"
#import "EvhAddressAddressDTO.h"
#import "EvhAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationDetailDTO
//

@implementation EvhOrganizationDetailDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationDetailDTO* obj = [EvhOrganizationDetailDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _addresses = [NSMutableArray new];
        _attachments = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.geohash)
        [jsonObject setObject: self.geohash forKey: @"geohash"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.contactor)
        [jsonObject setObject: self.contactor forKey: @"contactor"];
    if(self.memberCount)
        [jsonObject setObject: self.memberCount forKey: @"memberCount"];
    if(self.checkinDate)
        [jsonObject setObject: self.checkinDate forKey: @"checkinDate"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.avatarUri)
        [jsonObject setObject: self.avatarUri forKey: @"avatarUri"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.updateTime)
        [jsonObject setObject: self.updateTime forKey: @"updateTime"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.accountPhone)
        [jsonObject setObject: self.accountPhone forKey: @"accountPhone"];
    if(self.accountName)
        [jsonObject setObject: self.accountName forKey: @"accountName"];
    if(self.assignmentId)
        [jsonObject setObject: self.assignmentId forKey: @"assignmentId"];
    if(self.postUri)
        [jsonObject setObject: self.postUri forKey: @"postUri"];
    if(self.postUrl)
        [jsonObject setObject: self.postUrl forKey: @"postUrl"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.member) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.member toJson: dic];
        
        [jsonObject setObject: dic forKey: @"member"];
    }
    if(self.community) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.community toJson: dic];
        
        [jsonObject setObject: dic forKey: @"community"];
    }
    if(self.addresses) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAddressAddressDTO* item in self.addresses) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"addresses"];
    }
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAttachmentDescriptor* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.geohash = [jsonObject objectForKey: @"geohash"];
        if(self.geohash && [self.geohash isEqual:[NSNull null]])
            self.geohash = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        self.contactor = [jsonObject objectForKey: @"contactor"];
        if(self.contactor && [self.contactor isEqual:[NSNull null]])
            self.contactor = nil;

        self.memberCount = [jsonObject objectForKey: @"memberCount"];
        if(self.memberCount && [self.memberCount isEqual:[NSNull null]])
            self.memberCount = nil;

        self.checkinDate = [jsonObject objectForKey: @"checkinDate"];
        if(self.checkinDate && [self.checkinDate isEqual:[NSNull null]])
            self.checkinDate = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.avatarUri = [jsonObject objectForKey: @"avatarUri"];
        if(self.avatarUri && [self.avatarUri isEqual:[NSNull null]])
            self.avatarUri = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.updateTime = [jsonObject objectForKey: @"updateTime"];
        if(self.updateTime && [self.updateTime isEqual:[NSNull null]])
            self.updateTime = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.accountPhone = [jsonObject objectForKey: @"accountPhone"];
        if(self.accountPhone && [self.accountPhone isEqual:[NSNull null]])
            self.accountPhone = nil;

        self.accountName = [jsonObject objectForKey: @"accountName"];
        if(self.accountName && [self.accountName isEqual:[NSNull null]])
            self.accountName = nil;

        self.assignmentId = [jsonObject objectForKey: @"assignmentId"];
        if(self.assignmentId && [self.assignmentId isEqual:[NSNull null]])
            self.assignmentId = nil;

        self.postUri = [jsonObject objectForKey: @"postUri"];
        if(self.postUri && [self.postUri isEqual:[NSNull null]])
            self.postUri = nil;

        self.postUrl = [jsonObject objectForKey: @"postUrl"];
        if(self.postUrl && [self.postUrl isEqual:[NSNull null]])
            self.postUrl = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"member"];

        self.member = [EvhOrganizationMemberDTO new];
        self.member = [self.member fromJson: itemJson];
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"community"];

        self.community = [EvhCommunityDTO new];
        self.community = [self.community fromJson: itemJson];
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"addresses"];
            for(id itemJson in jsonArray) {
                EvhAddressAddressDTO* item = [EvhAddressAddressDTO new];
                
                [item fromJson: itemJson];
                [self.addresses addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhAttachmentDescriptor* item = [EvhAttachmentDescriptor new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
