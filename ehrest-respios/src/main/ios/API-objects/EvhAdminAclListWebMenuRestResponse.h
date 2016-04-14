//
// EvhAdminAclListWebMenuRestResponse.h
// generated at 2016-04-12 19:00:53 
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
