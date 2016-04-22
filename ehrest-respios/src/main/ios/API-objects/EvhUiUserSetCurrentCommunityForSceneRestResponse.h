//
// EvhUiUserSetCurrentCommunityForSceneRestResponse.h
// generated at 2016-04-22 13:56:52 
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
