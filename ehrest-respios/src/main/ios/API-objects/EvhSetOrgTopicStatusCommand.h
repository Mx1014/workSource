//
// EvhSetOrgTopicStatusCommand.h
// generated at 2016-03-31 13:49:12 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetOrgTopicStatusCommand
//
@interface EvhSetOrgTopicStatusCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* topicId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

