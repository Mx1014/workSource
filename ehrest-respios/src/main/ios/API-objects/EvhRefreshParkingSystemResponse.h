//
// EvhRefreshParkingSystemResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

