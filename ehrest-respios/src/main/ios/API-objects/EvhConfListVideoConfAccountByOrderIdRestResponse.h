//
// EvhConfListVideoConfAccountByOrderIdRestResponse.h
// generated at 2016-04-05 13:45:27 
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
