//
// EvhConfListConfOrderRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListVideoConfAccountOrderResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListConfOrderRestResponse
//
@interface EvhConfListConfOrderRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListVideoConfAccountOrderResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
