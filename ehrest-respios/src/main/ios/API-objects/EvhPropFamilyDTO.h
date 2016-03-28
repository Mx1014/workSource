//
// EvhPropFamilyDTO.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropFamilyDTO
//
@interface EvhPropFamilyDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* memberCount;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* livingStatus;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSNumber* areaSize;

@property(nonatomic, copy) NSNumber* owed;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

