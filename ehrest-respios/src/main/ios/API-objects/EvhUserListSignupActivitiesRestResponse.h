//
// EvhUserListSignupActivitiesRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListPostResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListSignupActivitiesRestResponse
//
@interface EvhUserListSignupActivitiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
