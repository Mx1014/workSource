//
// EvhPollShowResultCommand.h
// generated at 2016-04-05 13:45:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollShowResultCommand
//
@interface EvhPollShowResultCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pollId;

@property(nonatomic, copy) NSString* uuid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

