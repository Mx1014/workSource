//
// EvhClaimAddressCommand.h
// generated at 2016-04-12 19:00:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhClaimAddressCommand
//
@interface EvhClaimAddressCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* replacedAddressId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* apartmentName;

@property(nonatomic, copy) NSNumber* historyId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

