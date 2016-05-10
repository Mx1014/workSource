//
// EvhChannel.m
//
#import "EvhChannel.h"

///////////////////////////////////////////////////////////////////////////////
// EvhChannel
//

@implementation EvhChannel

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhChannel* obj = [EvhChannel new];
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
    if(self.channelName)
        [jsonObject setObject: self.channelName forKey: @"channelName"];
    if(self.channelId)
        [jsonObject setObject: self.channelId forKey: @"channelId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.channelName = [jsonObject objectForKey: @"channelName"];
        if(self.channelName && [self.channelName isEqual:[NSNull null]])
            self.channelName = nil;

        self.channelId = [jsonObject objectForKey: @"channelId"];
        if(self.channelId && [self.channelId isEqual:[NSNull null]])
            self.channelId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
