//
// EvhPollDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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

