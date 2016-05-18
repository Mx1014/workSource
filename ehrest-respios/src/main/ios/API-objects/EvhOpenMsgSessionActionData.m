//
// EvhOpenMsgSessionActionData.m
//
#import "EvhOpenMsgSessionActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenMsgSessionActionData
//

@implementation EvhOpenMsgSessionActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOpenMsgSessionActionData* obj = [EvhOpenMsgSessionActionData new];
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
    if(self.srcChannel)
        [jsonObject setObject: self.srcChannel forKey: @"srcChannel"];
    if(self.srcChannelId)
        [jsonObject setObject: self.srcChannelId forKey: @"srcChannelId"];
    if(self.senderUid)
        [jsonObject setObject: self.senderUid forKey: @"senderUid"];
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

        self.srcChannel = [jsonObject objectForKey: @"srcChannel"];
        if(self.srcChannel && [self.srcChannel isEqual:[NSNull null]])
            self.srcChannel = nil;

        self.srcChannelId = [jsonObject objectForKey: @"srcChannelId"];
        if(self.srcChannelId && [self.srcChannelId isEqual:[NSNull null]])
            self.srcChannelId = nil;

        self.senderUid = [jsonObject objectForKey: @"senderUid"];
        if(self.senderUid && [self.senderUid isEqual:[NSNull null]])
            self.senderUid = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
