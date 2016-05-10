//
// EvhListActiveRegionCommand.m
//
#import "EvhListActiveRegionCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListActiveRegionCommand
//

@implementation EvhListActiveRegionCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListActiveRegionCommand* obj = [EvhListActiveRegionCommand new];
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
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.scope = [jsonObject objectForKey: @"scope"];
        if(self.scope && [self.scope isEqual:[NSNull null]])
            self.scope = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
