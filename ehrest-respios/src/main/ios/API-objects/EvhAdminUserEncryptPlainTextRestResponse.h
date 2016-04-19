//
// EvhAdminUserEncryptPlainTextRestResponse.h
// generated at 2016-04-19 13:40:01 
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
