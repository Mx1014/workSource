//
// EvhConfListVideoConfAccountByOrderIdRestResponse.h
// generated at 2016-03-25 19:05:21 
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
