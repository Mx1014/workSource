//
// EvhDeleteLaunchPadItemAdminCommand.h
// generated at 2016-04-22 13:56:44 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteLaunchPadItemAdminCommand
//
@interface EvhDeleteLaunchPadItemAdminCommand
    : NSObject<EvhJsonSerializable>


// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* itemIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

