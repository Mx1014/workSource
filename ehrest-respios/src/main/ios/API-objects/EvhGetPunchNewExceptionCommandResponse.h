//
// EvhGetPunchNewExceptionCommandResponse.h
// generated at 2016-03-30 10:13:07 
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

