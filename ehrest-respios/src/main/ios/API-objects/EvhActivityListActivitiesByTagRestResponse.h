//
// EvhActivityListActivitiesByTagRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"
#import "EvhListActivitiesReponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListActivitiesByTagRestResponse
//
@interface EvhActivityListActivitiesByTagRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListActivitiesReponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
