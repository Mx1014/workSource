//
// EvhUiNewsListNewsBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListNewsBySceneResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiNewsListNewsBySceneRestResponse
//
@interface EvhUiNewsListNewsBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNewsBySceneResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
