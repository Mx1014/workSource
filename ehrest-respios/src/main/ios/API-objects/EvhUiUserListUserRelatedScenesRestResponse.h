//
// EvhUiUserListUserRelatedScenesRestResponse.h
// generated at 2016-04-18 14:48:53 
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
