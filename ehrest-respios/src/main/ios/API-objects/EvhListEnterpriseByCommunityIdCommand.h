//
// EvhListEnterpriseByCommunityIdCommand.h
// generated at 2016-04-07 17:57:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseByCommunityIdCommand
//
@interface EvhListEnterpriseByCommunityIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

