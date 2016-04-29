//
// EvhConfOrderDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfOrderDTO
//
@interface EvhConfOrderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* buyChannel;

@property(nonatomic, copy) NSNumber* quantity;

@property(nonatomic, copy) NSNumber* assignedQuantity;

@property(nonatomic, copy) NSNumber* period;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSNumber* accountCategoryId;

@property(nonatomic, copy) NSNumber* invoiceFlag;

@property(nonatomic, copy) NSNumber* makeOutFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

