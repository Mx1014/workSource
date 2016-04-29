//
// EvhPollShowResultResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPollDTO.h"
#import "EvhPollItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollShowResultResponse
//
@interface EvhPollShowResultResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhPollDTO* poll;

// item type EvhPollItemDTO*
@property(nonatomic, strong) NSMutableArray* items;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

