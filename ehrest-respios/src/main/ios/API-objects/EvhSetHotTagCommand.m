//
// EvhSetHotTagCommand.m
//
#import "EvhSetHotTagCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetHotTagCommand
//

@implementation EvhSetHotTagCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetHotTagCommand* obj = [EvhSetHotTagCommand new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.serviceType)
        [jsonObject setObject: self.serviceType forKey: @"serviceType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.serviceType = [jsonObject objectForKey: @"serviceType"];
        if(self.serviceType && [self.serviceType isEqual:[NSNull null]])
            self.serviceType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
