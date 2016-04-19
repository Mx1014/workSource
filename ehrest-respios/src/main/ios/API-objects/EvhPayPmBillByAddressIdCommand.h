//
// EvhPayPmBillByAddressIdCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPayPmBillByAddressIdCommand
//
@interface EvhPayPmBillByAddressIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSNumber* payTime;

@property(nonatomic, copy) NSNumber* payAmount;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* ownerName;

@property(nonatomic, copy) NSString* telephone;

@property(nonatomic, copy) NSString* vendor;

@property(nonatomic, copy) NSNumber* txType;

@property(nonatomic, copy) NSNumber* paidType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

