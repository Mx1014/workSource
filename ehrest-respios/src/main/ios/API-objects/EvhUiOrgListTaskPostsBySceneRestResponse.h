//
// EvhUiOrgListTaskPostsBySceneRestResponse.h
// generated at 2016-04-22 13:56:52 
//
#import "RestResponseBase.h"
#import "EvhListTaskPostsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiOrgListTaskPostsBySceneRestResponse
//
@interface EvhUiOrgListTaskPostsBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListTaskPostsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
