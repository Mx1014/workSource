//
// EvhListPropFamilyMemberCommand.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropFamilyMemberCommand
//
@interface EvhListPropFamilyMemberCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* familyId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

