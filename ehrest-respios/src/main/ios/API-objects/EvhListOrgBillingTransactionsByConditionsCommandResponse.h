//
// EvhListOrgBillingTransactionsByConditionsCommandResponse.h
// generated at 2016-03-25 17:08:11 
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

