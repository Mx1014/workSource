//
// EvhGetPunchNewExceptionCommand.h
// generated at 2016-04-26 18:22:56 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPunchNewExceptionCommand
//
@interface EvhGetPunchNewExceptionCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

