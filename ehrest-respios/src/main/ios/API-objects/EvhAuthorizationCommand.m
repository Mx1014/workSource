//
// EvhAuthorizationCommand.m
//
#import "EvhAuthorizationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAuthorizationCommand
//

@implementation EvhAuthorizationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAuthorizationCommand* obj = [EvhAuthorizationCommand new];
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
    if(self.response_type)
        [jsonObject setObject: self.response_type forKey: @"response_type"];
    if(self.client_id)
        [jsonObject setObject: self.client_id forKey: @"client_id"];
    if(self.redirect_uri)
        [jsonObject setObject: self.redirect_uri forKey: @"redirect_uri"];
    if(self.scope)
        [jsonObject setObject: self.scope forKey: @"scope"];
    if(self.state)
        [jsonObject setObject: self.state forKey: @"state"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.response_type = [jsonObject objectForKey: @"response_type"];
        if(self.response_type && [self.response_type isEqual:[NSNull null]])
            self.response_type = nil;

        self.client_id = [jsonObject objectForKey: @"client_id"];
        if(self.client_id && [self.client_id isEqual:[NSNull null]])
            self.client_id = nil;

        self.redirect_uri = [jsonObject objectForKey: @"redirect_uri"];
        if(self.redirect_uri && [self.redirect_uri isEqual:[NSNull null]])
            self.redirect_uri = nil;

        self.scope = [jsonObject objectForKey: @"scope"];
        if(self.scope && [self.scope isEqual:[NSNull null]])
            self.scope = nil;

        self.state = [jsonObject objectForKey: @"state"];
        if(self.state && [self.state isEqual:[NSNull null]])
            self.state = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
