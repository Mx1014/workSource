//
// EvhRentalv2OnlinePayCallbackCommandResponse.m
//
#import "EvhRentalv2OnlinePayCallbackCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2OnlinePayCallbackCommandResponse
//

@implementation EvhRentalv2OnlinePayCallbackCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2OnlinePayCallbackCommandResponse* obj = [EvhRentalv2OnlinePayCallbackCommandResponse new];
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
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
