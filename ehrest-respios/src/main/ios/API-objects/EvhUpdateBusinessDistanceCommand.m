//
// EvhUpdateBusinessDistanceCommand.m
//
#import "EvhUpdateBusinessDistanceCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateBusinessDistanceCommand
//

@implementation EvhUpdateBusinessDistanceCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateBusinessDistanceCommand* obj = [EvhUpdateBusinessDistanceCommand new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.visibleDistance)
        [jsonObject setObject: self.visibleDistance forKey: @"visibleDistance"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.visibleDistance = [jsonObject objectForKey: @"visibleDistance"];
        if(self.visibleDistance && [self.visibleDistance isEqual:[NSNull null]])
            self.visibleDistance = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
