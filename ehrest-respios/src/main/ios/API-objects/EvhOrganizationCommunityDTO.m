//
// EvhOrganizationCommunityDTO.m
//
#import "EvhOrganizationCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationCommunityDTO
//

@implementation EvhOrganizationCommunityDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationCommunityDTO* obj = [EvhOrganizationCommunityDTO new];
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
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.areaName)
        [jsonObject setObject: self.areaName forKey: @"areaName"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.communityType)
        [jsonObject setObject: self.communityType forKey: @"communityType"];
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

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.areaName = [jsonObject objectForKey: @"areaName"];
        if(self.areaName && [self.areaName isEqual:[NSNull null]])
            self.areaName = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.communityType = [jsonObject objectForKey: @"communityType"];
        if(self.communityType && [self.communityType isEqual:[NSNull null]])
            self.communityType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
