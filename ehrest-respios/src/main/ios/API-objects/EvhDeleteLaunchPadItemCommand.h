//
// EvhDeleteLaunchPadItemCommand.h
// generated at 2016-04-12 15:02:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteLaunchPadItemCommand
//
@interface EvhDeleteLaunchPadItemCommand
    : NSObject<EvhJsonSerializable>


// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* itemIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

