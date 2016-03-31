//
// EvhAclinkListAdminAesUserKeyRestResponse.h
// generated at 2016-03-31 15:43:23 
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
