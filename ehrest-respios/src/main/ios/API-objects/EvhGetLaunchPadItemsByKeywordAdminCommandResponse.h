//
// EvhGetLaunchPadItemsByKeywordAdminCommandResponse.h
// generated at 2016-03-25 19:05:20 
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

