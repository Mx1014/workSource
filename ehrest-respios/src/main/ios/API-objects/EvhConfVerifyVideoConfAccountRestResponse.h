//
// EvhConfVerifyVideoConfAccountRestResponse.h
// generated at 2016-03-25 09:26:44 
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
