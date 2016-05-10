//
// EvhGetTopicCommand.m
//
#import "EvhGetTopicCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetTopicCommand
//

@implementation EvhGetTopicCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetTopicCommand* obj = [EvhGetTopicCommand new];
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
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.topicId)
        [jsonObject setObject: self.topicId forKey: @"topicId"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.option)
        [jsonObject setObject: self.option forKey: @"option"];
    if(self.entrancePrivilege)
        [jsonObject setObject: self.entrancePrivilege forKey: @"entrancePrivilege"];
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

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.option = [jsonObject objectForKey: @"option"];
        if(self.option && [self.option isEqual:[NSNull null]])
            self.option = nil;

        self.entrancePrivilege = [jsonObject objectForKey: @"entrancePrivilege"];
        if(self.entrancePrivilege && [self.entrancePrivilege isEqual:[NSNull null]])
            self.entrancePrivilege = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
