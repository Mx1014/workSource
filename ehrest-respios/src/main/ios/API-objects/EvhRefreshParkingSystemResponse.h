//
// EvhRefreshParkingSystemResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRefreshParkingSystemResponse
//
@interface EvhRefreshParkingSystemResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* carNumber;

@property(nonatomic, copy) NSString* flag;

@property(nonatomic, copy) NSString* cost;

@property(nonatomic, copy) NSString* validStart;

@property(nonatomic, copy) NSString* validEnd;

@property(nonatomic, copy) NSString* payTime;

@property(nonatomic, copy) NSString* sign;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

