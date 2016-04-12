//
// EvhAddRentalSiteCommandResponse.m
// generated at 2016-04-08 20:09:23 
//
#import "EvhAddRentalSiteCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddRentalSiteCommandResponse
//

@implementation EvhAddRentalSiteCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddRentalSiteCommandResponse* obj = [EvhAddRentalSiteCommandResponse new];
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
