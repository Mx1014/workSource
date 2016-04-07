//
// EvhUiUserListScenesByCummunityIdRestResponse.h
// generated at 2016-04-07 14:16:32 
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
