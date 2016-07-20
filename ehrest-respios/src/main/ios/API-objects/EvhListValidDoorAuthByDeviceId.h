//
// EvhListValidDoorAuthByDeviceId.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListValidDoorAuthByDeviceId
//
@interface EvhListValidDoorAuthByDeviceId
    : NSObject<EvhJsonSerializable>


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

