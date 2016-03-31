//
// EvhUiForumNewTopicBySceneRestResponse.h
// generated at 2016-03-31 15:43:24 
//
#import "RestResponseBase.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiForumNewTopicBySceneRestResponse
//
@interface EvhUiForumNewTopicBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPostDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
