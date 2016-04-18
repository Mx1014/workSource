//
// EvhSetOrgTopicStatusCommand.h
// generated at 2016-04-18 14:48:52 
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

