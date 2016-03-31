//
// EvhListBillTxByAddressIdCommandResponse.h
// generated at 2016-03-28 15:56:07 
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

