//
// EvhListOrderByAccountResponse.h
// generated at 2016-03-25 17:08:10 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCountAccountOrdersAndMonths.h"
#import "EvhOrderBriefDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrderByAccountResponse
//
@interface EvhListOrderByAccountResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

@property(nonatomic, strong) EvhCountAccountOrdersAndMonths* counts;

// item type EvhOrderBriefDTO*
@property(nonatomic, strong) NSMutableArray* orderBriefs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

