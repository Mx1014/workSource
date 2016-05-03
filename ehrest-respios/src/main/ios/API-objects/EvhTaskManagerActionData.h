//
// EvhTaskManagerActionData.h
// generated at 2016-04-29 18:56:01 
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

