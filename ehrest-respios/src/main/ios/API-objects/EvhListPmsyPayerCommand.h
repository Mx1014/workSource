//
// EvhListPmsyPayerCommand.h
// generated at 2016-04-29 18:56:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPmsyPayerCommand
//
@interface EvhListPmsyPayerCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* creatorId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

