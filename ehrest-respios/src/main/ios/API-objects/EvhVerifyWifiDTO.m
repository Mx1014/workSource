//
// EvhVerifyWifiDTO.m
//
#import "EvhVerifyWifiDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyWifiDTO
//

@implementation EvhVerifyWifiDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVerifyWifiDTO* obj = [EvhVerifyWifiDTO new];
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
    if(self.flag)
        [jsonObject setObject: self.flag forKey: @"flag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.flag = [jsonObject objectForKey: @"flag"];
        if(self.flag && [self.flag isEqual:[NSNull null]])
            self.flag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
