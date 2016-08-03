//
// EvhRentalv2AddRentalSiteCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2AddRentalSiteCommandResponse
//
@interface EvhRentalv2AddRentalSiteCommandResponse
    : NSObject<EvhJsonSerializable>


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

