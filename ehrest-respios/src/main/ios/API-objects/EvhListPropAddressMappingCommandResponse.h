//
// EvhListPropAddressMappingCommandResponse.h
// generated at 2016-04-12 19:00:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPropAddressMappingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropAddressMappingCommandResponse
//
@interface EvhListPropAddressMappingCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhPropAddressMappingDTO*
@property(nonatomic, strong) NSMutableArray* members;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

