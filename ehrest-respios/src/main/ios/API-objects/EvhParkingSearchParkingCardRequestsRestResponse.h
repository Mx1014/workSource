//
// EvhParkingSearchParkingCardRequestsRestResponse.h
// generated at 2016-04-12 15:02:21 
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
