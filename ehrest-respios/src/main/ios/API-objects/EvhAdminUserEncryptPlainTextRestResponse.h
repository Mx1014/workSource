//
// EvhAdminUserEncryptPlainTextRestResponse.h
// generated at 2016-03-31 20:15:33 
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
