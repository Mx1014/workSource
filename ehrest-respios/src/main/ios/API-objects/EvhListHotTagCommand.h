//
// EvhListHotTagCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListHotTagCommand
//
@interface EvhListHotTagCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSString* serviceType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

