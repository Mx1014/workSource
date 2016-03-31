//
// EvhPmListOrgBillingTransactionsByConditionsRestResponse.h
// generated at 2016-03-31 19:08:54 
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
