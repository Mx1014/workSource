//
// EvhSetSourceSecretKeyCommand.m
//
#import "EvhSetSourceSecretKeyCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetSourceSecretKeyCommand
//

@implementation EvhSetSourceSecretKeyCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetSourceSecretKeyCommand* obj = [EvhSetSourceSecretKeyCommand new];
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
    if(self.secretKey)
        [jsonObject setObject: self.secretKey forKey: @"secretKey"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.secretKey = [jsonObject objectForKey: @"secretKey"];
        if(self.secretKey && [self.secretKey isEqual:[NSNull null]])
            self.secretKey = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
