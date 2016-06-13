//
// EvhConfListVideoConfAccountByOrderIdRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListConfOrderAccountResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListVideoConfAccountByOrderIdRestResponse
//
@interface EvhConfListVideoConfAccountByOrderIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListConfOrderAccountResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
