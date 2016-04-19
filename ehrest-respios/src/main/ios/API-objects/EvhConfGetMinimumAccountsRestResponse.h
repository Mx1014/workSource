//
// EvhConfGetMinimumAccountsRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhMinimumAccountsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfGetMinimumAccountsRestResponse
//
@interface EvhConfGetMinimumAccountsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhMinimumAccountsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
