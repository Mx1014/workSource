//
// EvhFamilyBillingTransactionDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyBillingTransactionDTO
//
@interface EvhFamilyBillingTransactionDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* billType;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* chargeAmount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

