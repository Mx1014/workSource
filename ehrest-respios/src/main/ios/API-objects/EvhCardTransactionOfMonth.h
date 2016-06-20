//
// EvhCardTransactionOfMonth.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCardTransactionFromVendorDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardTransactionOfMonth
//
@interface EvhCardTransactionOfMonth
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* date;

@property(nonatomic, copy) NSNumber* consumeAmount;

@property(nonatomic, copy) NSNumber* rechargeAmount;

// item type EvhCardTransactionFromVendorDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

