//
// EvhUiUserListContactsBySceneRestResponse.h
// generated at 2016-04-07 10:47:33 
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
