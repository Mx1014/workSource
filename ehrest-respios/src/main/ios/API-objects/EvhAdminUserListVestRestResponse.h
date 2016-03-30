//
// EvhAdminUserListVestRestResponse.h
// generated at 2016-03-30 10:13:09 
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
