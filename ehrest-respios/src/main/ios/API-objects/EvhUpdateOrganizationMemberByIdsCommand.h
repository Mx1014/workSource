//
// EvhUpdateOrganizationMemberByIdsCommand.h
// generated at 2016-04-12 15:02:19 
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

