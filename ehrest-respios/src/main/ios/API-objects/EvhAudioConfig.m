//
// EvhAudioConfig.m
//
#import "EvhAudioConfig.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAudioConfig
//

@implementation EvhAudioConfig

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAudioConfig* obj = [EvhAudioConfig new];
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
    if(self.format)
        [jsonObject setObject: self.format forKey: @"format"];
    if(self.bit)
        [jsonObject setObject: self.bit forKey: @"bit"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.format = [jsonObject objectForKey: @"format"];
        if(self.format && [self.format isEqual:[NSNull null]])
            self.format = nil;

        self.bit = [jsonObject objectForKey: @"bit"];
        if(self.bit && [self.bit isEqual:[NSNull null]])
            self.bit = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
