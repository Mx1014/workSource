//
// EvhUiUserListUserRelatedScenesRestResponse.h
// generated at 2016-04-26 18:22:57 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiUserListUserRelatedScenesRestResponse
//
@interface EvhUiUserListUserRelatedScenesRestResponse : EvhRestResponseBase

// array of EvhSceneDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
