//
// EvhListParkingCardRequestResponse.h
// generated at 2016-04-06 19:59:46 
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

