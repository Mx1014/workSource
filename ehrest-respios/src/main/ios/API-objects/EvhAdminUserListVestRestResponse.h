//
// EvhAdminUserListVestRestResponse.h
// generated at 2016-04-08 20:09:23 
//
#import "RestResponseBase.h"
#import "EvhListVestResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminUserListVestRestResponse
//
@interface EvhAdminUserListVestRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListVestResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
