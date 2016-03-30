//
// EvhConfListConfOrderRestResponse.h
// generated at 2016-03-30 10:13:09 
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
