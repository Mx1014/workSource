//
// EvhJoinVideoConfCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhJoinVideoConfCommand
//
@interface EvhJoinVideoConfCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* confId;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

