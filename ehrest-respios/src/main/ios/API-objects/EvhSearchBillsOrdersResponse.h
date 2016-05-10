//
// EvhSearchBillsOrdersResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPmBillsOrdersDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchBillsOrdersResponse
//
@interface EvhSearchBillsOrdersResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhPmBillsOrdersDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

