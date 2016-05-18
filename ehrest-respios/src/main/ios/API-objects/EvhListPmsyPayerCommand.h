//
// EvhListPmsyPayerCommand.h
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

