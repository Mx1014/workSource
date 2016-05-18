//
// EvhFleaMarketUpdateCommand.m
//
#import "EvhFleaMarketUpdateCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFleaMarketUpdateCommand
//

@implementation EvhFleaMarketUpdateCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFleaMarketUpdateCommand* obj = [EvhFleaMarketUpdateCommand new];
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
    if(self.barterFlag)
        [jsonObject setObject: self.barterFlag forKey: @"barterFlag"];
    if(self.price)
        [jsonObject setObject: self.price forKey: @"price"];
    if(self.closeFlag)
        [jsonObject setObject: self.closeFlag forKey: @"closeFlag"];
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

        self.barterFlag = [jsonObject objectForKey: @"barterFlag"];
        if(self.barterFlag && [self.barterFlag isEqual:[NSNull null]])
            self.barterFlag = nil;

        self.price = [jsonObject objectForKey: @"price"];
        if(self.price && [self.price isEqual:[NSNull null]])
            self.price = nil;

        self.closeFlag = [jsonObject objectForKey: @"closeFlag"];
        if(self.closeFlag && [self.closeFlag isEqual:[NSNull null]])
            self.closeFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
