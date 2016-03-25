//
// EvhAdminAclinkListUserKeyRestResponse.h
// generated at 2016-03-25 15:57:23 
//
#import "RestResponseBase.h"
#import "EvhListAesUserKeyByUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminAclinkListUserKeyRestResponse
//
@interface EvhAdminAclinkListUserKeyRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListAesUserKeyByUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
