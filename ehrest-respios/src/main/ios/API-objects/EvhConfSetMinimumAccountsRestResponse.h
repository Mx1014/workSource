//
// EvhConfSetMinimumAccountsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhMinimumAccountsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfSetMinimumAccountsRestResponse
//
@interface EvhConfSetMinimumAccountsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhMinimumAccountsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
