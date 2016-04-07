//
// EvhParkingSearchParkingCardRequestsRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhListParkingCardRequestResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingSearchParkingCardRequestsRestResponse
//
@interface EvhParkingSearchParkingCardRequestsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListParkingCardRequestResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
