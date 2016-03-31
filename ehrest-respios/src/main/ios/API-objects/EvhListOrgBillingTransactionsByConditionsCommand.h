//
// EvhListOrgBillingTransactionsByConditionsCommand.h
// generated at 2016-03-28 15:56:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrgBillingTransactionsByConditionsCommand
//
@interface EvhListOrgBillingTransactionsByConditionsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* payDate;

@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

