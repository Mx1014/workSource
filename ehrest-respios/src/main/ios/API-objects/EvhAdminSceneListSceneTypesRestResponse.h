//
// EvhAdminSceneListSceneTypesRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminSceneListSceneTypesRestResponse
//
@interface EvhAdminSceneListSceneTypesRestResponse : EvhRestResponseBase

// array of EvhSceneTypeInfoDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
