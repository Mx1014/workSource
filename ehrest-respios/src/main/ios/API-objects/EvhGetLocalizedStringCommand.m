//
// EvhGetLocalizedStringCommand.m
//
#import "EvhGetLocalizedStringCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLocalizedStringCommand
//

@implementation EvhGetLocalizedStringCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetLocalizedStringCommand* obj = [EvhGetLocalizedStringCommand new];
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
    if(self.scope)
        [jsonObject setObject: self.scope forKey: @"scope"];
    if(self.code)
        [jsonObject setObject: self.code forKey: @"code"];
    if(self.locale)
        [jsonObject setObject: self.locale forKey: @"locale"];
    if(self.defaultValue)
        [jsonObject setObject: self.defaultValue forKey: @"defaultValue"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.scope = [jsonObject objectForKey: @"scope"];
        if(self.scope && [self.scope isEqual:[NSNull null]])
            self.scope = nil;

        self.code = [jsonObject objectForKey: @"code"];
        if(self.code && [self.code isEqual:[NSNull null]])
            self.code = nil;

        self.locale = [jsonObject objectForKey: @"locale"];
        if(self.locale && [self.locale isEqual:[NSNull null]])
            self.locale = nil;

        self.defaultValue = [jsonObject objectForKey: @"defaultValue"];
        if(self.defaultValue && [self.defaultValue isEqual:[NSNull null]])
            self.defaultValue = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
