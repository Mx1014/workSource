//
// EvhSendUserTestMailCommand.m
//
#import "EvhSendUserTestMailCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendUserTestMailCommand
//

@implementation EvhSendUserTestMailCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSendUserTestMailCommand* obj = [EvhSendUserTestMailCommand new];
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
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.from)
        [jsonObject setObject: self.from forKey: @"from"];
    if(self.to)
        [jsonObject setObject: self.to forKey: @"to"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.body)
        [jsonObject setObject: self.body forKey: @"body"];
    if(self.attachment1)
        [jsonObject setObject: self.attachment1 forKey: @"attachment1"];
    if(self.attachment2)
        [jsonObject setObject: self.attachment2 forKey: @"attachment2"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.from = [jsonObject objectForKey: @"from"];
        if(self.from && [self.from isEqual:[NSNull null]])
            self.from = nil;

        self.to = [jsonObject objectForKey: @"to"];
        if(self.to && [self.to isEqual:[NSNull null]])
            self.to = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.body = [jsonObject objectForKey: @"body"];
        if(self.body && [self.body isEqual:[NSNull null]])
            self.body = nil;

        self.attachment1 = [jsonObject objectForKey: @"attachment1"];
        if(self.attachment1 && [self.attachment1 isEqual:[NSNull null]])
            self.attachment1 = nil;

        self.attachment2 = [jsonObject objectForKey: @"attachment2"];
        if(self.attachment2 && [self.attachment2 isEqual:[NSNull null]])
            self.attachment2 = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
