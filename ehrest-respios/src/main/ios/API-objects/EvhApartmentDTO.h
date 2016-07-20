//
// EvhApartmentDTO.h
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

