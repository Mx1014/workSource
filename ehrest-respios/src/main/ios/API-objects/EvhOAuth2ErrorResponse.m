//
// EvhOAuth2ErrorResponse.m
//
#import "EvhOAuth2ErrorResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOAuth2ErrorResponse
//

@implementation EvhOAuth2ErrorResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOAuth2ErrorResponse* obj = [EvhOAuth2ErrorResponse new];
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
    if(self.error)
        [jsonObject setObject: self.error forKey: @"error"];
    if(self.error_description)
        [jsonObject setObject: self.error_description forKey: @"error_description"];
    if(self.error_uri)
        [jsonObject setObject: self.error_uri forKey: @"error_uri"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.error = [jsonObject objectForKey: @"error"];
        if(self.error && [self.error isEqual:[NSNull null]])
            self.error = nil;

        self.error_description = [jsonObject objectForKey: @"error_description"];
        if(self.error_description && [self.error_description isEqual:[NSNull null]])
            self.error_description = nil;

        self.error_uri = [jsonObject objectForKey: @"error_uri"];
        if(self.error_uri && [self.error_uri isEqual:[NSNull null]])
            self.error_uri = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
