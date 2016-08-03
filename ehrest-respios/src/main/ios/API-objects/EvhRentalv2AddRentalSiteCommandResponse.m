//
// EvhRentalv2AddRentalSiteCommandResponse.m
//
#import "EvhRentalv2AddRentalSiteCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2AddRentalSiteCommandResponse
//

@implementation EvhRentalv2AddRentalSiteCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2AddRentalSiteCommandResponse* obj = [EvhRentalv2AddRentalSiteCommandResponse new];
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
