//
// EvhBizDetailActionData.h
// generated at 2016-04-06 19:59:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBizDetailActionData
//
@interface EvhBizDetailActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* url;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

