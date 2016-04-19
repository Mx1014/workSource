//
// EvhListRecordsByTaskIdCommand.h
// generated at 2016-04-19 12:41:53 
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

