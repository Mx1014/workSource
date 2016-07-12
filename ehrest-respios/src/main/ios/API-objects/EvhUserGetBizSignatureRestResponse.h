//
// EvhUserGetBizSignatureRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhUserGetSignatureCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserGetBizSignatureRestResponse
//
@interface EvhUserGetBizSignatureRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserGetSignatureCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
