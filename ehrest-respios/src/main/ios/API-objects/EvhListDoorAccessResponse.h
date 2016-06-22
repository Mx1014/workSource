//
// EvhListDoorAccessResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhDoorAccessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListDoorAccessResponse
//
@interface EvhListDoorAccessResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

@property(nonatomic, copy) NSNumber* role;

// item type EvhDoorAccessDTO*
@property(nonatomic, strong) NSMutableArray* doors;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

