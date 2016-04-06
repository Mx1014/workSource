//
// EvhSetPreferentialStatusCommand.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetPreferentialStatusCommand
//
@interface EvhSetPreferentialStatusCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

