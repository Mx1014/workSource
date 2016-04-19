//
// EvhFindBillByAddressIdAndTimeCommand.h
// generated at 2016-04-19 13:40:00 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindBillByAddressIdAndTimeCommand
//
@interface EvhFindBillByAddressIdAndTimeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* billDate;

@property(nonatomic, copy) NSNumber* addressId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

