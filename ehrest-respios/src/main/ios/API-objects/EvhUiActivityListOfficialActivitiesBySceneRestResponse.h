//
// EvhUiActivityListOfficialActivitiesBySceneRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListOfficialActivitiesBySceneReponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiActivityListOfficialActivitiesBySceneRestResponse
//
@interface EvhUiActivityListOfficialActivitiesBySceneRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListOfficialActivitiesBySceneReponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
