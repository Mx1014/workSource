//
// EvhAdminUserListVestRestResponse.h
// generated at 2016-04-07 17:57:43 
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
