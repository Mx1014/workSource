//
// EvhUiCommunityListCommunitiesBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListCommunitiesBySceneResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiCommunityListCommunitiesBySceneRestResponse
//
@interface EvhUiCommunityListCommunitiesBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCommunitiesBySceneResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
