//
// EvhAclinkConnectingCommand.m
// generated at 2016-04-06 19:10:43 
//
#import "EvhAclinkConnectingCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkConnectingCommand
//

@implementation EvhAclinkConnectingCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkConnectingCommand* obj = [EvhAclinkConnectingCommand new];
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
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.encrypt_base64)
        [jsonObject setObject: self.encrypt_base64 forKey: @"encrypt_base64"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        self.encrypt_base64 = [jsonObject objectForKey: @"encrypt_base64"];
        if(self.encrypt_base64 && [self.encrypt_base64 isEqual:[NSNull null]])
            self.encrypt_base64 = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
