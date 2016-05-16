//
// EvhBusinessMessageCommand.m
//
#import "EvhBusinessMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessMessageCommand
//

@implementation EvhBusinessMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBusinessMessageCommand* obj = [EvhBusinessMessageCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _meta = [NSMutableDictionary new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.timestamp)
        [jsonObject setObject: self.timestamp forKey: @"timestamp"];
    if(self.nonce)
        [jsonObject setObject: self.nonce forKey: @"nonce"];
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.contentType)
        [jsonObject setObject: self.contentType forKey: @"contentType"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.meta) {
        NSMutableDictionary* jsonMap = [NSMutableDictionary new];
        for(NSString* key in self.meta) {
            [jsonMap setValue:[self.meta objectForKey: key] forKey: key];
        }
        [jsonObject setObject: jsonMap forKey: @"meta"];
    }        
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.timestamp = [jsonObject objectForKey: @"timestamp"];
        if(self.timestamp && [self.timestamp isEqual:[NSNull null]])
            self.timestamp = nil;

        self.nonce = [jsonObject objectForKey: @"nonce"];
        if(self.nonce && [self.nonce isEqual:[NSNull null]])
            self.nonce = nil;

        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.contentType = [jsonObject objectForKey: @"contentType"];
        if(self.contentType && [self.contentType isEqual:[NSNull null]])
            self.contentType = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        {
            NSDictionary* jsonMap = [jsonObject objectForKey: @"meta"];
            for(NSString* key in jsonMap) {
                [self.meta setObject: [jsonMap objectForKey: key] forKey: key];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
