//
// EvhAdminTechparkParkGetWaitingDaysRestResponse.h
// generated at 2016-04-18 14:48:52 
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
