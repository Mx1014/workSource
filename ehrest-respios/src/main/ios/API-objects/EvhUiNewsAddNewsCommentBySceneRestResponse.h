//
// EvhUiNewsAddNewsCommentBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhAddNewsCommentBySceneResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiNewsAddNewsCommentBySceneRestResponse
//
@interface EvhUiNewsAddNewsCommentBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAddNewsCommentBySceneResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
