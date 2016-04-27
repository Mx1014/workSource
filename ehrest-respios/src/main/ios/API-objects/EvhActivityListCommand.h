//
// EvhActivityListCommand.h
// generated at 2016-04-26 18:22:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListCommand
//
@interface EvhActivityListCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* activityId;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSNumber* anchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

