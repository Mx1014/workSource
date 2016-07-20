//
// EvhBizOrderHolder.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhObject.h"
#import "EvhBoolean.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBizOrderHolder
//
@interface EvhBizOrderHolder
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) id body;

@property(nonatomic, strong) EvhBoolean* result;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

