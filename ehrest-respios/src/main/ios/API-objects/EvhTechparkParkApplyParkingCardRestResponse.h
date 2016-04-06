//
// EvhTechparkParkApplyParkingCardRestResponse.h
// generated at 2016-04-06 19:59:47 
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
