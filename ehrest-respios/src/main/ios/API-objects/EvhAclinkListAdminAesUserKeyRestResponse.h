//
// EvhAclinkListAdminAesUserKeyRestResponse.h
// generated at 2016-03-31 20:15:33 
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
