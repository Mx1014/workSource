//
// EvhReviewVerificationResultCommand.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReviewVerificationResultCommand
//
@interface EvhReviewVerificationResultCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* taskId;

@property(nonatomic, copy) NSNumber* reviewResult;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

