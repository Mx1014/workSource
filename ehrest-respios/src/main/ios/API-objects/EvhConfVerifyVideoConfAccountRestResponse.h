//
// EvhConfVerifyVideoConfAccountRestResponse.h
// generated at 2016-03-31 20:15:33 
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
