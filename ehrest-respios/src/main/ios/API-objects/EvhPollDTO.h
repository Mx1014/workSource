//
// EvhPollDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollDTO
//
@interface EvhPollDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pollId;

@property(nonatomic, copy) NSString* startTime;

@property(nonatomic, copy) NSString* stopTime;

@property(nonatomic, copy) NSNumber* pollCount;

@property(nonatomic, copy) NSNumber* anonymousFlag;

@property(nonatomic, copy) NSNumber* multiChoiceFlag;

@property(nonatomic, copy) NSNumber* pollVoterStatus;

@property(nonatomic, copy) NSNumber* processStatus;

@property(nonatomic, copy) NSString* uuid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

