//
// EvhConfListOrderByAccountRestResponse.h
// generated at 2016-03-31 20:15:33 
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
