//
// EvhListActiveRegionCommand.h
// generated at 2016-03-31 13:49:12 
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

