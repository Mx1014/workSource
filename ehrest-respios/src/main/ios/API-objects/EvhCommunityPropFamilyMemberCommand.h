//
// EvhCommunityPropFamilyMemberCommand.h
// generated at 2016-04-07 14:16:29 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityPropFamilyMemberCommand
//
@interface EvhCommunityPropFamilyMemberCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSNumber* userId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

