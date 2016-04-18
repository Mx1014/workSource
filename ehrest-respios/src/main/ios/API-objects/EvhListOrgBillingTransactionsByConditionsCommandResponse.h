//
// EvhListOrgBillingTransactionsByConditionsCommandResponse.h
// generated at 2016-04-18 14:48:50 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationBillingTransactionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrgBillingTransactionsByConditionsCommandResponse
//
@interface EvhListOrgBillingTransactionsByConditionsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhOrganizationBillingTransactionDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

