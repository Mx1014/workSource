//
// EvhMessageChannel.m
//
#import "EvhMessageChannel.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMessageChannel
//

@implementation EvhMessageChannel

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhMessageChannel* obj = [EvhMessageChannel new];
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
    if(self.channelType)
        [jsonObject setObject: self.channelType forKey: @"channelType"];
    if(self.channelToken)
        [jsonObject setObject: self.channelToken forKey: @"channelToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.channelType = [jsonObject objectForKey: @"channelType"];
        if(self.channelType && [self.channelType isEqual:[NSNull null]])
            self.channelType = nil;

        self.channelToken = [jsonObject objectForKey: @"channelToken"];
        if(self.channelToken && [self.channelToken isEqual:[NSNull null]])
            self.channelToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
