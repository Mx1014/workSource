//
// EvhGetLaunchPadItemsByKeywordAdminCommandResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

