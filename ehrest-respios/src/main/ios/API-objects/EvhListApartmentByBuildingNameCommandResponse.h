//
// EvhListApartmentByBuildingNameCommandResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
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

