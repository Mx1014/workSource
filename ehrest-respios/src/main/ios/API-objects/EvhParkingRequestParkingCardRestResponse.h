//
// EvhParkingRequestParkingCardRestResponse.h
// generated at 2016-04-12 15:02:21 
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
