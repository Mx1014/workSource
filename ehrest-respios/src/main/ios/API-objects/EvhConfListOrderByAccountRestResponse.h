//
// EvhConfListOrderByAccountRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhListOrderByAccountResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListOrderByAccountRestResponse
//
@interface EvhConfListOrderByAccountRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOrderByAccountResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
