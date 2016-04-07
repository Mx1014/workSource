//
// EvhDownAppActionData.h
// generated at 2016-04-07 15:16:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDownAppActionData
//
@interface EvhDownAppActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* iosUrl;

@property(nonatomic, copy) NSString* androidUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

