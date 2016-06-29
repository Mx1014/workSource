//
// EvhFetchRecentToPastMessageAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFetchRecentToPastMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchRecentToPastMessageAdminCommand
//
@interface EvhFetchRecentToPastMessageAdminCommand
    : EvhFetchRecentToPastMessageCommand


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* loginId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

