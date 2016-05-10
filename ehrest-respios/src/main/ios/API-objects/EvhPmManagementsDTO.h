//
// EvhPmManagementsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPmBuildingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmManagementsDTO
//
@interface EvhPmManagementsDTO
    : NSObject<EvhJsonSerializable>


// item type EvhPmBuildingDTO*
@property(nonatomic, strong) NSMutableArray* buildings;

@property(nonatomic, copy) NSString* pmName;

@property(nonatomic, copy) NSString* plate;

@property(nonatomic, copy) NSNumber* pmId;

@property(nonatomic, copy) NSNumber* isAll;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

