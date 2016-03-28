//
// EvhUiCommunityListCommunitiesBySceneRestResponse.h
// generated at 2016-03-28 15:56:09 
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
