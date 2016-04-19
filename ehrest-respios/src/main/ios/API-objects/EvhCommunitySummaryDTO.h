//
// EvhCommunitySummaryDTO.h
// generated at 2016-04-19 12:41:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunitySummaryDTO
//
@interface EvhCommunitySummaryDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSNumber* areaId;

@property(nonatomic, copy) NSString* areaName;

@property(nonatomic, copy) NSNumber* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

