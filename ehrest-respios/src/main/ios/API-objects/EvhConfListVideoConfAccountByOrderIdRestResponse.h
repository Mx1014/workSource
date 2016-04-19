//
// EvhConfListVideoConfAccountByOrderIdRestResponse.h
// generated at 2016-04-19 13:40:01 
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
