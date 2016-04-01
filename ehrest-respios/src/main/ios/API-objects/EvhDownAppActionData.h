//
// EvhDownAppActionData.h
// generated at 2016-03-31 20:15:31 
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

