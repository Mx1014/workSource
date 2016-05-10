//
// EvhReserveVideoConfCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReserveVideoConfCommand
//
@interface EvhReserveVideoConfCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* timeZone;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* hostKey;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

