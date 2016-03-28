//
// EvhAddOrgAddressCommand.h
// generated at 2016-03-25 19:05:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddOrgAddressCommand
//
@interface EvhAddOrgAddressCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* orgId;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSNumber* areaId;

@property(nonatomic, copy) NSString* address;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

