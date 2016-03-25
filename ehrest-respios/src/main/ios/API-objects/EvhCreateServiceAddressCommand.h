//
// EvhCreateServiceAddressCommand.h
// generated at 2016-03-25 17:08:12 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateServiceAddressCommand
//
@interface EvhCreateServiceAddressCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* regionId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSString* contactName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

