//
// EvhUiUserSetCurrentCommunityForSceneRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiUserSetCurrentCommunityForSceneRestResponse
//
@interface EvhUiUserSetCurrentCommunityForSceneRestResponse : EvhRestResponseBase

// array of EvhSceneDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
