//
// EvhListDoorAuthResponse.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhDoorAuthDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListDoorAuthResponse
//
@interface EvhListDoorAuthResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhDoorAuthDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

