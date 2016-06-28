//
// EvhAppUrlGetAppInfoRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhAppUrlDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppUrlGetAppInfoRestResponse
//
@interface EvhAppUrlGetAppInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAppUrlDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
