//
// EvhUiCommunityListCommunitiesBySceneRestResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:58 
>>>>>>> 3.3.x
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
