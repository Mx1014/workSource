//
// EvhUiUserListScenesByCummunityIdRestResponse.h
// generated at 2016-03-31 20:15:34 
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
