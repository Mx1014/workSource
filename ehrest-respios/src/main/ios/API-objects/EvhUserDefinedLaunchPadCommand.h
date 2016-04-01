//
// EvhUserDefinedLaunchPadCommand.h
// generated at 2016-03-31 20:15:32 
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

