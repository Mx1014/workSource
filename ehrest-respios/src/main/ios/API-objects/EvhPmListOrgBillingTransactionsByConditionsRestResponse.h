//
// EvhPmListOrgBillingTransactionsByConditionsRestResponse.h
// generated at 2016-04-18 14:48:52 
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
