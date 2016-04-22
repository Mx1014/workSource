//
// EvhGetBusinessesByScopeCommand.h
// generated at 2016-04-22 13:56:45 
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

