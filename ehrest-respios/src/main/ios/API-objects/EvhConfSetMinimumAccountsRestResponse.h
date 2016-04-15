//
// EvhConfSetMinimumAccountsRestResponse.h
// generated at 2016-04-12 15:02:21 
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
