//
// EvhOrganizationMemberDTO.m
//
#import "EvhOrganizationMemberDTO.h"
#import "EvhRoleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationMemberDTO
//

@implementation EvhOrganizationMemberDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationMemberDTO* obj = [EvhOrganizationMemberDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _roles = [NSMutableArray new];
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
    if(self.organizationName)
        [jsonObject setObject: self.organizationName forKey: @"organizationName"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.memberGroup)
        [jsonObject setObject: self.memberGroup forKey: @"memberGroup"];
    if(self.contactName)
        [jsonObject setObject: self.contactName forKey: @"contactName"];
    if(self.contactType)
        [jsonObject setObject: self.contactType forKey: @"contactType"];
    if(self.contactToken)
        [jsonObject setObject: self.contactToken forKey: @"contactToken"];
    if(self.contactDescription)
        [jsonObject setObject: self.contactDescription forKey: @"contactDescription"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.initial)
        [jsonObject setObject: self.initial forKey: @"initial"];
    if(self.fullPinyin)
        [jsonObject setObject: self.fullPinyin forKey: @"fullPinyin"];
    if(self.fullInitial)
        [jsonObject setObject: self.fullInitial forKey: @"fullInitial"];
    if(self.roles) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhRoleDTO* item in self.roles) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"roles"];
    }
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.employeeNo)
        [jsonObject setObject: self.employeeNo forKey: @"employeeNo"];
    if(self.gender)
        [jsonObject setObject: self.gender forKey: @"gender"];
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

        self.organizationName = [jsonObject objectForKey: @"organizationName"];
        if(self.organizationName && [self.organizationName isEqual:[NSNull null]])
            self.organizationName = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.memberGroup = [jsonObject objectForKey: @"memberGroup"];
        if(self.memberGroup && [self.memberGroup isEqual:[NSNull null]])
            self.memberGroup = nil;

        self.contactName = [jsonObject objectForKey: @"contactName"];
        if(self.contactName && [self.contactName isEqual:[NSNull null]])
            self.contactName = nil;

        self.contactType = [jsonObject objectForKey: @"contactType"];
        if(self.contactType && [self.contactType isEqual:[NSNull null]])
            self.contactType = nil;

        self.contactToken = [jsonObject objectForKey: @"contactToken"];
        if(self.contactToken && [self.contactToken isEqual:[NSNull null]])
            self.contactToken = nil;

        self.contactDescription = [jsonObject objectForKey: @"contactDescription"];
        if(self.contactDescription && [self.contactDescription isEqual:[NSNull null]])
            self.contactDescription = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.initial = [jsonObject objectForKey: @"initial"];
        if(self.initial && [self.initial isEqual:[NSNull null]])
            self.initial = nil;

        self.fullPinyin = [jsonObject objectForKey: @"fullPinyin"];
        if(self.fullPinyin && [self.fullPinyin isEqual:[NSNull null]])
            self.fullPinyin = nil;

        self.fullInitial = [jsonObject objectForKey: @"fullInitial"];
        if(self.fullInitial && [self.fullInitial isEqual:[NSNull null]])
            self.fullInitial = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"roles"];
            for(id itemJson in jsonArray) {
                EvhRoleDTO* item = [EvhRoleDTO new];
                
                [item fromJson: itemJson];
                [self.roles addObject: item];
            }
        }
        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.groupName = [jsonObject objectForKey: @"groupName"];
        if(self.groupName && [self.groupName isEqual:[NSNull null]])
            self.groupName = nil;

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.employeeNo = [jsonObject objectForKey: @"employeeNo"];
        if(self.employeeNo && [self.employeeNo isEqual:[NSNull null]])
            self.employeeNo = nil;

        self.gender = [jsonObject objectForKey: @"gender"];
        if(self.gender && [self.gender isEqual:[NSNull null]])
            self.gender = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
