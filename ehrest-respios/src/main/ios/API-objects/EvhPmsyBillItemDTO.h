//
// EvhPmsyBillItemDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyBillItemDTO
//
@interface EvhPmsyBillItemDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* billId;

@property(nonatomic, copy) NSNumber* billDateStr;

@property(nonatomic, copy) NSNumber* receivableAmount;

@property(nonatomic, copy) NSNumber* debtAmount;

@property(nonatomic, copy) NSString* customerId;

@property(nonatomic, copy) NSString* itemName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

