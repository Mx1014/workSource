//
// EvhPmListOrgBillingTransactionsByConditionsRestResponse.h
// generated at 2016-03-25 19:05:21 
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
