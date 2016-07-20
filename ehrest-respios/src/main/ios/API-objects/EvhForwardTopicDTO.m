//
// EvhForwardTopicDTO.m
//
#import "EvhForwardTopicDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhForwardTopicDTO
//

@implementation EvhForwardTopicDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhForwardTopicDTO* obj = [EvhForwardTopicDTO new];
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
    if(self.topicId)
        [jsonObject setObject: self.topicId forKey: @"topicId"];
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.contentUri)
        [jsonObject setObject: self.contentUri forKey: @"contentUri"];
    if(self.contentUrl)
        [jsonObject setObject: self.contentUrl forKey: @"contentUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.topicId = [jsonObject objectForKey: @"topicId"];
        if(self.topicId && [self.topicId isEqual:[NSNull null]])
            self.topicId = nil;

        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.contentUri = [jsonObject objectForKey: @"contentUri"];
        if(self.contentUri && [self.contentUri isEqual:[NSNull null]])
            self.contentUri = nil;

        self.contentUrl = [jsonObject objectForKey: @"contentUrl"];
        if(self.contentUrl && [self.contentUrl isEqual:[NSNull null]])
            self.contentUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
