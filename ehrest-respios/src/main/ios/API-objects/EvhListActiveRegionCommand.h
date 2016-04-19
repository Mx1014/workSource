//
// EvhListActiveRegionCommand.h
// generated at 2016-04-19 12:41:53 
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

