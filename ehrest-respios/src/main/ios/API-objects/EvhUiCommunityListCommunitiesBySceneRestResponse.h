//
// EvhUiCommunityListCommunitiesBySceneRestResponse.h
// generated at 2016-04-18 14:48:52 
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
