//
// EvhUpdateOrganizationMemberByIdsCommand.h
// generated at 2016-03-28 15:56:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateOrganizationMemberByIdsCommand
//
@interface EvhUpdateOrganizationMemberByIdsCommand
    : NSObject<EvhJsonSerializable>


// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* ids;

@property(nonatomic, copy) NSNumber* orgId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

