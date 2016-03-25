//
// EvhUserDefinedLaunchPadCommand.h
// generated at 2016-03-25 11:43:33 
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

