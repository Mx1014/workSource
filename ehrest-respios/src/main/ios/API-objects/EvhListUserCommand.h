//
// EvhListUserCommand.h
// generated at 2016-04-07 17:57:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserCommand
//
@interface EvhListUserCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* keyword;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

