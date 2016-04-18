//
// EvhUpdateContactorCommand.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateContactorCommand
//
@interface EvhUpdateContactorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* contactorName;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSNumber* enterpriseId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

