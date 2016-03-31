//
// EvhRecentMessageCommand.h
// generated at 2016-03-31 13:49:14 
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

