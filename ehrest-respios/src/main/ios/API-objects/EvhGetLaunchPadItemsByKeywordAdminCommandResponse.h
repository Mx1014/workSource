//
// EvhGetLaunchPadItemsByKeywordAdminCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhLaunchPadItemAdminDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadItemsByKeywordAdminCommandResponse
//
@interface EvhGetLaunchPadItemsByKeywordAdminCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhLaunchPadItemAdminDTO*
@property(nonatomic, strong) NSMutableArray* launchPadItems;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

