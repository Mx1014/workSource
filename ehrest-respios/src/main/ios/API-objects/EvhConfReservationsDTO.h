//
// EvhConfReservationsDTO.h
// generated at 2016-04-07 17:03:16 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfReservationsDTO
//
@interface EvhConfReservationsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* timeZone;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* duration;

@property(nonatomic, copy) NSString* confHostKey;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

