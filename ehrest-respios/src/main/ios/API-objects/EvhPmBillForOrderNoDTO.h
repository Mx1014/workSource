//
// EvhPmBillForOrderNoDTO.h
// generated at 2016-03-31 13:49:13 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmBillForOrderNoDTO
//
@interface EvhPmBillForOrderNoDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* orderNo;

@property(nonatomic, copy) NSNumber* payAmount;

@property(nonatomic, copy) NSString* billName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

