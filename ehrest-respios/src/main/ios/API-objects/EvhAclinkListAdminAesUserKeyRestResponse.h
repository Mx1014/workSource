//
// EvhAclinkListAdminAesUserKeyRestResponse.h
// generated at 2016-04-18 14:48:52 
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
