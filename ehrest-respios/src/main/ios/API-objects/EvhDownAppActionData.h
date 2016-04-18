//
// EvhDownAppActionData.h
// generated at 2016-04-18 14:48:51 
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

