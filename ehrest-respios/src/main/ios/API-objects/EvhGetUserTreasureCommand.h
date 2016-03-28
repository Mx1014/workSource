//
// EvhGetUserTreasureCommand.h
// generated at 2016-03-25 19:05:18 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserTreasureCommand
//
@interface EvhGetUserTreasureCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* uid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

