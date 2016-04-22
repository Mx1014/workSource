//
// EvhAdminUnregisterLoginRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhUserLoginDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUnregisterLoginRestResponse
//
@interface EvhAdminUnregisterLoginRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUserLoginDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
