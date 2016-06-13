//
// EvhSetOrgTopicStatusCommand.h
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

