//
// EvhOnlinePayCallbackCommandResponse.m
// generated at 2016-03-25 19:05:19 
//
#import "EvhOnlinePayCallbackCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOnlinePayCallbackCommandResponse
//

@implementation EvhOnlinePayCallbackCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOnlinePayCallbackCommandResponse* obj = [EvhOnlinePayCallbackCommandResponse new];
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
