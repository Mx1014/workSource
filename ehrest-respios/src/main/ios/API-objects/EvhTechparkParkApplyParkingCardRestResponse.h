//
// EvhTechparkParkApplyParkingCardRestResponse.h
// generated at 2016-03-25 19:05:21 
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
