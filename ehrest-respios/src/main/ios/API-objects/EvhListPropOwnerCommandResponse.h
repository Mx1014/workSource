//
// EvhListPropOwnerCommandResponse.h
// generated at 2016-04-08 20:09:21 
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

