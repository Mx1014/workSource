//
// EvhGetPunchNewExceptionCommandResponse.h
// generated at 2016-04-07 14:16:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPunchNewExceptionCommandResponse
//
@interface EvhGetPunchNewExceptionCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* exceptionCode;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

