//
// EvhListParkingCardRequestResponse.h
// generated at 2016-04-07 17:03:16 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhParkingCardRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListParkingCardRequestResponse
//
@interface EvhListParkingCardRequestResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhParkingCardRequestDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

