//
// EvhMakeTopCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import "EvhMakeTopCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMakeTopCommand
//

@implementation EvhMakeTopCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhMakeTopCommand* obj = [EvhMakeTopCommand new];
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
    if(self.topFlag)
        [jsonObject setObject: self.topFlag forKey: @"topFlag"];
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

        self.topFlag = [jsonObject objectForKey: @"topFlag"];
        if(self.topFlag && [self.topFlag isEqual:[NSNull null]])
            self.topFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
