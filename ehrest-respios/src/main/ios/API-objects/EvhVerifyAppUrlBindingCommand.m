//
// EvhVerifyAppUrlBindingCommand.m
//
#import "EvhVerifyAppUrlBindingCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyAppUrlBindingCommand
//

@implementation EvhVerifyAppUrlBindingCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVerifyAppUrlBindingCommand* obj = [EvhVerifyAppUrlBindingCommand new];
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
    if(self.appKey)
        [jsonObject setObject: self.appKey forKey: @"appKey"];
    if(self.url)
        [jsonObject setObject: self.url forKey: @"url"];
    if(self.timestamp)
        [jsonObject setObject: self.timestamp forKey: @"timestamp"];
    if(self.nonce)
        [jsonObject setObject: self.nonce forKey: @"nonce"];
    if(self.signature)
        [jsonObject setObject: self.signature forKey: @"signature"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.appKey = [jsonObject objectForKey: @"appKey"];
        if(self.appKey && [self.appKey isEqual:[NSNull null]])
            self.appKey = nil;

        self.url = [jsonObject objectForKey: @"url"];
        if(self.url && [self.url isEqual:[NSNull null]])
            self.url = nil;

        self.timestamp = [jsonObject objectForKey: @"timestamp"];
        if(self.timestamp && [self.timestamp isEqual:[NSNull null]])
            self.timestamp = nil;

        self.nonce = [jsonObject objectForKey: @"nonce"];
        if(self.nonce && [self.nonce isEqual:[NSNull null]])
            self.nonce = nil;

        self.signature = [jsonObject objectForKey: @"signature"];
        if(self.signature && [self.signature isEqual:[NSNull null]])
            self.signature = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
