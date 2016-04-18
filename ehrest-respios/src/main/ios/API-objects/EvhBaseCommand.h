//
// EvhBaseCommand.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBaseCommand
//
@interface EvhBaseCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* type;

@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

