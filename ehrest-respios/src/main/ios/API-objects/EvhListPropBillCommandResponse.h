//
// EvhListPropBillCommandResponse.h
// generated at 2016-03-31 15:43:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPropBillDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropBillCommandResponse
//
@interface EvhListPropBillCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhPropBillDTO*
@property(nonatomic, strong) NSMutableArray* members;

@property(nonatomic, copy) NSNumber* pageCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

