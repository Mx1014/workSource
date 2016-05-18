//
// EvhOrganizationDTO.m
//
#import "EvhOrganizationDTO.h"
#import "EvhOrganizationDTO.h"
#import "EvhRoleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationDTO
//

@implementation EvhOrganizationDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationDTO* obj = [EvhOrganizationDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _childrens = [NSMutableArray new];
        _roles = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.parentName)
        [jsonObject setObject: self.parentName forKey: @"parentName"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.path)
        [jsonObject setObject: self.path forKey: @"path"];
    if(self.level)
        [jsonObject setObject: self.level forKey: @"level"];
    if(self.addressId)
        [jsonObject setObject: self.addressId forKey: @"addressId"];
    if(self.OrganizationType)
        [jsonObject setObject: self.OrganizationType forKey: @"OrganizationType"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.memberStatus)
        [jsonObject setObject: self.memberStatus forKey: @"memberStatus"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.groupType)
        [jsonObject setObject: self.groupType forKey: @"groupType"];
    if(self.directlyEnterpriseId)
        [jsonObject setObject: self.directlyEnterpriseId forKey: @"directlyEnterpriseId"];
    if(self.avatarUri)
        [jsonObject setObject: self.avatarUri forKey: @"avatarUri"];
    if(self.avatarUrl)
        [jsonObject setObject: self.avatarUrl forKey: @"avatarUrl"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
    if(self.communityType)
        [jsonObject setObject: self.communityType forKey: @"communityType"];
    if(self.defaultForumId)
        [jsonObject setObject: self.defaultForumId forKey: @"defaultForumId"];
    if(self.feedbackForumId)
        [jsonObject setObject: self.feedbackForumId forKey: @"feedbackForumId"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.showFlag)
        [jsonObject setObject: self.showFlag forKey: @"showFlag"];
    if(self.childrens) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhOrganizationDTO* item in self.childrens) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"childrens"];
    }
    if(self.roles) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRoleDTO* item in self.roles) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"roles"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.parentName = [jsonObject objectForKey: @"parentName"];
        if(self.parentName && [self.parentName isEqual:[NSNull null]])
            self.parentName = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.path = [jsonObject objectForKey: @"path"];
        if(self.path && [self.path isEqual:[NSNull null]])
            self.path = nil;

        self.level = [jsonObject objectForKey: @"level"];
        if(self.level && [self.level isEqual:[NSNull null]])
            self.level = nil;

        self.addressId = [jsonObject objectForKey: @"addressId"];
        if(self.addressId && [self.addressId isEqual:[NSNull null]])
            self.addressId = nil;

        self.OrganizationType = [jsonObject objectForKey: @"OrganizationType"];
        if(self.OrganizationType && [self.OrganizationType isEqual:[NSNull null]])
            self.OrganizationType = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.memberStatus = [jsonObject objectForKey: @"memberStatus"];
        if(self.memberStatus && [self.memberStatus isEqual:[NSNull null]])
            self.memberStatus = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.groupType = [jsonObject objectForKey: @"groupType"];
        if(self.groupType && [self.groupType isEqual:[NSNull null]])
            self.groupType = nil;

        self.directlyEnterpriseId = [jsonObject objectForKey: @"directlyEnterpriseId"];
        if(self.directlyEnterpriseId && [self.directlyEnterpriseId isEqual:[NSNull null]])
            self.directlyEnterpriseId = nil;

        self.avatarUri = [jsonObject objectForKey: @"avatarUri"];
        if(self.avatarUri && [self.avatarUri isEqual:[NSNull null]])
            self.avatarUri = nil;

        self.avatarUrl = [jsonObject objectForKey: @"avatarUrl"];
        if(self.avatarUrl && [self.avatarUrl isEqual:[NSNull null]])
            self.avatarUrl = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        self.communityType = [jsonObject objectForKey: @"communityType"];
        if(self.communityType && [self.communityType isEqual:[NSNull null]])
            self.communityType = nil;

        self.defaultForumId = [jsonObject objectForKey: @"defaultForumId"];
        if(self.defaultForumId && [self.defaultForumId isEqual:[NSNull null]])
            self.defaultForumId = nil;

        self.feedbackForumId = [jsonObject objectForKey: @"feedbackForumId"];
        if(self.feedbackForumId && [self.feedbackForumId isEqual:[NSNull null]])
            self.feedbackForumId = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.showFlag = [jsonObject objectForKey: @"showFlag"];
        if(self.showFlag && [self.showFlag isEqual:[NSNull null]])
            self.showFlag = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"childrens"];
            for(id itemJson in jsonArray) {
                EvhOrganizationDTO* item = [EvhOrganizationDTO new];
                
                [item fromJson: itemJson];
                [self.childrens addObject: item];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"roles"];
            for(id itemJson in jsonArray) {
                EvhRoleDTO* item = [EvhRoleDTO new];
                
                [item fromJson: itemJson];
                [self.roles addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
