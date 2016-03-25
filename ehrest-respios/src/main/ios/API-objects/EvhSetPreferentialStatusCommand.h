//
// EvhSetPreferentialStatusCommand.h
// generated at 2016-03-25 15:57:22 
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

