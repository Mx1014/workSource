//
// EvhUiUserListUserRelatedScenesRestResponse.h
// generated at 2016-03-30 10:13:10 
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
