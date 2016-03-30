//
// EvhListNeighborUsersCommandResponse.h
// generated at 2016-03-30 10:13:09 
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

