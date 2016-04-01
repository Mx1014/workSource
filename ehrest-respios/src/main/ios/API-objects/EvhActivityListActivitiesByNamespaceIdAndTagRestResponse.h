//
// EvhActivityListActivitiesByNamespaceIdAndTagRestResponse.h
// generated at 2016-04-01 15:40:24 
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
