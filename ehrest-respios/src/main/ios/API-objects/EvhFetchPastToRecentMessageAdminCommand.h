//
// EvhFetchPastToRecentMessageAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFetchPastToRecentMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchPastToRecentMessageAdminCommand
//
@interface EvhFetchPastToRecentMessageAdminCommand
    : EvhFetchPastToRecentMessageCommand


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* loginId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

