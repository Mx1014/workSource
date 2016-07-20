//
// EvhAclinkWebSocketMessage.m
//
#import "EvhAclinkWebSocketMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkWebSocketMessage
//

@implementation EvhAclinkWebSocketMessage

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkWebSocketMessage* obj = [EvhAclinkWebSocketMessage new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.seq)
        [jsonObject setObject: self.seq forKey: @"seq"];
    if(self.type)
        [jsonObject setObject: self.type forKey: @"type"];
    if(self.payload)
        [jsonObject setObject: self.payload forKey: @"payload"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.seq = [jsonObject objectForKey: @"seq"];
        if(self.seq && [self.seq isEqual:[NSNull null]])
            self.seq = nil;

        self.type = [jsonObject objectForKey: @"type"];
        if(self.type && [self.type isEqual:[NSNull null]])
            self.type = nil;

        self.payload = [jsonObject objectForKey: @"payload"];
        if(self.payload && [self.payload isEqual:[NSNull null]])
            self.payload = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
