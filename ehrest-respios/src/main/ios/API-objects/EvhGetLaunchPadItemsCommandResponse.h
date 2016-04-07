//
// EvhGetLaunchPadItemsCommandResponse.h
// generated at 2016-04-07 10:47:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhLaunchPadItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadItemsCommandResponse
//
@interface EvhGetLaunchPadItemsCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhLaunchPadItemDTO*
@property(nonatomic, strong) NSMutableArray* launchPadItems;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

