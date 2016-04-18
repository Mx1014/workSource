//
// EvhPollVoteCommand.h
// generated at 2016-04-18 14:48:50 
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

