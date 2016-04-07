//
// EvhListUserByIdentifierCommand.h
// generated at 2016-04-07 14:16:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserByIdentifierCommand
//
@interface EvhListUserByIdentifierCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* identifier;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

