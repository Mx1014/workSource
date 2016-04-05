//
// EvhUiUserListContactsBySceneRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhListContactBySceneRespose.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiUserListContactsBySceneRestResponse
//
@interface EvhUiUserListContactsBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListContactBySceneRespose* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
