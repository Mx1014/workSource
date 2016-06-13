//
// EvhAclinkListAdminAesUserKeyRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListAesUserKeyByUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkListAdminAesUserKeyRestResponse
//
@interface EvhAclinkListAdminAesUserKeyRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListAesUserKeyByUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
