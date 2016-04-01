//
// EvhListApartmentByBuildingNameCommandResponse.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhApartmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListApartmentByBuildingNameCommandResponse
//
@interface EvhListApartmentByBuildingNameCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* apartmentLivingCount;

// item type EvhApartmentDTO*
@property(nonatomic, strong) NSMutableArray* apartmentList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

