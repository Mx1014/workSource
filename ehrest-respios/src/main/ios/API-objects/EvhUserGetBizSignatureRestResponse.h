//
// EvhUserGetBizSignatureRestResponse.h
// generated at 2016-04-22 13:56:52 
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
