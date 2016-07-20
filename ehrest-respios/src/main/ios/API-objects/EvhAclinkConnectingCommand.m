//
// EvhAclinkConnectingCommand.m
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
    if(self.encryptBase64)
        [jsonObject setObject: self.encryptBase64 forKey: @"encryptBase64"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        self.encryptBase64 = [jsonObject objectForKey: @"encryptBase64"];
        if(self.encryptBase64 && [self.encryptBase64 isEqual:[NSNull null]])
            self.encryptBase64 = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
