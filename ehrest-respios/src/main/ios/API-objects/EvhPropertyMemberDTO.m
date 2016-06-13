//
// EvhPropertyMemberDTO.m
//
#import "EvhPropertyMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropertyMemberDTO
//

@implementation EvhPropertyMemberDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropertyMemberDTO* obj = [EvhPropertyMemberDTO new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.organizationName)
        [jsonObject setObject: self.organizationName forKey: @"organizationName"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.pmGroup)
        [jsonObject setObject: self.pmGroup forKey: @"pmGroup"];
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

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.organizationName = [jsonObject objectForKey: @"organizationName"];
        if(self.organizationName && [self.organizationName isEqual:[NSNull null]])
            self.organizationName = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.pmGroup = [jsonObject objectForKey: @"pmGroup"];
        if(self.pmGroup && [self.pmGroup isEqual:[NSNull null]])
            self.pmGroup = nil;

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

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
