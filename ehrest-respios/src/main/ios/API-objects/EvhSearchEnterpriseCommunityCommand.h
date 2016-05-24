//
// EvhSearchEnterpriseCommunityCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchEnterpriseCommunityCommand
//
@interface EvhSearchEnterpriseCommunityCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* regionId;

@property(nonatomic, copy) NSNumber* communityType;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

