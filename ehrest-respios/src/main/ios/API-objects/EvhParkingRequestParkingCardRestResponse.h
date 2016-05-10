//
// EvhParkingRequestParkingCardRestResponse.h
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
