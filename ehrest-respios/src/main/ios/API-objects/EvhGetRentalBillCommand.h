//
// EvhGetRentalBillCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetRentalBillCommand
//
@interface EvhGetRentalBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* billId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

