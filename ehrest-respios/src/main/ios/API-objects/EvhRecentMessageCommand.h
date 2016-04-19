//
// EvhRecentMessageCommand.h
// generated at 2016-04-19 12:41:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecentMessageCommand
//
@interface EvhRecentMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* deviceId;

@property(nonatomic, copy) NSNumber* anchor;

@property(nonatomic, copy) NSNumber* count;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

