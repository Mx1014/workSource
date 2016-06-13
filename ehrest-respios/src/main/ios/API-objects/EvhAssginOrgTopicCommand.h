//
// EvhAssginOrgTopicCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAssginOrgTopicCommand
//
@interface EvhAssginOrgTopicCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* topicId;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* contactPhone;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

