//
// EvhParkingListParkingCardsRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingListParkingCardsRestResponse
//
@interface EvhParkingListParkingCardsRestResponse : EvhRestResponseBase

// array of EvhParkingCardDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
