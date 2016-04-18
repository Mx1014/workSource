//
// EvhOrganizationMemberCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationMemberCommand
//
@interface EvhOrganizationMemberCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* memberId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

