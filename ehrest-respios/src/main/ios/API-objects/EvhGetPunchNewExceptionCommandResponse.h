//
// EvhGetPunchNewExceptionCommandResponse.h
// generated at 2016-03-25 19:05:20 
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

