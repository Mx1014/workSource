//
// EvhPunchGeoPointDTO.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchGeoPointDTO
//
@interface EvhPunchGeoPointDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSNumber* distance;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

