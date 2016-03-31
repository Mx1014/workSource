//
// EvhAddOrgAddressCommand.h
// generated at 2016-03-31 13:49:12 
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

