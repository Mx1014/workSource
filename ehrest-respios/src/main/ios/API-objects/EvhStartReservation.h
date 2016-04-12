//
// EvhStartReservation.h
// generated at 2016-04-08 20:09:23 
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

