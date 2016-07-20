//
// EvhLinkBody.m
//
#import "EvhLinkBody.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLinkBody
//

@implementation EvhLinkBody

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhLinkBody* obj = [EvhLinkBody new];
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
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.coverUrl)
        [jsonObject setObject: self.coverUrl forKey: @"coverUrl"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.actionUrl)
        [jsonObject setObject: self.actionUrl forKey: @"actionUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.coverUrl = [jsonObject objectForKey: @"coverUrl"];
        if(self.coverUrl && [self.coverUrl isEqual:[NSNull null]])
            self.coverUrl = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.actionUrl = [jsonObject objectForKey: @"actionUrl"];
        if(self.actionUrl && [self.actionUrl isEqual:[NSNull null]])
            self.actionUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
