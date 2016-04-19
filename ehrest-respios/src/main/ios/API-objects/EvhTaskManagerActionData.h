//
// EvhTaskManagerActionData.h
// generated at 2016-04-19 12:41:52 
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

