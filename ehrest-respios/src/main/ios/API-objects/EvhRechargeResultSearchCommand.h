//
// EvhRechargeResultSearchCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeResultSearchCommand
//
@interface EvhRechargeResultSearchCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* billId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

