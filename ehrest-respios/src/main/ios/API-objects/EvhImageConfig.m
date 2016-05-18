//
// EvhImageConfig.m
//
#import "EvhImageConfig.h"

///////////////////////////////////////////////////////////////////////////////
// EvhImageConfig
//

@implementation EvhImageConfig

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhImageConfig* obj = [EvhImageConfig new];
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
    if(self.width)
        [jsonObject setObject: self.width forKey: @"width"];
    if(self.height)
        [jsonObject setObject: self.height forKey: @"height"];
    if(self.gary)
        [jsonObject setObject: self.gary forKey: @"gary"];
    if(self.proportion)
        [jsonObject setObject: self.proportion forKey: @"proportion"];
    if(self.rotate)
        [jsonObject setObject: self.rotate forKey: @"rotate"];
    if(self.format)
        [jsonObject setObject: self.format forKey: @"format"];
    if(self.quality)
        [jsonObject setObject: self.quality forKey: @"quality"];
    if(self.x)
        [jsonObject setObject: self.x forKey: @"x"];
    if(self.y)
        [jsonObject setObject: self.y forKey: @"y"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.width = [jsonObject objectForKey: @"width"];
        if(self.width && [self.width isEqual:[NSNull null]])
            self.width = nil;

        self.height = [jsonObject objectForKey: @"height"];
        if(self.height && [self.height isEqual:[NSNull null]])
            self.height = nil;

        self.gary = [jsonObject objectForKey: @"gary"];
        if(self.gary && [self.gary isEqual:[NSNull null]])
            self.gary = nil;

        self.proportion = [jsonObject objectForKey: @"proportion"];
        if(self.proportion && [self.proportion isEqual:[NSNull null]])
            self.proportion = nil;

        self.rotate = [jsonObject objectForKey: @"rotate"];
        if(self.rotate && [self.rotate isEqual:[NSNull null]])
            self.rotate = nil;

        self.format = [jsonObject objectForKey: @"format"];
        if(self.format && [self.format isEqual:[NSNull null]])
            self.format = nil;

        self.quality = [jsonObject objectForKey: @"quality"];
        if(self.quality && [self.quality isEqual:[NSNull null]])
            self.quality = nil;

        self.x = [jsonObject objectForKey: @"x"];
        if(self.x && [self.x isEqual:[NSNull null]])
            self.x = nil;

        self.y = [jsonObject objectForKey: @"y"];
        if(self.y && [self.y isEqual:[NSNull null]])
            self.y = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
