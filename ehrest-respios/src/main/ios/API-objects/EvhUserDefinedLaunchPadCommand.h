//
// EvhUserDefinedLaunchPadCommand.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhItem.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserDefinedLaunchPadCommand
//
@interface EvhUserDefinedLaunchPadCommand
    : NSObject<EvhJsonSerializable>


// item type EvhItem*
@property(nonatomic, strong) NSMutableArray* Items;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

