//
// EvhBusinessScope.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessScope
//
@interface EvhBusinessScope
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* scopeCode;

@property(nonatomic, copy) NSNumber* scopeId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

