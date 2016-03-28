//
// EvhAdminRegisterLoginRestResponse.h
// generated at 2016-03-25 19:05:21 
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
