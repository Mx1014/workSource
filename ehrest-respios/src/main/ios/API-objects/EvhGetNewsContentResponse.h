//
// EvhGetNewsContentResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetNewsContentResponse
//
@interface EvhGetNewsContentResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* content;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

