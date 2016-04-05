//
// EvhPromoteBusinessAdminCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhItemScope.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPromoteBusinessAdminCommand
//
@interface EvhPromoteBusinessAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

// item type EvhItemScope*
@property(nonatomic, strong) NSMutableArray* itemScopes;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

