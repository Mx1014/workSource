//
// EvhUserListActivityFavoriteRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListActivitiesReponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListActivityFavoriteRestResponse
//
@interface EvhUserListActivityFavoriteRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListActivitiesReponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
