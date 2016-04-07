//
// EvhUiUserListScenesByCummunityIdRestResponse.h
// generated at 2016-04-07 17:03:18 
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
