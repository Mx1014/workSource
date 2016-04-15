//
// EvhConfListConfOrderRestResponse.h
// generated at 2016-04-12 15:02:21 
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
