//
// EvhListDoorAccessResponse.h
// generated at 2016-03-28 15:56:08 
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

// item type EvhDoorAccessDTO*
@property(nonatomic, strong) NSMutableArray* doors;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

