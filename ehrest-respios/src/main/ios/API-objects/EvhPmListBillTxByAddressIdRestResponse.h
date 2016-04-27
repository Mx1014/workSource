//
// EvhPmListBillTxByAddressIdRestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListBillTxByAddressIdRestResponse
//
@interface EvhPmListBillTxByAddressIdRestResponse : EvhRestResponseBase

// array of EvhFamilyBillingTransactionDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
