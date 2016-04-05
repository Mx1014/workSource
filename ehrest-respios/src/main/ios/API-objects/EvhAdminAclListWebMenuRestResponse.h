//
// EvhAdminAclListWebMenuRestResponse.h
// generated at 2016-04-05 13:45:26 
//
#import "RestResponseBase.h"
#import "EvhListWebMenuResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclListWebMenuRestResponse
//
@interface EvhAdminAclListWebMenuRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListWebMenuResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
