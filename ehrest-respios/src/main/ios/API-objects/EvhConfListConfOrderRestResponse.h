//
// EvhConfListConfOrderRestResponse.h
// generated at 2016-04-19 14:25:57 
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
