//
// EvhLogonByTokenCommand.m
//
#import "EvhLogonByTokenCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLogonByTokenCommand
//

@implementation EvhLogonByTokenCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhLogonByTokenCommand* obj = [EvhLogonByTokenCommand new];
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
    if(self.loginToken)
        [jsonObject setObject: self.loginToken forKey: @"loginToken"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.loginToken = [jsonObject objectForKey: @"loginToken"];
        if(self.loginToken && [self.loginToken isEqual:[NSNull null]])
            self.loginToken = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
