//
// EvhUserCommunityDTO.m
//
#import "EvhUserCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserCommunityDTO
//

@implementation EvhUserCommunityDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserCommunityDTO* obj = [EvhUserCommunityDTO new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.communityName)
        [jsonObject setObject: self.communityName forKey: @"communityName"];
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.communityName = [jsonObject objectForKey: @"communityName"];
        if(self.communityName && [self.communityName isEqual:[NSNull null]])
            self.communityName = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
