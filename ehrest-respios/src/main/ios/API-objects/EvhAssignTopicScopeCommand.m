//
// EvhAssignTopicScopeCommand.m
//
#import "EvhAssignTopicScopeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAssignTopicScopeCommand
//

@implementation EvhAssignTopicScopeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAssignTopicScopeCommand* obj = [EvhAssignTopicScopeCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _scopeIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.topicId)
        [jsonObject setObject: self.topicId forKey: @"topicId"];
    if(self.assignedFlag)
        [jsonObject setObject: self.assignedFlag forKey: @"assignedFlag"];
    if(self.scopeCode)
        [jsonObject setObject: self.scopeCode forKey: @"scopeCode"];
    if(self.scopeIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.scopeIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"scopeIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.topicId = [jsonObject objectForKey: @"topicId"];
        if(self.topicId && [self.topicId isEqual:[NSNull null]])
            self.topicId = nil;

        self.assignedFlag = [jsonObject objectForKey: @"assignedFlag"];
        if(self.assignedFlag && [self.assignedFlag isEqual:[NSNull null]])
            self.assignedFlag = nil;

        self.scopeCode = [jsonObject objectForKey: @"scopeCode"];
        if(self.scopeCode && [self.scopeCode isEqual:[NSNull null]])
            self.scopeCode = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"scopeIds"];
            for(id itemJson in jsonArray) {
                [self.scopeIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
