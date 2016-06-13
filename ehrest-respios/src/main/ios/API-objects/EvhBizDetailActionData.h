//
// EvhBizDetailActionData.h
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

