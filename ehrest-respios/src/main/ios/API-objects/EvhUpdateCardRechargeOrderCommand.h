//
// EvhUpdateCardRechargeOrderCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateCardRechargeOrderCommand
//
@interface EvhUpdateCardRechargeOrderCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rechargeStatus;

@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

