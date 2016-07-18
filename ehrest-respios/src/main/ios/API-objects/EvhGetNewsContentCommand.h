//
// EvhGetNewsContentCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetNewsContentCommand
//
@interface EvhGetNewsContentCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* theNewsToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

