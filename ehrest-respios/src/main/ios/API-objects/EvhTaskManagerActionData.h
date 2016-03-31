//
// EvhTaskManagerActionData.h
// generated at 2016-03-31 11:07:26 
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

