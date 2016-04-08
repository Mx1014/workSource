//
// EvhAdminTechparkParkSetWaitingDaysRestResponse.h
// generated at 2016-04-07 17:57:43 
//
#import "RestResponseBase.h"
#import "EvhWaitingDaysResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminTechparkParkSetWaitingDaysRestResponse
//
@interface EvhAdminTechparkParkSetWaitingDaysRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhWaitingDaysResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
