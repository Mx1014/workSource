//
// EvhApartmentDTO.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApartmentDTO
//
@interface EvhApartmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSString* apartmentName;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* livingStatus;

@property(nonatomic, copy) NSNumber* areaSize;

@property(nonatomic, copy) NSString* enterpriseName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

