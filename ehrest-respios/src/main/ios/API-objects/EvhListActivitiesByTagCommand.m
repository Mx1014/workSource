//
// EvhListActivitiesByTagCommand.m
//
#import "EvhListActivitiesByTagCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListActivitiesByTagCommand
//

@implementation EvhListActivitiesByTagCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListActivitiesByTagCommand* obj = [EvhListActivitiesByTagCommand new];
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
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
    if(self.community_id)
        [jsonObject setObject: self.community_id forKey: @"community_id"];
    if(self.anchor)
        [jsonObject setObject: self.anchor forKey: @"anchor"];
    if(self.range)
        [jsonObject setObject: self.range forKey: @"range"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        self.community_id = [jsonObject objectForKey: @"community_id"];
        if(self.community_id && [self.community_id isEqual:[NSNull null]])
            self.community_id = nil;

        self.anchor = [jsonObject objectForKey: @"anchor"];
        if(self.anchor && [self.anchor isEqual:[NSNull null]])
            self.anchor = nil;

        self.range = [jsonObject objectForKey: @"range"];
        if(self.range && [self.range isEqual:[NSNull null]])
            self.range = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
