//
// EvhDeleteResourceTypeCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteResourceTypeCommand
//
@interface EvhDeleteResourceTypeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

