//
// EvhSetPasswordCommand.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetPasswordCommand
//
@interface EvhSetPasswordCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* oldPassword;

@property(nonatomic, copy) NSString* theNewPassword;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

