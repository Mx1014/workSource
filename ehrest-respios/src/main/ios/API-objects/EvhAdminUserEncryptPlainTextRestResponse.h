//
// EvhAdminUserEncryptPlainTextRestResponse.h
// generated at 2016-04-18 14:48:52 
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
