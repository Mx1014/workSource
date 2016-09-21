//
// EvhAssginOrgTopicCommand.m
//
#import "EvhAssginOrgTopicCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAssginOrgTopicCommand
//

@implementation EvhAssginOrgTopicCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAssginOrgTopicCommand* obj = [EvhAssginOrgTopicCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.topicId)
        [jsonObject setObject: self.topicId forKey: @"topicId"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.contactPhone)
        [jsonObject setObject: self.contactPhone forKey: @"contactPhone"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.parentId = [jsonObject objectForKey: @"parentId"];
        if(self.parentId && [self.parentId isEqual:[NSNull null]])
            self.parentId = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.topicId = [jsonObject objectForKey: @"topicId"];
        if(self.topicId && [self.topicId isEqual:[NSNull null]])
            self.topicId = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.contactPhone = [jsonObject objectForKey: @"contactPhone"];
        if(self.contactPhone && [self.contactPhone isEqual:[NSNull null]])
            self.contactPhone = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
