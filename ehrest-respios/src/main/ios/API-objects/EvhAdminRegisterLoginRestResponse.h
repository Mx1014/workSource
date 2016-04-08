//
// EvhAdminRegisterLoginRestResponse.h
// generated at 2016-04-07 17:57:43 
//
#import "RestResponseBase.h"
#import "EvhUserLoginDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminRegisterLoginRestResponse
//
@interface EvhAdminRegisterLoginRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserLoginDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
