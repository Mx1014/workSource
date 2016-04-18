//
// EvhListRecordsByTaskIdCommand.h
// generated at 2016-04-18 14:48:52 
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

