//
// EvhAdminTechparkParkSetWaitingDaysRestResponse.h
// generated at 2016-03-25 09:26:43 
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
