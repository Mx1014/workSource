//
// EvhAppIdStatusCommand.h
// generated at 2016-04-07 17:57:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppIdStatusCommand
//
@interface EvhAppIdStatusCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* name;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* appIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

