//
// EvhPropFamilyDTO.h
// generated at 2016-04-01 15:40:23 
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

