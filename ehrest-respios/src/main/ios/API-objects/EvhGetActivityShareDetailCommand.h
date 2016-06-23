//
// EvhGetActivityShareDetailCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetActivityShareDetailCommand
//
@interface EvhGetActivityShareDetailCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* postToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

