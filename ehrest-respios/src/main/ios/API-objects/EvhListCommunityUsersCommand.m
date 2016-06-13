//
// EvhListCommunityUsersCommand.m
//
#import "EvhListCommunityUsersCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCommunityUsersCommand
//

@implementation EvhListCommunityUsersCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListCommunityUsersCommand* obj = [EvhListCommunityUsersCommand new];
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
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.isAuth)
        [jsonObject setObject: self.isAuth forKey: @"isAuth"];
    if(self.keywords)
        [jsonObject setObject: self.keywords forKey: @"keywords"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.isAuth = [jsonObject objectForKey: @"isAuth"];
        if(self.isAuth && [self.isAuth isEqual:[NSNull null]])
            self.isAuth = nil;

        self.keywords = [jsonObject objectForKey: @"keywords"];
        if(self.keywords && [self.keywords isEqual:[NSNull null]])
            self.keywords = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
