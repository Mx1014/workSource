//
// EvhOrganizationMemberDetailDTO.m
//
#import "EvhOrganizationMemberDetailDTO.h"
#import "EvhOrganizationDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationMemberDetailDTO
//

@implementation EvhOrganizationMemberDetailDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationMemberDetailDTO* obj = [EvhOrganizationMemberDetailDTO new];
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
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.avatar)
        [jsonObject setObject: self.avatar forKey: @"avatar"];
    if(self.employeeNo)
        [jsonObject setObject: self.employeeNo forKey: @"employeeNo"];
    if(self.gender)
        [jsonObject setObject: self.gender forKey: @"gender"];
    if(self.organizationDetailDTO) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.organizationDetailDTO toJson: dic];
        
        [jsonObject setObject: dic forKey: @"organizationDetailDTO"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

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

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.groupName = [jsonObject objectForKey: @"groupName"];
        if(self.groupName && [self.groupName isEqual:[NSNull null]])
            self.groupName = nil;

        self.avatar = [jsonObject objectForKey: @"avatar"];
        if(self.avatar && [self.avatar isEqual:[NSNull null]])
            self.avatar = nil;

        self.employeeNo = [jsonObject objectForKey: @"employeeNo"];
        if(self.employeeNo && [self.employeeNo isEqual:[NSNull null]])
            self.employeeNo = nil;

        self.gender = [jsonObject objectForKey: @"gender"];
        if(self.gender && [self.gender isEqual:[NSNull null]])
            self.gender = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"organizationDetailDTO"];

        self.organizationDetailDTO = [EvhOrganizationDetailDTO new];
        self.organizationDetailDTO = [self.organizationDetailDTO fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
