//
// EvhTaskManagerActionData.h
// generated at 2016-03-25 19:05:19 
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

