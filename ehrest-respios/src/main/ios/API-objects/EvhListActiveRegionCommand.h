//
// EvhListActiveRegionCommand.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListActiveRegionCommand
//
@interface EvhListActiveRegionCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* scope;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

