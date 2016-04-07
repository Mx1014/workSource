//
// EvhPmListOrgBillingTransactionsByConditionsRestResponse.h
// generated at 2016-04-07 15:16:54 
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
