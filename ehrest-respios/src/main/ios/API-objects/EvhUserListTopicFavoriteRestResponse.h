//
// EvhUserListTopicFavoriteRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListPostResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListTopicFavoriteRestResponse
//
@interface EvhUserListTopicFavoriteRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
