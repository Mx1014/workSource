//
// EvhListNeighborUsersCommandResponse.h
// generated at 2016-03-25 11:43:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhNeighborUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNeighborUsersCommandResponse
//
@interface EvhListNeighborUsersCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* neighborCount;

// item type EvhNeighborUserDTO*
@property(nonatomic, strong) NSMutableArray* neighborUserList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

