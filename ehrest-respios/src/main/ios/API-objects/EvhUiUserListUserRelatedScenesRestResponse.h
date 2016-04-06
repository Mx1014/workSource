//
// EvhUiUserListUserRelatedScenesRestResponse.h
// generated at 2016-04-06 19:10:44 
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
