//
// EvhUserListActivityFavoriteRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListPostResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListActivityFavoriteRestResponse
//
@interface EvhUserListActivityFavoriteRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
