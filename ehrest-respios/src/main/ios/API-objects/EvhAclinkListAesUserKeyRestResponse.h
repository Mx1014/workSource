//
// EvhAclinkListAesUserKeyRestResponse.h
// generated at 2016-04-19 14:25:57 
//
#import "RestResponseBase.h"
#import "EvhListAesUserKeyByUserResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkListAesUserKeyRestResponse
//
@interface EvhAclinkListAesUserKeyRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListAesUserKeyByUserResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
