//
// EvhQryPreferentialRuleByCommunityIdCommand.h
// generated at 2016-03-31 10:18:18 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQryPreferentialRuleByCommunityIdCommand
//
@interface EvhQryPreferentialRuleByCommunityIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

