//
// EvhVideoconfUpdateContactorCommand.h
// generated at 2016-03-28 14:52:09 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoconfUpdateContactorCommand
//
@interface EvhVideoconfUpdateContactorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* contactorName;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSNumber* enterpriseId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

