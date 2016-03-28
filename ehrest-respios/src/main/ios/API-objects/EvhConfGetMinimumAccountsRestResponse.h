//
// EvhConfGetMinimumAccountsRestResponse.h
// generated at 2016-03-28 15:56:09 
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
