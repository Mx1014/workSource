//
// EvhGetCommunitiesByNameAndCityIdCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCommunitiesByNameAndCityIdCommand
//
@interface EvhGetCommunitiesByNameAndCityIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

