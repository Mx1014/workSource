//
// EvhDoorMessageResp.m
//
#import "EvhDoorMessageResp.h"
#import "EvhAclinkMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorMessageResp
//

@implementation EvhDoorMessageResp

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDoorMessageResp* obj = [EvhDoorMessageResp new];
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
    if(self.seq)
        [jsonObject setObject: self.seq forKey: @"seq"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.messageType)
        [jsonObject setObject: self.messageType forKey: @"messageType"];
    if(self.body) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.body toJson: dic];
        
        [jsonObject setObject: dic forKey: @"body"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.seq = [jsonObject objectForKey: @"seq"];
        if(self.seq && [self.seq isEqual:[NSNull null]])
            self.seq = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.messageType = [jsonObject objectForKey: @"messageType"];
        if(self.messageType && [self.messageType isEqual:[NSNull null]])
            self.messageType = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"body"];

        self.body = [EvhAclinkMessage new];
        self.body = [self.body fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
