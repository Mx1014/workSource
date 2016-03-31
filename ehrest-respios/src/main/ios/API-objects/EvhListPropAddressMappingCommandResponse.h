//
// EvhListPropAddressMappingCommandResponse.h
// generated at 2016-03-31 13:49:12 
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

