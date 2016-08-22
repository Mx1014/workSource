//
// EvhRentalv2OnlinePayCallbackCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2OnlinePayCallbackCommandResponse
//
@interface EvhRentalv2OnlinePayCallbackCommandResponse
    : NSObject<EvhJsonSerializable>


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

