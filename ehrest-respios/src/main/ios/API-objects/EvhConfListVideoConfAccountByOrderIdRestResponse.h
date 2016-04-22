//
// EvhConfListVideoConfAccountByOrderIdRestResponse.h
// generated at 2016-04-22 13:56:50 
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
