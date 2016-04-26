//
// EvhAppIdStatusCommand.h
// generated at 2016-04-26 18:22:54 
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

