//
// EvhListDoorAuthResponse.h
// generated at 2016-04-19 12:41:54 
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

