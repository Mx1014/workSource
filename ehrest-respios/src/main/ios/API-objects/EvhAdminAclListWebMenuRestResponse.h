//
// EvhAdminAclListWebMenuRestResponse.h
// generated at 2016-03-31 13:49:15 
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
