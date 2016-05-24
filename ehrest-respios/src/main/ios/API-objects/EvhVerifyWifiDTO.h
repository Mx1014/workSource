//
// EvhVerifyWifiDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyWifiDTO
//
@interface EvhVerifyWifiDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* flag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

