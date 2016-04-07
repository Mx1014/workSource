//
// EvhAdminUnregisterLoginRestResponse.h
// generated at 2016-04-07 17:03:18 
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
