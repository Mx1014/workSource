//
// EvhSignupCommand.m
//
#import "EvhSignupCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSignupCommand
//

@implementation EvhSignupCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSignupCommand* obj = [EvhSignupCommand new];
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
    if(self.type)
        [jsonObject setObject: self.type forKey: @"type"];
    if(self.token)
        [jsonObject setObject: self.token forKey: @"token"];
    if(self.channel_id)
        [jsonObject setObject: self.channel_id forKey: @"channel_id"];
    if(self.ifExistsThenOverride)
        [jsonObject setObject: self.ifExistsThenOverride forKey: @"ifExistsThenOverride"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.type = [jsonObject objectForKey: @"type"];
        if(self.type && [self.type isEqual:[NSNull null]])
            self.type = nil;

        self.token = [jsonObject objectForKey: @"token"];
        if(self.token && [self.token isEqual:[NSNull null]])
            self.token = nil;

        self.channel_id = [jsonObject objectForKey: @"channel_id"];
        if(self.channel_id && [self.channel_id isEqual:[NSNull null]])
            self.channel_id = nil;

        self.ifExistsThenOverride = [jsonObject objectForKey: @"ifExistsThenOverride"];
        if(self.ifExistsThenOverride && [self.ifExistsThenOverride isEqual:[NSNull null]])
            self.ifExistsThenOverride = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
