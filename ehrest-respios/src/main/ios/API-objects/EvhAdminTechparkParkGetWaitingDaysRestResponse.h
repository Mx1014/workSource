//
// EvhAdminTechparkParkGetWaitingDaysRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhWaitingDaysResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminTechparkParkGetWaitingDaysRestResponse
//
@interface EvhAdminTechparkParkGetWaitingDaysRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhWaitingDaysResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
