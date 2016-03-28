//
// EvhAdminActivityListRestResponse.h
// generated at 2016-03-28 15:56:09 
//
#import "RestResponseBase.h"
#import "EvhListActivitiesReponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminActivityListRestResponse
//
@interface EvhAdminActivityListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListActivitiesReponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
