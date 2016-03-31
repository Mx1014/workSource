//
// EvhListPropOwnerCommandResponse.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPropOwnerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropOwnerCommandResponse
//
@interface EvhListPropOwnerCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhPropOwnerDTO*
@property(nonatomic, strong) NSMutableArray* members;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

