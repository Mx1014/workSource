//
// EvhPollVoteCommand.h
// generated at 2016-03-25 19:05:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollVoteCommand
//
@interface EvhPollVoteCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pollId;

@property(nonatomic, copy) NSString* uuid;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* itemIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

