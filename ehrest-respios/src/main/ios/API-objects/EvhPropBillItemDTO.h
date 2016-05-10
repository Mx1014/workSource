//
// EvhPropBillItemDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropBillItemDTO
//
@interface EvhPropBillItemDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* billId;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSNumber* startCount;

@property(nonatomic, copy) NSNumber* endCount;

@property(nonatomic, copy) NSNumber* useCount;

@property(nonatomic, copy) NSNumber* price;

@property(nonatomic, copy) NSNumber* totalAmount;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

