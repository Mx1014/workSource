//
// EvhListRecordsByTaskIdCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRecordsByTaskIdCommand
//
@interface EvhListRecordsByTaskIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* taskId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

