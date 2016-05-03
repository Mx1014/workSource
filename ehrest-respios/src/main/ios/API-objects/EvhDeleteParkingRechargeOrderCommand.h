//
// EvhDeleteParkingRechargeOrderCommand.h
// generated at 2016-04-30 11:16:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteParkingRechargeOrderCommand
//
@interface EvhDeleteParkingRechargeOrderCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

