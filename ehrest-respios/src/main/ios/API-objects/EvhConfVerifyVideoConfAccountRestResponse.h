//
// EvhConfVerifyVideoConfAccountRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhUserAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfVerifyVideoConfAccountRestResponse
//
@interface EvhConfVerifyVideoConfAccountRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserAccountDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
