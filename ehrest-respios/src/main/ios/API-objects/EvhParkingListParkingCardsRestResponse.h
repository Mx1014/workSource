//
// EvhParkingListParkingCardsRestResponse.h
// generated at 2016-04-07 17:03:18 
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
