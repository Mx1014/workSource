//
// EvhConfListOrderByAccountRestResponse.h
// generated at 2016-04-22 13:56:50 
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
