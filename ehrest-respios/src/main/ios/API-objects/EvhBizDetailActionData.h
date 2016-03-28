//
// EvhBizDetailActionData.h
// generated at 2016-03-25 19:05:19 
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

