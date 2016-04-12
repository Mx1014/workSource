//
// EvhUiCommunityListCommunitiesBySceneRestResponse.h
// generated at 2016-04-12 15:02:21 
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
