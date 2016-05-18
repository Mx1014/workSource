//
// EvhSearchByMultiForumAndCmntyCommand.m
//
#import "EvhSearchByMultiForumAndCmntyCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchByMultiForumAndCmntyCommand
//

@implementation EvhSearchByMultiForumAndCmntyCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchByMultiForumAndCmntyCommand* obj = [EvhSearchByMultiForumAndCmntyCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _forumIds = [NSMutableArray new];
        _communityIds = [NSMutableArray new];
        _regionIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.queryString)
        [jsonObject setObject: self.queryString forKey: @"queryString"];
    if(self.forumIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.forumIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"forumIds"];
    }
    if(self.communityIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.communityIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"communityIds"];
    }
    if(self.regionIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.regionIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"regionIds"];
    }
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.queryString = [jsonObject objectForKey: @"queryString"];
        if(self.queryString && [self.queryString isEqual:[NSNull null]])
            self.queryString = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"forumIds"];
            for(id itemJson in jsonArray) {
                [self.forumIds addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"communityIds"];
            for(id itemJson in jsonArray) {
                [self.communityIds addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"regionIds"];
            for(id itemJson in jsonArray) {
                [self.regionIds addObject: itemJson];
            }
        }
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
