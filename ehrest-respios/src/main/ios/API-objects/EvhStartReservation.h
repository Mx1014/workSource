//
// EvhStartReservation.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhStartReservation
//
@interface EvhStartReservation
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* loginName;

@property(nonatomic, copy) NSString* timeStamp;

@property(nonatomic, copy) NSString* token;

@property(nonatomic, copy) NSString* confName;

@property(nonatomic, copy) NSString* hostKey;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* duration;

@property(nonatomic, copy) NSNumber* optionJbh;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

