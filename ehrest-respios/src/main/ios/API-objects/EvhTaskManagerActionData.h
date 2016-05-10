//
// EvhTaskManagerActionData.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTaskManagerActionData
//
@interface EvhTaskManagerActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* module;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

