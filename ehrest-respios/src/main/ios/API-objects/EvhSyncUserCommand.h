//
// EvhSyncUserCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncUserCommand
//
@interface EvhSyncUserCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* crypto;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSString* areaName;

@property(nonatomic, copy) NSString* communityNames;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* unitName;

@property(nonatomic, copy) NSString* apartmentName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

