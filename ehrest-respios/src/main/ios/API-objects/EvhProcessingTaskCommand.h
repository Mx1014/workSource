//
// EvhProcessingTaskCommand.h
// generated at 2016-03-25 09:26:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhProcessingTaskCommand
//
@interface EvhProcessingTaskCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

@property(nonatomic, copy) NSNumber* taskId;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* taskStatus;

@property(nonatomic, copy) NSString* taskCategory;

@property(nonatomic, copy) NSNumber* privateFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

