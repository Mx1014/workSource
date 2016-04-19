//
// EvhPollPostCommand.h
// generated at 2016-04-19 13:40:00 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPollItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollPostCommand
//
@interface EvhPollPostCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* stopTime;

@property(nonatomic, copy) NSNumber* multiChoiceFlag;

@property(nonatomic, copy) NSNumber* anonymousFlag;

@property(nonatomic, copy) NSNumber* id;

// item type EvhPollItemDTO*
@property(nonatomic, strong) NSMutableArray* itemList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

