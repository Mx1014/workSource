//
// EvhListActiveRegionCommand.h
// generated at 2016-03-25 09:26:39 
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

