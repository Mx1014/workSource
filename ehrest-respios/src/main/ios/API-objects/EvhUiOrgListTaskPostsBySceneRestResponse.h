//
// EvhUiOrgListTaskPostsBySceneRestResponse.h
// generated at 2016-03-31 13:49:15 
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
