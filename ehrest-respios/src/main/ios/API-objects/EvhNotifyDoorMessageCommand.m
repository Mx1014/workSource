//
// EvhNotifyDoorMessageCommand.m
//
#import "EvhNotifyDoorMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNotifyDoorMessageCommand
//

@implementation EvhNotifyDoorMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNotifyDoorMessageCommand* obj = [EvhNotifyDoorMessageCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _Phones = [NSMutableArray new];
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
    if(self.crypto)
        [jsonObject setObject: self.crypto forKey: @"crypto"];
    if(self.Phones) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.Phones) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"Phones"];
    }
    if(self.contentType)
        [jsonObject setObject: self.contentType forKey: @"contentType"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
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

        self.crypto = [jsonObject objectForKey: @"crypto"];
        if(self.crypto && [self.crypto isEqual:[NSNull null]])
            self.crypto = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"Phones"];
            for(id itemJson in jsonArray) {
                [self.Phones addObject: itemJson];
            }
        }
        self.contentType = [jsonObject objectForKey: @"contentType"];
        if(self.contentType && [self.contentType isEqual:[NSNull null]])
            self.contentType = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
