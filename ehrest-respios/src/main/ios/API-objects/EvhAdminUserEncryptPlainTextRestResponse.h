//
// EvhAdminUserEncryptPlainTextRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhEncriptInfoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserEncryptPlainTextRestResponse
//
@interface EvhAdminUserEncryptPlainTextRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhEncriptInfoDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
