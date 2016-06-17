//
// EvhDoorAccessCapapilityDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessCapapilityDTO
//
@interface EvhDoorAccessCapapilityDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* isSupportQR;

@property(nonatomic, copy) NSNumber* isSupportSmart;

@property(nonatomic, copy) NSString* qrDriver;

@property(nonatomic, copy) NSString* smartDriver;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

