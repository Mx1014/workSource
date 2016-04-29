//
// EvhPmBillItemDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmBillItemDTO
//
@interface EvhPmBillItemDTO
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

