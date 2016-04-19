//
// EvhFetchRecentToPastMessageCommand.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchRecentToPastMessageCommand
//
@interface EvhFetchRecentToPastMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSNumber* anchor;

@property(nonatomic, copy) NSNumber* count;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

