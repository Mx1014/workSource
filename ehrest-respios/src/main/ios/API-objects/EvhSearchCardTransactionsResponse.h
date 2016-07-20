//
// EvhSearchCardTransactionsResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCardTransactionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchCardTransactionsResponse
//
@interface EvhSearchCardTransactionsResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhCardTransactionDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

