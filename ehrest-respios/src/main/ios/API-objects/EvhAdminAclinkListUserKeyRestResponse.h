//
// EvhAdminAclinkListUserKeyRestResponse.h
// generated at 2016-04-19 13:40:01 
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
