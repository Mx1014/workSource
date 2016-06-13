//
// EvhOpenDoorActionData.m
//
#import "EvhOpenDoorActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenDoorActionData
//

@implementation EvhOpenDoorActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOpenDoorActionData* obj = [EvhOpenDoorActionData new];
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
    if(self.vender)
        [jsonObject setObject: self.vender forKey: @"vender"];
    if(self.remote)
        [jsonObject setObject: self.remote forKey: @"remote"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.vender = [jsonObject objectForKey: @"vender"];
        if(self.vender && [self.vender isEqual:[NSNull null]])
            self.vender = nil;

        self.remote = [jsonObject objectForKey: @"remote"];
        if(self.remote && [self.remote isEqual:[NSNull null]])
            self.remote = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
