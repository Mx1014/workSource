//
// EvhGetLaunchPadItemsCommandResponse.h
// generated at 2016-03-31 10:18:18 
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

