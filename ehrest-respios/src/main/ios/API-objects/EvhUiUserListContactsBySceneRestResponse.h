//
// EvhUiUserListContactsBySceneRestResponse.h
// generated at 2016-03-25 09:26:45 
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
