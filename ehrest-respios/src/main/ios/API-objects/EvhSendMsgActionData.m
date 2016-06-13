//
// EvhSendMsgActionData.m
//
#import "EvhSendMsgActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendMsgActionData
//

@implementation EvhSendMsgActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSendMsgActionData* obj = [EvhSendMsgActionData new];
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
    if(self.dstChannel)
        [jsonObject setObject: self.dstChannel forKey: @"dstChannel"];
    if(self.dstChannelId)
        [jsonObject setObject: self.dstChannelId forKey: @"dstChannelId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.dstChannel = [jsonObject objectForKey: @"dstChannel"];
        if(self.dstChannel && [self.dstChannel isEqual:[NSNull null]])
            self.dstChannel = nil;

        self.dstChannelId = [jsonObject objectForKey: @"dstChannelId"];
        if(self.dstChannelId && [self.dstChannelId isEqual:[NSNull null]])
            self.dstChannelId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
