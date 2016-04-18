//
// EvhUserGetBizSignatureRestResponse.h
// generated at 2016-04-18 14:48:53 
//
#import "RestResponseBase.h"
#import "EvhGetSignatureCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserGetBizSignatureRestResponse
//
@interface EvhUserGetBizSignatureRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetSignatureCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
