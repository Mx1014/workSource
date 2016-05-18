//
// EvhSearchGroupTopicAdminCommand.m
//
#import "EvhSearchGroupTopicAdminCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchGroupTopicAdminCommand
//

@implementation EvhSearchGroupTopicAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchGroupTopicAdminCommand* obj = [EvhSearchGroupTopicAdminCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _senderPhones = [NSMutableArray new];
        _senderNickNames = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.senderPhones) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.senderPhones) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"senderPhones"];
    }
    if(self.senderNickNames) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.senderNickNames) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"senderNickNames"];
    }
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"senderPhones"];
            for(id itemJson in jsonArray) {
                [self.senderPhones addObject: itemJson];
            }
        }
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"senderNickNames"];
            for(id itemJson in jsonArray) {
                [self.senderNickNames addObject: itemJson];
            }
        }
        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

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
