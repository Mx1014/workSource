//
// EvhCreateOrganizationByAdminCommand.m
//
#import "EvhCreateOrganizationByAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationByAdminCommand
//

@implementation EvhCreateOrganizationByAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateOrganizationByAdminCommand* obj = [EvhCreateOrganizationByAdminCommand new];
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
    if(self.parentId)
        [jsonObject setObject: self.parentId forKey: @"parentId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.apartmentId)
        [jsonObject setObject: self.apartmentId forKey: @"apartmentId"];
    if(self.OrganizationType)
        [jsonObject setObject: self.OrganizationType forKey: @"OrganizationType"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.apartmentId = [jsonObject objectForKey: @"apartmentId"];
        if(self.apartmentId && [self.apartmentId isEqual:[NSNull null]])
            self.apartmentId = nil;

        self.OrganizationType = [jsonObject objectForKey: @"OrganizationType"];
        if(self.OrganizationType && [self.OrganizationType isEqual:[NSNull null]])
            self.OrganizationType = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
