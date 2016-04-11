//
// EvhCommandResult.h
// generated at 2016-04-07 17:57:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommandResult
//
@interface EvhCommandResult
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* identifier;

@property(nonatomic, copy) NSString* errorScope;

@property(nonatomic, copy) NSNumber* errorCode;

@property(nonatomic, copy) NSString* errorDescription;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

