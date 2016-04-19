//
// EvhAdminTechparkParkSetWaitingDaysRestResponse.h
// generated at 2016-04-19 12:41:55 
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
