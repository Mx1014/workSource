//
// EvhGetBusinessesByScopeCommand.h
// generated at 2016-04-19 13:40:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBusinessesByScopeCommand
//
@interface EvhGetBusinessesByScopeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* scopeType;

@property(nonatomic, copy) NSNumber* scopeId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

