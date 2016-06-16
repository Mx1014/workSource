//
// EvhGetVisitorCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetVisitorCommand
//
@interface EvhGetVisitorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

