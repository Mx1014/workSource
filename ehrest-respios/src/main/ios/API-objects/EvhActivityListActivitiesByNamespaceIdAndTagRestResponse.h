//
// EvhActivityListActivitiesByNamespaceIdAndTagRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhListActivitiesReponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListActivitiesByNamespaceIdAndTagRestResponse
//
@interface EvhActivityListActivitiesByNamespaceIdAndTagRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListActivitiesReponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
