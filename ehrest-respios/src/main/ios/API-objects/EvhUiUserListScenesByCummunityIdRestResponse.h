//
// EvhUiUserListScenesByCummunityIdRestResponse.h
// generated at 2016-03-25 17:08:13 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiUserListScenesByCummunityIdRestResponse
//
@interface EvhUiUserListScenesByCummunityIdRestResponse : EvhRestResponseBase

// array of EvhSceneDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
