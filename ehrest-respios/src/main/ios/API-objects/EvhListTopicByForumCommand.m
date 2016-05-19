//
// EvhListTopicByForumCommand.m
//
#import "EvhListTopicByForumCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListTopicByForumCommand
//

@implementation EvhListTopicByForumCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListTopicByForumCommand* obj = [EvhListTopicByForumCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _forumIdList = [NSMutableArray new];
        _excludeCategories = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.forumIdList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.forumIdList) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"forumIdList"];
    }
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
    if(self.excludeCategories) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.excludeCategories) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"excludeCategories"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"forumIdList"];
            for(id itemJson in jsonArray) {
                [self.forumIdList addObject: itemJson];
            }
        }
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"excludeCategories"];
            for(id itemJson in jsonArray) {
                [self.excludeCategories addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
