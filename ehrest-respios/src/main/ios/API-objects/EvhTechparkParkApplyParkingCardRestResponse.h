//
// EvhTechparkParkApplyParkingCardRestResponse.h
// generated at 2016-04-08 20:09:24 
//
#import "RestResponseBase.h"
#import "EvhWaitingLine.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkApplyParkingCardRestResponse
//
@interface EvhTechparkParkApplyParkingCardRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhWaitingLine* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
