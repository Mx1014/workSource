//
// EvhPmListOrgBillingTransactionsByConditionsRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmListOrgBillingTransactionsByConditionsRestResponse
//
@interface EvhPmListOrgBillingTransactionsByConditionsRestResponse : EvhRestResponseBase

// array of EvhOrganizationBillingTransactionDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
