//
// EvhGetUserPointCommand.h
// generated at 2016-04-05 13:45:24 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserPointCommand
//
@interface EvhGetUserPointCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* uid;

@property(nonatomic, copy) NSNumber* anchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

