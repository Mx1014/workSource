//
// EvhDoorLinglingExtraKeyDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorLinglingExtraKeyDTO
//
@interface EvhDoorLinglingExtraKeyDTO
    : NSObject<EvhJsonSerializable>


// item type NSString*
@property(nonatomic, strong) NSMutableArray* keys;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* storeyAuthList;

@property(nonatomic, copy) NSNumber* authStorey;

@property(nonatomic, copy) NSNumber* authLevel;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

