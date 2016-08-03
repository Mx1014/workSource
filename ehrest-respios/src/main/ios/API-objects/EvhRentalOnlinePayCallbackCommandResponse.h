//
// EvhRentalOnlinePayCallbackCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalOnlinePayCallbackCommandResponse
//
@interface EvhRentalOnlinePayCallbackCommandResponse
    : NSObject<EvhJsonSerializable>


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

