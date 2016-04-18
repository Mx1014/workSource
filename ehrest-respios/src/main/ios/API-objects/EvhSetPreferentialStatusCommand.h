//
// EvhSetPreferentialStatusCommand.h
// generated at 2016-04-18 14:48:51 
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

