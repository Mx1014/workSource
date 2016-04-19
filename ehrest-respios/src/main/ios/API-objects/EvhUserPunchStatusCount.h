//
// EvhUserPunchStatusCount.h
// generated at 2016-04-19 12:41:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserPunchStatusCount
//
@interface EvhUserPunchStatusCount
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* count;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

