//
// EvhParkingListParkingCardRequestsRestResponse.h
// generated at 2016-04-07 15:16:54 
//
#import "RestResponseBase.h"
#import "EvhListParkingCardRequestResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingListParkingCardRequestsRestResponse
//
@interface EvhParkingListParkingCardRequestsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListParkingCardRequestResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
