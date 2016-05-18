//
// EvhPmBillsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFamilyBillingTransactionDTO.h"
#import "EvhPmBillItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmBillsDTO
//
@interface EvhPmBillsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* entityId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* startDate;

@property(nonatomic, copy) NSNumber* endDate;

@property(nonatomic, copy) NSNumber* payDate;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* dueAmount;

@property(nonatomic, copy) NSNumber* oweAmount;

@property(nonatomic, copy) NSNumber* payedAmount;

@property(nonatomic, copy) NSNumber* waitPayAmount;

@property(nonatomic, copy) NSNumber* totalAmount;

// item type EvhFamilyBillingTransactionDTO*
@property(nonatomic, strong) NSMutableArray* payList;

// item type EvhPmBillItemDTO*
@property(nonatomic, strong) NSMutableArray* billItems;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

