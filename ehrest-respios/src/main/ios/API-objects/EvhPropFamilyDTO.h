//
// EvhPropFamilyDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

