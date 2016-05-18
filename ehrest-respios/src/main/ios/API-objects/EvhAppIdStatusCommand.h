//
// EvhAppIdStatusCommand.h
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

