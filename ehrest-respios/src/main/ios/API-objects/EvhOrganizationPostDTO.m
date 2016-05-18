//
// EvhOrganizationPostDTO.m
//
#import "EvhOrganizationPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationPostDTO
//

@implementation EvhOrganizationPostDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOrganizationPostDTO* obj = [EvhOrganizationPostDTO new];
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
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.cityName)
        [jsonObject setObject: self.cityName forKey: @"cityName"];
    if(self.orgId)
        [jsonObject setObject: self.orgId forKey: @"orgId"];
    if(self.orgName)
        [jsonObject setObject: self.orgName forKey: @"orgName"];
    if(self.postType)
        [jsonObject setObject: self.postType forKey: @"postType"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.token)
        [jsonObject setObject: self.token forKey: @"token"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.orgType)
        [jsonObject setObject: self.orgType forKey: @"orgType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.cityName = [jsonObject objectForKey: @"cityName"];
        if(self.cityName && [self.cityName isEqual:[NSNull null]])
            self.cityName = nil;

        self.orgId = [jsonObject objectForKey: @"orgId"];
        if(self.orgId && [self.orgId isEqual:[NSNull null]])
            self.orgId = nil;

        self.orgName = [jsonObject objectForKey: @"orgName"];
        if(self.orgName && [self.orgName isEqual:[NSNull null]])
            self.orgName = nil;

        self.postType = [jsonObject objectForKey: @"postType"];
        if(self.postType && [self.postType isEqual:[NSNull null]])
            self.postType = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.token = [jsonObject objectForKey: @"token"];
        if(self.token && [self.token isEqual:[NSNull null]])
            self.token = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.orgType = [jsonObject objectForKey: @"orgType"];
        if(self.orgType && [self.orgType isEqual:[NSNull null]])
            self.orgType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
