//
// EvhDownAppActionData.h
// generated at 2016-03-31 15:43:21 
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

