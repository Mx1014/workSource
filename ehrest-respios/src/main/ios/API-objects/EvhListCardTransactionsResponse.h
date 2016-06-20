//
// EvhListCardTransactionsResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCardTransactionOfMonth.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCardTransactionsResponse
//
@interface EvhListCardTransactionsResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhCardTransactionOfMonth*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

