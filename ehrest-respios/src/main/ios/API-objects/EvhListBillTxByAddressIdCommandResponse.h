//
// EvhListBillTxByAddressIdCommandResponse.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFamilyBillingTransactionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBillTxByAddressIdCommandResponse
//
@interface EvhListBillTxByAddressIdCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhFamilyBillingTransactionDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

