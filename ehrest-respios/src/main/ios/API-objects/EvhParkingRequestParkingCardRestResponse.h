//
// EvhParkingRequestParkingCardRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhParkingCardRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingRequestParkingCardRestResponse
//
@interface EvhParkingRequestParkingCardRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhParkingCardRequestDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
