//
// EvhDeleteLaunchPadLayoutAdminCommand.h
// generated at 2016-04-07 17:57:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteLaunchPadLayoutAdminCommand
//
@interface EvhDeleteLaunchPadLayoutAdminCommand
    : NSObject<EvhJsonSerializable>


// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* ids;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

