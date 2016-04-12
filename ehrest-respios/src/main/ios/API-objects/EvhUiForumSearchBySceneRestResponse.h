//
// EvhUiForumSearchBySceneRestResponse.h
// generated at 2016-04-08 20:09:24 
//
#import "RestResponseBase.h"
#import "EvhListPostCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiForumSearchBySceneRestResponse
//
@interface EvhUiForumSearchBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPostCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
